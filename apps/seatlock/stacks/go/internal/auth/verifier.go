// Package auth validates access tokens from the identity provider with golang-jwt and keyfunc
// (Chapter 19). keyfunc fetches the JWKS and refreshes it when a token names an unknown key.
package auth

import (
	"context"
	"errors"
	"time"

	"github.com/MicahParks/keyfunc/v3"
	"github.com/golang-jwt/jwt/v5"

	"github.com/sebastianluna-dev/bakendpolyglot/apps/seatlock/stacks/go/internal/domain"
)

type Verifier struct {
	jwks     keyfunc.Keyfunc
	issuer   string
	audience string
}

type claims struct {
	jwt.RegisteredClaims
	RealmAccess struct {
		Roles []string `json:"roles"`
	} `json:"realm_access"`
}

func New(ctx context.Context, jwksURL, issuer, audience string) (*Verifier, error) {
	k, err := keyfunc.NewDefaultCtx(ctx, []string{jwksURL})
	if err != nil {
		return nil, err
	}
	return &Verifier{jwks: k, issuer: issuer, audience: audience}, nil
}

var errInvalid = errors.New("invalid token")

func (v *Verifier) Verify(_ context.Context, raw string) (domain.Caller, error) {
	var c claims
	tok, err := jwt.ParseWithClaims(raw, &c, v.jwks.Keyfunc,
		jwt.WithValidMethods([]string{"RS256"}), // never "none", never HS256 with a public key
		jwt.WithIssuer(v.issuer),
		jwt.WithAudience(v.audience),
		jwt.WithExpirationRequired(),
		jwt.WithLeeway(30*time.Second))
	if err != nil || !tok.Valid || c.Subject == "" {
		return domain.Caller{}, errInvalid
	}
	return domain.Caller{Sub: c.Subject, Roles: c.RealmAccess.Roles}, nil
}
