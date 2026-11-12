"""Input DTOs (Chapter 13) as DRF serializers. Shape rules only; business rules live in loans.py."""

from datetime import date

from rest_framework import serializers


class StrictSerializer(serializers.Serializer):
    """DRF ignores unknown fields by default; Shelfwise rejects them (no mass assignment)."""

    def to_internal_value(self, data):
        if not isinstance(data, dict):
            raise serializers.ValidationError({"body": ["must be a JSON object"]})
        unknown = set(data) - set(self.fields)
        if unknown:
            raise serializers.ValidationError({k: ["is not allowed"] for k in sorted(unknown)})
        return super().to_internal_value(data)


def not_in_future(year):
    if year is not None and year > date.today().year:
        raise serializers.ValidationError("cannot be in the future")
    return year


class CreateBookSerializer(StrictSerializer):
    title = serializers.CharField(min_length=1, max_length=200, trim_whitespace=False)
    author = serializers.CharField(min_length=1, max_length=200, trim_whitespace=False)
    isbn = serializers.RegexField(r"^97[89]\d{10}$", error_messages={"invalid": "must be an ISBN-13 starting with 978 or 979"})
    year = serializers.IntegerField(min_value=1450, allow_null=True, required=False, default=None,
                                    validators=[not_in_future])


class UpdateBookSerializer(StrictSerializer):
    title = serializers.CharField(min_length=1, max_length=200, required=False, trim_whitespace=False)
    author = serializers.CharField(min_length=1, max_length=200, required=False, trim_whitespace=False)
    year = serializers.IntegerField(min_value=1450, allow_null=True, required=False, validators=[not_in_future])

    def validate(self, attrs):
        if not attrs:
            raise serializers.ValidationError({"body": ["at least one field is required"]})
        return attrs


class CreateCopySerializer(StrictSerializer):
    barcode = serializers.CharField(min_length=1, max_length=40)


class CreateMemberSerializer(StrictSerializer):
    email = serializers.EmailField(max_length=254)
    name = serializers.CharField(min_length=1, max_length=200)


class CreateLoanSerializer(StrictSerializer):
    copyId = serializers.IntegerField(min_value=1)
    memberId = serializers.IntegerField(min_value=1)


class ListQuerySerializer(serializers.Serializer):
    q = serializers.CharField(required=False, allow_blank=True)
    after = serializers.CharField(required=False)
    limit = serializers.IntegerField(min_value=1, max_value=100, default=20)
    status = serializers.ChoiceField(choices=["open", "all"], default="all")
