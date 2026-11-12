package httpapi

import (
	"encoding/json"
	"errors"
	"log/slog"
	"net/http"

	"github.com/sebastianluna-dev/bakendpolyglot/apps/shelfwise/stacks/go/internal/domain"
)

const typeBase = "https://shelfwise.dev/problems/"

// writeError renders any error as application/problem+json (Chapter 12). Go handles errors where they
// happen, so handlers call this directly instead of throwing to a filter.
func writeError(w http.ResponseWriter, r *http.Request, err error) {
	var de *domain.Error
	if !errors.As(err, &de) {
		slog.ErrorContext(r.Context(), "unhandled error", "err", err, "path", r.URL.Path, "requestId", w.Header().Get("X-Request-Id"))
		de = &domain.Error{Code: "internal", Status: 500, Title: "Internal error",
			Detail: "Something went wrong. Quote request id " + w.Header().Get("X-Request-Id") + "."}
	}
	body := map[string]any{"type": typeBase + de.Code, "title": de.Title, "status": de.Status, "detail": de.Detail, "instance": r.URL.Path}
	for k, v := range de.Extensions {
		body[k] = v
	}
	w.Header().Set("Content-Type", "application/problem+json")
	w.WriteHeader(de.Status)
	_ = json.NewEncoder(w).Encode(body)
}
