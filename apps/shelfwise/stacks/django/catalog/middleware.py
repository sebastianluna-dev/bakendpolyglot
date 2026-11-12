import uuid


def request_id(get_response):
    """Request id (Chapter 11): accept the caller's or create one, and echo it."""

    def middleware(request):
        rid = request.headers.get("x-request-id") or str(uuid.uuid4())
        response = get_response(request)
        response["x-request-id"] = rid
        return response

    return middleware
