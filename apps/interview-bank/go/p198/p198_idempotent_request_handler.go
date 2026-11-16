// 198 · Idempotent Request Handler
package p198

type APIResponse struct {
	Status int
	Body   string
}

var Conflict = APIResponse{422, "idempotency key reused with a different request"}

type stored struct {
	body     string
	response APIResponse
}

type IdempotentHandler struct {
	operation func(body string) APIResponse
	seen      map[string]stored // key -> first request body and its response
}

func NewIdempotentHandler(operation func(body string) APIResponse) *IdempotentHandler {
	return &IdempotentHandler{operation: operation, seen: map[string]stored{}}
}

func (h *IdempotentHandler) Handle(key, body string) APIResponse {
	if s, ok := h.seen[key]; ok {
		if body != s.body {
			return Conflict
		}
		return s.response
	}
	response := h.operation(body)
	h.seen[key] = stored{body, response}
	return response
}
