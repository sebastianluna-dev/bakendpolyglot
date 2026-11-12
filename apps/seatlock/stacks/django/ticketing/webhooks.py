"""POST /webhooks/payments. A plain Django view, not DRF: the signature covers the raw bytes, so
nothing may parse (and re-serialise) the body before we verify it (Chapter 24)."""

import hashlib
import hmac
import json

from django.conf import settings
from django.http import HttpResponse
from django.views.decorators.csrf import csrf_exempt
from django.views.decorators.http import require_POST

from . import services
from .errors import invalid_body, invalid_signature
from .problems import problem_response

KNOWN = {"payment_intent.succeeded", "payment_intent.failed"}


@csrf_exempt
@require_POST
def payments(request):
    raw = request.body
    expected = hmac.new(settings.SEATLOCK["PAYMENT_WEBHOOK_SECRET"].encode(), raw, hashlib.sha256).hexdigest()
    if not hmac.compare_digest(request.headers.get("X-Signature", ""), expected):  # constant time
        return problem_response(invalid_signature(), request.path)
    try:
        event = json.loads(raw)
        event_id, event_type, data = event["id"], event["type"], event["data"]
        intent_id = data["paymentIntentId"]
        if not all(isinstance(v, str) and v for v in (event_id, event_type, intent_id)):
            raise ValueError
    except (ValueError, KeyError, TypeError):
        return problem_response(invalid_body(), request.path)
    if event_type in KNOWN:
        order_id = data.get("orderId")
        services.apply_payment_event(event_id, event_type, intent_id, order_id if isinstance(order_id, str) else None)
    return HttpResponse(status=204)
