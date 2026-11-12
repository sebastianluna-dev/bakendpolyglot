"""ORM models over the shared schema (db/migrations). managed = False: SQL migrations own the tables."""

import uuid

from django.contrib.postgres.fields import ArrayField
from django.db import models
from django.utils import timezone


class Event(models.Model):
    id = models.UUIDField(primary_key=True, default=uuid.uuid4)
    name = models.TextField()
    venue = models.TextField()
    starts_at = models.DateTimeField()
    created_by = models.TextField()
    created_at = models.DateTimeField(default=timezone.now)

    class Meta:
        managed = False
        db_table = "events"


class Hold(models.Model):
    ACTIVE, CHECKED_OUT, RELEASED, EXPIRED = "active", "checked_out", "released", "expired"

    id = models.UUIDField(primary_key=True, default=uuid.uuid4)
    event = models.ForeignKey(Event, on_delete=models.DO_NOTHING, db_column="event_id")
    user_id = models.TextField()
    seat_ids = ArrayField(models.UUIDField())
    status = models.TextField(default=ACTIVE)
    created_at = models.DateTimeField(default=timezone.now)
    expires_at = models.DateTimeField()

    class Meta:
        managed = False
        db_table = "holds"

    def visible_status(self, now) -> str:
        """An active hold past its expiry is expired, whether or not the reaper has run yet."""
        return self.EXPIRED if self.status == self.ACTIVE and self.expires_at <= now else self.status


class Seat(models.Model):
    AVAILABLE, HELD, SOLD = "available", "held", "sold"

    id = models.UUIDField(primary_key=True, default=uuid.uuid4)
    event = models.ForeignKey(Event, on_delete=models.DO_NOTHING, db_column="event_id")
    section = models.TextField()
    row_label = models.TextField()
    number = models.IntegerField()
    price_cents = models.IntegerField()
    status = models.TextField(default=AVAILABLE)
    hold = models.ForeignKey(Hold, null=True, on_delete=models.DO_NOTHING, db_column="hold_id")

    class Meta:
        managed = False
        db_table = "seats"


class Order(models.Model):
    PENDING, PAID, FAILED = "pending", "paid", "failed"

    id = models.UUIDField(primary_key=True, default=uuid.uuid4)
    hold = models.OneToOneField(Hold, on_delete=models.DO_NOTHING, db_column="hold_id")
    user_id = models.TextField()
    total_cents = models.IntegerField()
    currency = models.TextField(default="USD")
    status = models.TextField(default=PENDING)
    payment_intent_id = models.TextField(null=True)
    created_at = models.DateTimeField(default=timezone.now)
    updated_at = models.DateTimeField(default=timezone.now)

    class Meta:
        managed = False
        db_table = "orders"


class OrderLine(models.Model):
    pk = models.CompositePrimaryKey("order_id", "seat_id")
    order = models.ForeignKey(Order, on_delete=models.DO_NOTHING, db_column="order_id")
    seat = models.ForeignKey(Seat, on_delete=models.DO_NOTHING, db_column="seat_id")
    price_cents = models.IntegerField()

    class Meta:
        managed = False
        db_table = "order_lines"


class IdempotencyKey(models.Model):
    pk = models.CompositePrimaryKey("user_id", "key")
    key = models.TextField()
    user_id = models.TextField()
    request_hash = models.TextField()
    status = models.IntegerField(null=True)
    response = models.JSONField(null=True)
    created_at = models.DateTimeField(default=timezone.now)

    class Meta:
        managed = False
        db_table = "idempotency_keys"


class WebhookEvent(models.Model):
    id = models.TextField(primary_key=True)
    type = models.TextField()
    received_at = models.DateTimeField(default=timezone.now)

    class Meta:
        managed = False
        db_table = "webhook_events"
