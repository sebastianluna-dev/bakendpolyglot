package domain

import (
	"encoding/base64"
	"encoding/json"
)

// EncodeCursor returns an opaque base64url cursor (Chapter 12).
func EncodeCursor(v any) string {
	b, _ := json.Marshal(v)
	return base64.RawURLEncoding.EncodeToString(b)
}

// DecodeCursor fills v from a cursor, or returns BadCursor.
func DecodeCursor(raw string, v any, valid func() bool) error {
	b, err := base64.RawURLEncoding.DecodeString(raw)
	if err != nil || json.Unmarshal(b, v) != nil || !valid() {
		return BadCursor()
	}
	return nil
}
