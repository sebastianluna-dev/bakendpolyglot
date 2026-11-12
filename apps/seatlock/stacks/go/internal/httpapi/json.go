package httpapi

import (
	"encoding/json"
	"fmt"
	"io"
	"net/http"
	"regexp"
	"sort"
	"strings"
	"time"
	"unicode/utf8"

	"github.com/sebastianluna-dev/bakendpolyglot/apps/seatlock/stacks/go/internal/domain"
)

// object is a JSON object read field by field, so we can reject unknown fields and report every
// invalid field at once (Chapter 13). Go's encoding/json would stop at the first error.
type object struct {
	path   string // "" at the top, "sections[0]." inside a nested object
	fields map[string]json.RawMessage
	errs   map[string][]string // shared by nested objects
}

func readObject(r *http.Request, allowed ...string) (*object, error) {
	body, err := io.ReadAll(io.LimitReader(r.Body, 100<<10))
	if err != nil {
		return nil, domain.InvalidBody()
	}
	if !json.Valid(body) {
		return nil, domain.InvalidBody()
	}
	o := &object{errs: map[string][]string{}}
	o.parse(body, allowed)
	return o, nil
}

func (o *object) parse(raw json.RawMessage, allowed []string) {
	if json.Unmarshal(raw, &o.fields) != nil || o.fields == nil {
		o.fields = map[string]json.RawMessage{}
		o.fail("", "must be a JSON object")
		return
	}
	allow := map[string]bool{}
	for _, a := range allowed {
		allow[a] = true
	}
	keys := make([]string, 0, len(o.fields))
	for k := range o.fields {
		keys = append(keys, k)
	}
	sort.Strings(keys)
	for _, k := range keys {
		if !allow[k] {
			o.fail(k, "is not allowed")
		}
	}
}

func (o *object) fail(field, msg string) {
	name := strings.TrimSuffix(o.path+field, ".")
	if name == "" {
		name = "body"
	}
	o.errs[name] = append(o.errs[name], msg)
}

func (o *object) err() error {
	if len(o.errs) == 0 {
		return nil
	}
	return domain.Validation(o.errs)
}

func (o *object) str(field string, min, max int) string {
	raw, ok := o.fields[field]
	if !ok {
		o.fail(field, "is required")
		return ""
	}
	var s string
	if json.Unmarshal(raw, &s) != nil {
		o.fail(field, "must be a string")
		return ""
	}
	if n := utf8.RuneCountInString(s); n < min || n > max {
		o.fail(field, fmt.Sprintf("must be %d–%d characters", min, max))
	}
	return s
}

func (o *object) integer(field string, min, max int) int {
	raw, ok := o.fields[field]
	if !ok {
		o.fail(field, "is required")
		return 0
	}
	var n int
	if json.Unmarshal(raw, &n) != nil {
		o.fail(field, "must be an integer")
		return 0
	}
	if n < min || n > max {
		o.fail(field, fmt.Sprintf("must be between %d and %d", min, max))
	}
	return n
}

func (o *object) datetime(field string) time.Time {
	s := o.str(field, 1, 64)
	if s == "" {
		return time.Time{}
	}
	t, err := time.Parse(time.RFC3339, s)
	if err != nil {
		o.fail(field, "must be an RFC 3339 date-time")
	}
	return t
}

var uuidRe = regexp.MustCompile(`^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$`)

func isUUID(s string) bool { return uuidRe.MatchString(s) }

func (o *object) uuid(field string) string {
	s := o.str(field, 1, 64)
	if s != "" && !isUUID(s) {
		o.fail(field, "must be a UUID")
	}
	return strings.ToLower(s)
}

// uuids reads a non-empty array of distinct UUIDs.
func (o *object) uuids(field string, max int) []string {
	raw, ok := o.fields[field]
	if !ok {
		o.fail(field, "is required")
		return nil
	}
	var list []string
	if json.Unmarshal(raw, &list) != nil {
		o.fail(field, "must be an array of UUIDs")
		return nil
	}
	seen := map[string]bool{}
	for i, s := range list {
		if !isUUID(s) {
			o.fail(fmt.Sprintf("%s[%d]", field, i), "must be a UUID")
		}
		list[i] = strings.ToLower(s)
		if seen[list[i]] {
			o.fail(field, "seats must be unique")
		}
		seen[list[i]] = true
	}
	if len(list) == 0 || len(list) > max {
		o.fail(field, fmt.Sprintf("must hold 1 to %d seats", max))
	}
	return list
}

// objects reads an array of nested objects; each element shares this object's error map.
func (o *object) objects(field string, min, max int, allowed ...string) []*object {
	raw, ok := o.fields[field]
	if !ok {
		o.fail(field, "is required")
		return nil
	}
	var items []json.RawMessage
	if json.Unmarshal(raw, &items) != nil {
		o.fail(field, "must be an array")
		return nil
	}
	if len(items) < min || len(items) > max {
		o.fail(field, fmt.Sprintf("must have %d to %d items", min, max))
	}
	out := make([]*object, len(items))
	for i, item := range items {
		out[i] = &object{path: fmt.Sprintf("%s%s[%d].", o.path, field, i), errs: o.errs}
		out[i].parse(item, allowed)
	}
	return out
}

func writeJSON(w http.ResponseWriter, status int, v any) {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(status)
	_ = json.NewEncoder(w).Encode(v)
}
