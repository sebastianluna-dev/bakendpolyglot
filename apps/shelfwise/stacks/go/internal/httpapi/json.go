package httpapi

import (
	"encoding/json"
	"fmt"
	"io"
	"net/http"
	"net/mail"
	"regexp"
	"sort"
	"time"
	"unicode/utf8"

	"github.com/sebastianluna-dev/bakendpolyglot/apps/shelfwise/stacks/go/internal/domain"
)

// object is a JSON request body read field by field, so we can reject unknown fields and report
// every invalid field at once (Chapter 13). Go's encoding/json would stop at the first error.
type object struct {
	fields map[string]json.RawMessage
	errs   map[string][]string
}

func readObject(r *http.Request, allowed ...string) (*object, error) {
	body, err := io.ReadAll(io.LimitReader(r.Body, 100<<10))
	if err != nil {
		return nil, domain.InvalidBody()
	}
	var fields map[string]json.RawMessage
	if err := json.Unmarshal(body, &fields); err != nil {
		var syntax *json.SyntaxError
		if len(body) == 0 || isSyntax(err, &syntax) {
			return nil, domain.InvalidBody()
		}
		// valid JSON but not an object (an array, a number …)
		return &object{fields: map[string]json.RawMessage{}, errs: map[string][]string{"body": {"must be a JSON object"}}}, nil
	}
	o := &object{fields: fields, errs: map[string][]string{}}
	allow := map[string]bool{}
	for _, a := range allowed {
		allow[a] = true
	}
	keys := make([]string, 0, len(fields))
	for k := range fields {
		keys = append(keys, k)
	}
	sort.Strings(keys)
	for _, k := range keys {
		if !allow[k] {
			o.fail(k, "is not allowed")
		}
	}
	return o, nil
}

func isSyntax(err error, target **json.SyntaxError) bool {
	if se, ok := err.(*json.SyntaxError); ok {
		*target = se
		return true
	}
	return err == io.ErrUnexpectedEOF
}

func (o *object) fail(field, msg string) { o.errs[field] = append(o.errs[field], msg) }

func (o *object) has(field string) bool { _, ok := o.fields[field]; return ok }

func (o *object) isNull(field string) bool { return string(o.fields[field]) == "null" }

// err returns a 422 validation problem if any field failed.
func (o *object) err() error {
	if len(o.errs) == 0 {
		return nil
	}
	return domain.Validation(o.errs)
}

// str reads a required or optional string with a length range and optional pattern.
func (o *object) str(field string, required bool, min, max int, pattern *regexp.Regexp, patternMsg string) *string {
	raw, ok := o.fields[field]
	if !ok {
		if required {
			o.fail(field, "is required")
		}
		return nil
	}
	var s string
	if json.Unmarshal(raw, &s) != nil {
		o.fail(field, "must be a string")
		return nil
	}
	if n := utf8.RuneCountInString(s); n < min || n > max {
		o.fail(field, fmt.Sprintf("must be %d–%d characters", min, max))
		return nil
	}
	if pattern != nil && !pattern.MatchString(s) {
		o.fail(field, patternMsg)
		return nil
	}
	return &s
}

// integer reads an integer within [min, max]; nullable fields may be JSON null.
func (o *object) integer(field string, required, nullable bool, min, max int64, maxMsg string) *int64 {
	raw, ok := o.fields[field]
	if !ok {
		if required {
			o.fail(field, "is required")
		}
		return nil
	}
	if string(raw) == "null" {
		if !nullable {
			o.fail(field, "cannot be null")
		}
		return nil
	}
	var n int64
	if json.Unmarshal(raw, &n) != nil {
		o.fail(field, "must be an integer")
		return nil
	}
	if n < min {
		o.fail(field, fmt.Sprintf("must be at least %d", min))
		return nil
	}
	if n > max {
		o.fail(field, maxMsg)
		return nil
	}
	return &n
}

func (o *object) email(field string) *string {
	s := o.str(field, true, 3, 254, nil, "")
	if s == nil {
		return nil
	}
	if a, err := mail.ParseAddress(*s); err != nil || a.Address != *s {
		o.fail(field, "must be a valid email address")
		return nil
	}
	return s
}

var isbn13 = regexp.MustCompile(`^97[89][0-9]{10}$`)

func thisYear() int64 { return int64(time.Now().Year()) }

func writeJSON(w http.ResponseWriter, status int, v any) {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(status)
	_ = json.NewEncoder(w).Encode(v)
}
