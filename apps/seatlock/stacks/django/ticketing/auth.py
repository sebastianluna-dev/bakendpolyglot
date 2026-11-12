"""Bearer tokens from the identity provider (Chapter 19) and role checks (Chapter 21)."""

from dataclasses import dataclass

import jwt
from django.conf import settings
from rest_framework.authentication import BaseAuthentication
from rest_framework.exceptions import AuthenticationFailed
from rest_framework.permissions import BasePermission

_CONFIG = settings.SEATLOCK
# PyJWKClient caches the provider's keys and refetches when a token names an unknown kid (key rotation).
_jwks = jwt.PyJWKClient(_CONFIG["OIDC_JWKS_URL"], cache_keys=True, lifespan=300, timeout=5)


@dataclass(frozen=True)
class Caller:
    sub: str
    roles: frozenset[str]
    is_authenticated: bool = True


def verify(token: str) -> Caller:
    try:
        key = _jwks.get_signing_key_from_jwt(token)
        claims = jwt.decode(
            token, key.key, algorithms=["RS256"],               # never accept "none" or HS256 here
            audience=_CONFIG["OIDC_AUDIENCE"], issuer=_CONFIG["OIDC_ISSUER"],
            options={"require": ["exp", "iss", "sub", "aud"]}, leeway=30,
        )
    except jwt.PyJWTError as e:
        raise AuthenticationFailed(str(e)) from e
    roles = (claims.get("realm_access") or {}).get("roles") or []
    return Caller(sub=claims["sub"], roles=frozenset(roles))


class BearerAuthentication(BaseAuthentication):
    def authenticate(self, request):
        header = request.headers.get("Authorization")
        if not header:
            return None  # anonymous; protected views then answer 401
        scheme, _, token = header.partition(" ")
        if scheme.lower() != "bearer" or not token:
            raise AuthenticationFailed("expected a bearer token")
        return verify(token), None

    def authenticate_header(self, request) -> str:
        return 'Bearer realm="seatlock"'  # makes DRF answer 401 (not 403) to anonymous callers


def has_role(role: str) -> type[BasePermission]:
    class HasRole(BasePermission):
        def has_permission(self, request, view) -> bool:
            if not request.user:
                return False  # → 401 via authenticate_header
            return role in request.user.roles

    HasRole.__name__ = f"HasRole_{role}"
    return HasRole


class Authenticated(BasePermission):
    def has_permission(self, request, view) -> bool:
        return bool(request.user)
