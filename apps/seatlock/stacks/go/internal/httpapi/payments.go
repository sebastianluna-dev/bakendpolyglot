package httpapi

import (
	"bytes"
	"context"
	"encoding/json"
	"fmt"
	"net/http"
	"strings"
	"time"

	"github.com/sebastianluna-dev/bakendpolyglot/apps/seatlock/stacks/go/internal/domain"
)

// PaymentsClient calls the provider outside any transaction, with the order id as its
// idempotency key, so a retried checkout never creates a second intent (Chapter 27).
type PaymentsClient struct {
	BaseURL string
	HTTP    *http.Client
}

func NewPaymentsClient(baseURL string) *PaymentsClient {
	return &PaymentsClient{BaseURL: strings.TrimRight(baseURL, "/"), HTTP: &http.Client{Timeout: 5 * time.Second}}
}

func (p *PaymentsClient) CreateIntent(ctx context.Context, orderID string, amountCents int, currency string) (string, error) {
	body, _ := json.Marshal(map[string]any{"orderId": orderID, "amountCents": amountCents, "currency": currency})
	req, err := http.NewRequestWithContext(ctx, http.MethodPost, p.BaseURL+"/payment-intents", bytes.NewReader(body))
	if err != nil {
		return "", err
	}
	req.Header.Set("Content-Type", "application/json")
	req.Header.Set("Idempotency-Key", orderID)
	res, err := p.HTTP.Do(req)
	if err != nil {
		return "", fmt.Errorf("%w: %v", domain.PaymentProviderUnavailable(), err)
	}
	defer res.Body.Close()
	var intent struct {
		ID string `json:"id"`
	}
	if res.StatusCode >= 300 || json.NewDecoder(res.Body).Decode(&intent) != nil || intent.ID == "" {
		return "", domain.PaymentProviderUnavailable()
	}
	return intent.ID, nil
}
