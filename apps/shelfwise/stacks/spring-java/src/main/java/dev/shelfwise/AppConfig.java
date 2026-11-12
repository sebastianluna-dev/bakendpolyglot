package dev.shelfwise;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Typed configuration from the same environment variables every stack reads, validated before Spring
 * starts; every problem is reported at once (Chapter 16).
 */
public record AppConfig(int port, String jdbcUrl, String dbUser, String dbPassword,
                        int loanDays, int maxLoansPerMember, String logLevel) {

    public static AppConfig load(Function<String, String> env) {
        var problems = new ArrayList<String>();
        Function<String[], Integer> num = spec -> {
            String raw = env.apply(spec[0]);
            int def = Integer.parseInt(spec[1]), min = Integer.parseInt(spec[2]), max = Integer.parseInt(spec[3]);
            if (raw == null || raw.isEmpty()) return def;
            try {
                int n = Integer.parseInt(raw);
                if (n >= min && n <= max) return n;
            } catch (NumberFormatException ignored) {
                // reported below
            }
            problems.add(spec[0] + ": must be an integer between " + min + " and " + max);
            return def;
        };
        int port = num.apply(new String[] {"PORT", "3000", "1", "65535"});
        int loanDays = num.apply(new String[] {"LOAN_DAYS", "21", "1", "60"});
        int maxLoans = num.apply(new String[] {"MAX_LOANS_PER_MEMBER", "5", "1", "50"});

        String url = env.apply("DATABASE_URL"), jdbcUrl = null, user = null, password = null;
        if (url == null || url.isEmpty()) {
            problems.add("DATABASE_URL: is required");
        } else {
            try { // postgres://user:pass@host:port/db  →  jdbc:postgresql://host:port/db + credentials
                var uri = URI.create(url);
                var info = uri.getRawUserInfo() == null ? new String[0] : uri.getRawUserInfo().split(":", 2);
                jdbcUrl = "jdbc:postgresql://" + uri.getHost() + ":" + (uri.getPort() > 0 ? uri.getPort() : 5432) + uri.getPath();
                user = info.length > 0 ? decode(info[0]) : null;
                password = info.length > 1 ? decode(info[1]) : null;
            } catch (IllegalArgumentException e) {
                problems.add("DATABASE_URL: must be a postgres:// URL");
            }
        }

        String level = env.apply("LOG_LEVEL") == null ? "info" : env.apply("LOG_LEVEL");
        if (!level.matches("debug|info|warn|error")) problems.add("LOG_LEVEL: must be debug, info, warn or error");

        if (!problems.isEmpty())
            throw new IllegalStateException("Invalid configuration:\n  " + String.join("\n  ", problems));
        return new AppConfig(port, jdbcUrl, user, password, loanDays, maxLoans, level);
    }

    private static String decode(String s) {
        return java.net.URLDecoder.decode(s, java.nio.charset.StandardCharsets.UTF_8);
    }

    /** The Spring properties this config stands for. */
    public Map<String, Object> springProperties() {
        var p = new HashMap<String, Object>();
        p.put("server.port", port);
        p.put("spring.datasource.url", jdbcUrl);
        if (dbUser != null) p.put("spring.datasource.username", dbUser);
        if (dbPassword != null) p.put("spring.datasource.password", dbPassword);
        p.put("logging.level.dev.shelfwise", logLevel.equals("warn") ? "WARN" : logLevel.toUpperCase());
        return p;
    }
}
