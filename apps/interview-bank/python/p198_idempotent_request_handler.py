# 198 · Idempotent Request Handler
from collections.abc import Callable
from typing import NamedTuple


class ApiResponse(NamedTuple):
    status: int
    body: str


CONFLICT = ApiResponse(422, "idempotency key reused with a different request")


class IdempotentHandler:
    def __init__(self, operation: Callable[[str], ApiResponse]) -> None:
        self.operation = operation
        self.seen: dict[str, tuple[str, ApiResponse]] = {}  # key -> (request body, stored response)

    def handle(self, key: str, body: str) -> ApiResponse:
        stored = self.seen.get(key)
        if stored is not None:
            first_body, response = stored
            return response if body == first_body else CONFLICT
        response = self.operation(body)
        self.seen[key] = (body, response)
        return response


if __name__ == "__main__":
    runs = 0

    def checkout(body: str) -> ApiResponse:
        global runs
        runs += 1
        if body == "hold=3":
            return ApiResponse(409, "seat already sold")
        return ApiResponse(201, f"order {runs}")

    h = IdempotentHandler(checkout)
    assert h.handle("k1", "hold=7") == ApiResponse(201, "order 1")
    assert h.handle("k1", "hold=7") == ApiResponse(201, "order 1")
    assert h.handle("k2", "hold=9") == ApiResponse(201, "order 2")
    assert h.handle("k1", "hold=8") == CONFLICT
    assert h.handle("k1", "hold=7") == ApiResponse(201, "order 1")
    assert runs == 2
    assert h.handle("k3", "hold=3") == ApiResponse(409, "seat already sold")
    assert h.handle("k3", "hold=3") == ApiResponse(409, "seat already sold")
    assert runs == 3
    assert h.handle("k4", "hold=7") == ApiResponse(201, "order 4")
    assert runs == 4
    print("198 ok")
