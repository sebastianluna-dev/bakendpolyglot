"""Client for the payment provider. Called outside any database transaction (Chapter 27 pitfall),
with the order id as the provider's idempotency key, so retries never create a second intent."""

import json
import urllib.error
import urllib.request

from django.conf import settings

from .errors import payment_provider_unavailable


def create_intent(order_id: str, amount_cents: int, currency: str) -> str:
    request = urllib.request.Request(
        settings.SEATLOCK["PAYMENTS_URL"] + "/payment-intents",
        data=json.dumps({"orderId": order_id, "amountCents": amount_cents, "currency": currency}).encode(),
        headers={"Content-Type": "application/json", "Idempotency-Key": order_id},
        method="POST",
    )
    try:
        with urllib.request.urlopen(request, timeout=5) as response:
            return json.load(response)["id"]
    except (urllib.error.URLError, TimeoutError, KeyError, ValueError) as e:
        raise payment_provider_unavailable() from e
