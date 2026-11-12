"""Input DTOs (Chapter 13) as DRF serializers. Shape rules only; business rules live in services.py."""

from django.conf import settings
from rest_framework import serializers

MAX_SEATS_PER_EVENT = 5000


class StrictSerializer(serializers.Serializer):
    """DRF ignores unknown fields by default; SeatLock rejects them (no mass assignment)."""

    def to_internal_value(self, data):
        if not isinstance(data, dict):
            raise serializers.ValidationError({"body": ["must be a JSON object"]})
        unknown = set(data) - set(self.fields)
        if unknown:
            raise serializers.ValidationError({k: ["is not allowed"] for k in sorted(unknown)})
        return super().to_internal_value(data)


class SectionSerializer(StrictSerializer):
    name = serializers.CharField(min_length=1, max_length=40)
    rows = serializers.IntegerField(min_value=1, max_value=26)
    seatsPerRow = serializers.IntegerField(min_value=1, max_value=50)
    priceCents = serializers.IntegerField(min_value=0, max_value=1_000_000)


class CreateEventSerializer(StrictSerializer):
    name = serializers.CharField(min_length=1, max_length=200)
    venue = serializers.CharField(min_length=1, max_length=200)
    startsAt = serializers.DateTimeField()
    sections = SectionSerializer(many=True, allow_empty=False)

    def validate_sections(self, sections):
        if len(sections) > 10:
            raise serializers.ValidationError("at most 10 sections")
        names = [s["name"] for s in sections]
        if len(set(names)) != len(names):
            raise serializers.ValidationError("section names must be unique")
        if sum(s["rows"] * s["seatsPerRow"] for s in sections) > MAX_SEATS_PER_EVENT:
            raise serializers.ValidationError(f"at most {MAX_SEATS_PER_EVENT} seats per event")
        return sections


class CreateHoldSerializer(StrictSerializer):
    eventId = serializers.UUIDField()
    seatIds = serializers.ListField(child=serializers.UUIDField(), allow_empty=False)

    def validate_seatIds(self, ids):
        limit = settings.SEATLOCK["MAX_SEATS_PER_HOLD"]
        if len(ids) > limit:
            raise serializers.ValidationError(f"at most {limit} seats per hold")
        if len(set(ids)) != len(ids):
            raise serializers.ValidationError("seats must be unique")
        return ids
