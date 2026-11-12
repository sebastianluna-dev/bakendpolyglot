package dev.seatlock;

import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/** Typed configuration from the environment variables every stack reads, validated before Spring starts (Chapter 16). */
public record AppConfig(int port, String jdbcUrl, String dbUser, String dbPassword, String issuer, String jwksUrl, String audience,
                        String redisUrl, String paymentsUrl, String webhookSecret, int holdTtlSeconds, int reaperIntervalSeconds,
                        int maxSeatsPerHold) {

    public static AppConfig load(Function<String, String> env) {
        var problems = new ArrayList<String>();
        record Range(String key, int def, int min, int max) {}
        Function<Range, Integer> num = r -> {
            var raw = env.apply(r.key());
            if (raw == null || raw.isEmpty()) return r.def();
            try {
                int n = Integer.parseInt(raw);
                if (n >= r.min() && n <= r.max()) return n;
            } catch (NumberFormatException ignored) {
                // reported below
            }
            problems.add(r.key() + ": must be an integer between " + r.min() + " and " + r.max());
            return r.def();
        };
        Function<String, String> need = key -> {
            var v = env.apply(key);
            if (v == null || v.isEmpty()) {
                problems.add(key + ": is required");
                return "";
            }
            return v.replaceAll("/$", "");
        };

        var url = need.apply("DATABASE_URL");
        String jdbcUrl = "", user = null, password = null;
        if (!url.isEmpty()) {
            var uri = URI.create(url);
            var info = uri.getRawUserInfo() == null ? new String[0] : uri.getRawUserInfo().split(":", 2);
            jdbcUrl = "jdbc:postgresql://" + uri.getHost() + ":" + (uri.getPort() > 0 ? uri.getPort() : 5432) + uri.getPath();
            user = info.length > 0 ? URLDecoder.decode(info[0], StandardCharsets.UTF_8) : null;
            password = info.length > 1 ? URLDecoder.decode(info[1], StandardCharsets.UTF_8) : null;
        }
        var issuer = need.apply("OIDC_ISSUER");
        var jwks = env.apply("OIDC_JWKS_URL");
        var audience = env.apply("OIDC_AUDIENCE");
        var redis = env.apply("REDIS_URL");
        var config = new AppConfig(
                num.apply(new Range("PORT", 3000, 1, 65535)), jdbcUrl, user, password, issuer,
                jwks == null || jwks.isEmpty() ? issuer + "/protocol/openid-connect/certs" : jwks,
                audience == null || audience.isEmpty() ? "seatlock-api" : audience,
                redis == null || redis.isEmpty() ? null : redis,
                need.apply("PAYMENTS_URL"), need.apply("PAYMENT_WEBHOOK_SECRET"),
                num.apply(new Range("HOLD_TTL_SECONDS", 600, 1, 3600)),
                num.apply(new Range("REAPER_INTERVAL_SECONDS", 60, 0, 3600)),
                num.apply(new Range("MAX_SEATS_PER_HOLD", 6, 1, 20)));
        if (!problems.isEmpty()) throw new IllegalStateException("Invalid configuration:\n  " + String.join("\n  ", problems));
        return config;
    }

    /** The Spring properties this config stands for. */
    public Map<String, Object> springProperties() {
        var p = new HashMap<String, Object>();
        p.put("server.port", port);
        p.put("spring.datasource.url", jdbcUrl);
        if (dbUser != null) p.put("spring.datasource.username", dbUser);
        if (dbPassword != null) p.put("spring.datasource.password", dbPassword);
        if (redisUrl != null) {
            p.put("spring.data.redis.url", redisUrl);
            p.put("spring.cache.type", "redis");
        } else {
            p.put("spring.cache.type", "none");
        }
        return p;
    }
}
