package dev.seatlock.service;

import dev.seatlock.AppConfig;
import dev.seatlock.domain.DomainException;
import java.time.Duration;
import java.util.Map;
import java.util.UUID;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

/** The payment provider, called outside any transaction with the order id as its idempotency key (Chapter 27). */
@Component
public class PaymentsClient {
    private record Intent(String id) {}

    private final RestClient http;

    public PaymentsClient(AppConfig config) {
        var factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(Duration.ofSeconds(2));
        factory.setReadTimeout(Duration.ofSeconds(5));
        this.http = RestClient.builder().baseUrl(config.paymentsUrl()).requestFactory(factory).build();
    }

    public String createIntent(UUID orderId, int amountCents, String currency) {
        try {
            var intent = http.post().uri("/payment-intents")
                    .header("Idempotency-Key", orderId.toString())
                    .body(Map.of("orderId", orderId.toString(), "amountCents", amountCents, "currency", currency))
                    .retrieve().body(Intent.class);
            if (intent == null || intent.id() == null) throw DomainException.paymentProviderUnavailable();
            return intent.id();
        } catch (RestClientException e) {
            throw DomainException.paymentProviderUnavailable();
        }
    }
}
