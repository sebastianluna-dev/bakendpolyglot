"""Fast tests with no database or identity provider: validation, hold state, webhook signatures."""

import hashlib
import hmac
import json
import uuid
from datetime import timedelta

from django.conf import settings
from django.test import RequestFactory
from django.utils import timezone

from ticketing.models import Hold
from ticketing.serializers import CreateEventSerializer, CreateHoldSerializer
from ticketing.webhooks import payments


def section(**over):
    return {"name": "Floor", "rows": 2, "seatsPerRow": 5, "priceCents": 2500, **over}


def test_event_rejects_duplicate_sections_and_unknown_fields():
    s = CreateEventSerializer(data={"name": "X", "venue": "Y", "startsAt": "2027-01-01T20:00:00Z",
                                    "sections": [section(), section()], "extra": 1})
    assert not s.is_valid()
    assert "extra" in s.errors


def test_hold_limits_seats_and_rejects_duplicates():
    many = [str(uuid.uuid4()) for _ in range(settings.SEATLOCK["MAX_SEATS_PER_HOLD"] + 1)]
    assert not CreateHoldSerializer(data={"eventId": str(uuid.uuid4()), "seatIds": many}).is_valid()
    same = str(uuid.uuid4())
    assert not CreateHoldSerializer(data={"eventId": str(uuid.uuid4()), "seatIds": [same, same]}).is_valid()


def test_an_active_hold_past_expiry_reads_as_expired():
    now = timezone.now()
    hold = Hold(status=Hold.ACTIVE, expires_at=now - timedelta(seconds=1))
    assert hold.visible_status(now) == Hold.EXPIRED
    assert Hold(status=Hold.CHECKED_OUT, expires_at=now - timedelta(seconds=1)).visible_status(now) == Hold.CHECKED_OUT


def test_webhook_rejects_a_bad_signature_before_parsing():
    request = RequestFactory().post("/webhooks/payments", data="not even json", content_type="application/json",
                                    HTTP_X_SIGNATURE="00")
    response = payments(request)
    assert response.status_code == 401
    assert json.loads(response.content)["type"].endswith("/invalid-signature")


def test_webhook_rejects_a_signed_body_of_the_wrong_shape():
    raw = b'{"hello": "world"}'
    sig = hmac.new(settings.SEATLOCK["PAYMENT_WEBHOOK_SECRET"].encode(), raw, hashlib.sha256).hexdigest()
    request = RequestFactory().post("/webhooks/payments", data=raw, content_type="application/json", HTTP_X_SIGNATURE=sig)
    assert payments(request).status_code == 400
