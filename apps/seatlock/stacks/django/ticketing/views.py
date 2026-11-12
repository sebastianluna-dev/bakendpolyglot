"""The handler layer (Chapter 18): parse, authorize, call a service, shape the response."""

import uuid

from django.http import JsonResponse
from rest_framework.response import Response
from rest_framework.views import APIView

from . import services
from .auth import Authenticated, has_role
from .errors import idempotency_key_required, invalid_id
from .idempotency import run_once
from .problems import PROBLEM
from .serializers import CreateEventSerializer, CreateHoldSerializer


def parse_id(raw: str) -> uuid.UUID:
    try:
        return uuid.UUID(raw)
    except ValueError:
        raise invalid_id(raw) from None


def health(request):
    return JsonResponse({"status": "ok"})


def created(body: dict, location: str) -> Response:
    return Response(body, status=201, headers={"Location": location})


class EventsView(APIView):
    permission_classes = [has_role("box-office")]

    def post(self, request):
        s = CreateEventSerializer(data=request.data)
        s.is_valid(raise_exception=True)
        event = services.create_event(s.validated_data, request.user.sub)
        return created(event, f"/events/{event['id']}")


class EventView(APIView):
    authentication_classes = []  # public

    def get(self, request, event_id):
        return Response(services.get_event(parse_id(event_id)), headers={"Cache-Control": "public, max-age=60"})


class SeatMapView(APIView):
    authentication_classes = []

    def get(self, request, event_id):
        return Response(services.seat_map(parse_id(event_id)), headers={"Cache-Control": "no-store"})


class HoldsView(APIView):
    permission_classes = [has_role("customer")]

    def post(self, request):
        s = CreateHoldSerializer(data=request.data)
        s.is_valid(raise_exception=True)
        hold = services.create_hold(request.user.sub, s.validated_data["eventId"], s.validated_data["seatIds"])
        return created(hold, f"/holds/{hold['id']}")


class HoldView(APIView):
    permission_classes = [Authenticated]

    def get(self, request, hold_id):
        return Response(services.get_hold(request.user.sub, parse_id(hold_id)))

    def delete(self, request, hold_id):
        services.release_hold(request.user.sub, parse_id(hold_id))
        return Response(status=204)


class CheckoutView(APIView):
    permission_classes = [Authenticated]

    def post(self, request, hold_id):
        hid = parse_id(hold_id)
        key = request.headers.get("Idempotency-Key", "")
        if not 1 <= len(key) <= 200:
            raise idempotency_key_required()

        def operation():
            return 201, services.checkout(request.user.sub, hid)

        status, body, replayed = run_once(request.user.sub, key, f"POST /holds/{hid}/checkout", request.path, operation)
        headers = {"Idempotent-Replayed": "true"} if replayed else {}
        if status == 201:
            headers["Location"] = f"/orders/{body['id']}"
        return Response(body, status=status, headers=headers, content_type=PROBLEM if status >= 400 else None)


class OrderView(APIView):
    permission_classes = [Authenticated]

    def get(self, request, order_id):
        return Response(services.get_order(request.user.sub, parse_id(order_id)))


class ReaperView(APIView):
    permission_classes = [has_role("box-office")]  # the hold-reaper client's service account has it

    def post(self, request):
        return Response({"released": services.release_expired_holds()})
