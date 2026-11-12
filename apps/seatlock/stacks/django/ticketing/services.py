"""SeatLock's use cases. Views translate HTTP; everything with a rule lives here (Chapter 18)."""

import logging
import string
import uuid
from datetime import UTC, datetime, timedelta

from django.conf import settings
from django.core.cache import cache
from django.db import IntegrityError, transaction
from django.utils import timezone

from . import payments
from .errors import hold_expired, hold_not_active, not_found, seats_unavailable
from .models import Event, Hold, Order, OrderLine, Seat, WebhookEvent

log = logging.getLogger("seatlock")


def iso(dt: datetime) -> str:
    return dt.astimezone(UTC).isoformat().replace("+00:00", "Z")


# ---------- events (public reads are cached; Chapter 25) ----------

def event_json(e: Event, seat_count: int) -> dict:
    return {"id": str(e.id), "name": e.name, "venue": e.venue, "startsAt": iso(e.starts_at), "seatCount": seat_count}


def create_event(data: dict, created_by: str) -> dict:
    with transaction.atomic():
        event = Event.objects.create(name=data["name"], venue=data["venue"], starts_at=data["startsAt"], created_by=created_by)
        seats = [
            Seat(event=event, section=s["name"], row_label=string.ascii_uppercase[r], number=n + 1, price_cents=s["priceCents"])
            for s in data["sections"] for r in range(s["rows"]) for n in range(s["seatsPerRow"])
        ]
        Seat.objects.bulk_create(seats, batch_size=1000)
    return event_json(event, len(seats))


def get_event(event_id: uuid.UUID) -> dict:
    key = f"event:{event_id}:v1"
    if (cached := cache.get(key)) is not None:
        return cached
    event = Event.objects.filter(id=event_id).first()
    if event is None:
        raise not_found("Event", event_id)
    result = event_json(event, Seat.objects.filter(event_id=event_id).count())
    cache.set(key, result, 60)  # events never change after creation; 60 s bounds a stale cache anyway
    return result


def seat_map(event_id: uuid.UUID) -> dict:
    key = f"event:{event_id}:seats:v1"
    layout = cache.get(key)
    if layout is None:
        if not Event.objects.filter(id=event_id).exists():
            raise not_found("Event", event_id)
        layout = [
            {"id": str(s["id"]), "section": s["section"], "row": s["row_label"], "number": s["number"], "priceCents": s["price_cents"]}
            for s in Seat.objects.filter(event_id=event_id).order_by("section", "row_label", "number")
            .values("id", "section", "row_label", "number", "price_cents")
        ]
        cache.set(key, layout, 300)
    # Availability is never cached: a two-second-old "available" sells a held seat.
    status = {str(i): s for i, s in Seat.objects.filter(event_id=event_id).values_list("id", "status")}
    return {"eventId": str(event_id), "items": [{**seat, "status": status[seat["id"]]} for seat in layout]}


# ---------- holds (Chapter 23: claim scarce rows with a conditional UPDATE) ----------

def hold_json(h: Hold, now: datetime) -> dict:
    return {"id": str(h.id), "eventId": str(h.event_id), "seatIds": sorted(str(s) for s in h.seat_ids),
            "status": h.visible_status(now), "expiresAt": iso(h.expires_at)}


def create_hold(user_id: str, event_id: uuid.UUID, seat_ids: list[uuid.UUID]) -> dict:
    now = timezone.now()
    try:
        with transaction.atomic():
            if not Event.objects.filter(id=event_id).exists():
                raise not_found("Event", event_id)
            # Lock the requested rows in id order first, so two overlapping holds can't deadlock.
            list(Seat.objects.select_for_update().filter(event_id=event_id, id__in=seat_ids).order_by("id").values_list("id"))
            hold = Hold.objects.create(event_id=event_id, user_id=user_id, seat_ids=seat_ids, created_at=now,
                                       expires_at=now + timedelta(seconds=settings.SEATLOCK["HOLD_TTL_SECONDS"]))
            claimed = (Seat.objects.filter(event_id=event_id, id__in=seat_ids, status=Seat.AVAILABLE)
                       .update(status=Seat.HELD, hold=hold))
            if claimed != len(seat_ids):
                raise _Unavailable()  # leaving atomic() with an exception rolls everything back
    except _Unavailable:
        free = set(Seat.objects.filter(event_id=event_id, id__in=seat_ids, status=Seat.AVAILABLE).values_list("id", flat=True))
        raise seats_unavailable(sorted(str(s) for s in seat_ids if s not in free)) from None
    return hold_json(hold, now)


class _Unavailable(Exception):
    pass


def owned_hold(user_id: str, hold_id: uuid.UUID, *, lock: bool = False) -> Hold:
    """Object-level authorization (Chapter 21): someone else's hold does not exist for you."""
    qs = Hold.objects.select_for_update() if lock else Hold.objects
    hold = qs.filter(id=hold_id, user_id=user_id).first()
    if hold is None:
        raise not_found("Hold", hold_id)
    return hold


def get_hold(user_id: str, hold_id: uuid.UUID) -> dict:
    return hold_json(owned_hold(user_id, hold_id), timezone.now())


def release_hold(user_id: str, hold_id: uuid.UUID) -> None:
    with transaction.atomic():
        hold = owned_hold(user_id, hold_id, lock=True)
        if hold.visible_status(timezone.now()) != Hold.ACTIVE:
            raise hold_not_active(hold_id)
        Hold.objects.filter(id=hold.id).update(status=Hold.RELEASED)
        Seat.objects.filter(hold_id=hold.id, status=Seat.HELD).update(status=Seat.AVAILABLE, hold=None)


# ---------- checkout ----------

def order_json(o: Order) -> dict:
    seat_ids = [str(s) for s in OrderLine.objects.filter(order_id=o.id).order_by("seat_id").values_list("seat_id", flat=True)]
    return {"id": str(o.id), "holdId": str(o.hold_id), "eventId": str(o.hold.event_id), "seatIds": seat_ids,
            "status": o.status, "totalCents": o.total_cents, "currency": o.currency, "paymentIntentId": o.payment_intent_id}


def checkout(user_id: str, hold_id: uuid.UUID) -> dict:
    """Commit a pending order first, then call the provider: never hold locks across a network call.
    A checkout interrupted after the commit resumes on retry, reusing the same order."""
    now = timezone.now()
    with transaction.atomic():
        hold = owned_hold(user_id, hold_id, lock=True)
        if hold.status == Hold.CHECKED_OUT:
            order = Order.objects.select_related("hold").get(hold_id=hold.id)
            if order.payment_intent_id:
                raise hold_not_active(hold_id)  # already checked out
        elif hold.status == Hold.EXPIRED or hold.visible_status(now) == Hold.EXPIRED:
            raise hold_expired(hold_id)
        elif hold.status != Hold.ACTIVE:
            raise hold_not_active(hold_id)
        else:
            seats = list(Seat.objects.filter(hold_id=hold.id).values_list("id", "price_cents"))
            order = Order.objects.create(hold=hold, user_id=user_id, total_cents=sum(p for _, p in seats))
            OrderLine.objects.bulk_create([OrderLine(order=order, seat_id=s, price_cents=p) for s, p in seats])
            Hold.objects.filter(id=hold.id).update(status=Hold.CHECKED_OUT)

    intent_id = payments.create_intent(str(order.id), order.total_cents, order.currency)
    Order.objects.filter(id=order.id, payment_intent_id__isnull=True).update(payment_intent_id=intent_id, updated_at=timezone.now())
    return order_json(Order.objects.select_related("hold").get(id=order.id))


def get_order(user_id: str, order_id: uuid.UUID) -> dict:
    order = Order.objects.select_related("hold").filter(id=order_id, user_id=user_id).first()
    if order is None:
        raise not_found("Order", order_id)
    return order_json(order)


# ---------- payment webhooks (Chapter 24: verify, dedupe, apply idempotently) ----------

def apply_payment_event(event_id: str, event_type: str, intent_id: str, order_id: str | None) -> None:
    with transaction.atomic():
        try:
            with transaction.atomic():  # savepoint: a duplicate must not abort the outer transaction
                WebhookEvent.objects.create(id=event_id, type=event_type)
        except IntegrityError:
            return  # seen before
        order = Order.objects.select_for_update().filter(payment_intent_id=intent_id).first()
        if order is None and order_id:
            # The webhook can beat us to saving the intent id; the provider echoes our order id.
            order = Order.objects.select_for_update().filter(id=_uuid_or_none(order_id), payment_intent_id__isnull=True).first()
        if order is None or order.status != Order.PENDING:
            log.info("payment event %s ignored (order %s)", event_id, order and order.status)
            return  # unknown, or already final: late events never undo a final state
        if event_type == "payment_intent.succeeded":
            Order.objects.filter(id=order.id).update(status=Order.PAID, payment_intent_id=intent_id, updated_at=timezone.now())
            Seat.objects.filter(hold_id=order.hold_id).update(status=Seat.SOLD)
        elif event_type == "payment_intent.failed":
            Order.objects.filter(id=order.id).update(status=Order.FAILED, payment_intent_id=intent_id, updated_at=timezone.now())
            Seat.objects.filter(hold_id=order.hold_id).update(status=Seat.AVAILABLE, hold=None)
            Hold.objects.filter(id=order.hold_id).update(status=Hold.RELEASED)


def _uuid_or_none(raw: str) -> uuid.UUID | None:
    try:
        return uuid.UUID(raw)
    except ValueError:
        return None


# ---------- the hold reaper ----------

def release_expired_holds(batch: int = 1000) -> int:
    """One conditional update, idempotent by construction: running it twice releases nothing extra.
    SKIP LOCKED lets several reapers (one per worker) share the work without waiting on each other."""
    with transaction.atomic():
        ids = list(Hold.objects.select_for_update(skip_locked=True)
                   .filter(status=Hold.ACTIVE, expires_at__lte=timezone.now())
                   .values_list("id", flat=True)[:batch])
        if ids:
            Hold.objects.filter(id__in=ids).update(status=Hold.EXPIRED)
            Seat.objects.filter(hold_id__in=ids, status=Seat.HELD).update(status=Seat.AVAILABLE, hold=None)
    return len(ids)
