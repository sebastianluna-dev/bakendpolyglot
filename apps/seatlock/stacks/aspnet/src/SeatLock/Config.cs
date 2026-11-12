using Npgsql;

namespace SeatLock;

/// <summary>Typed configuration, validated at startup; every problem is reported at once (Chapter 16).</summary>
public sealed record AppConfig(
    int Port, string DatabaseUrl, string Issuer, string JwksUrl, string Audience, string? RedisUrl,
    string PaymentsUrl, string WebhookSecret, int HoldTtlSeconds, int ReaperIntervalSeconds, int MaxSeatsPerHold)
{
    public static AppConfig Load(Func<string, string?> env)
    {
        var problems = new List<string>();
        int Num(string key, int def, int min, int max)
        {
            var raw = env(key);
            if (string.IsNullOrEmpty(raw)) return def;
            if (int.TryParse(raw, out var n) && n >= min && n <= max) return n;
            problems.Add($"{key}: must be an integer between {min} and {max}");
            return def;
        }
        string Need(string key)
        {
            var v = env(key);
            if (string.IsNullOrEmpty(v)) problems.Add($"{key}: is required");
            return (v ?? "").TrimEnd('/');
        }

        var issuer = Need("OIDC_ISSUER");
        var config = new AppConfig(
            Num("PORT", 3000, 1, 65535),
            Need("DATABASE_URL"),
            issuer,
            env("OIDC_JWKS_URL") is { Length: > 0 } jwks ? jwks : issuer + "/protocol/openid-connect/certs",
            env("OIDC_AUDIENCE") is { Length: > 0 } aud ? aud : "seatlock-api",
            env("REDIS_URL") is { Length: > 0 } redis ? redis : null,
            Need("PAYMENTS_URL"),
            Need("PAYMENT_WEBHOOK_SECRET"),
            Num("HOLD_TTL_SECONDS", 600, 1, 3600),
            Num("REAPER_INTERVAL_SECONDS", 60, 0, 3600),
            Num("MAX_SEATS_PER_HOLD", 6, 1, 20));
        if (problems.Count > 0) throw new InvalidOperationException("Invalid configuration:\n  " + string.Join("\n  ", problems));
        return config;
    }

    /// <summary>DATABASE_URL is a postgres:// URI; Npgsql wants a key=value connection string.</summary>
    public string ConnectionString()
    {
        if (!DatabaseUrl.StartsWith("postgres://") && !DatabaseUrl.StartsWith("postgresql://")) return DatabaseUrl;
        var uri = new Uri(DatabaseUrl);
        var userInfo = uri.UserInfo.Split(':', 2);
        return new NpgsqlConnectionStringBuilder
        {
            Host = uri.Host,
            Port = uri.Port > 0 ? uri.Port : 5432,
            Username = Uri.UnescapeDataString(userInfo[0]),
            Password = userInfo.Length > 1 ? Uri.UnescapeDataString(userInfo[1]) : null,
            Database = uri.AbsolutePath.TrimStart('/'),
            MaxPoolSize = 50,
        }.ConnectionString;
    }

    /// <summary>StackExchange.Redis takes "host:port", not a redis:// URL.</summary>
    public string? RedisConfiguration() =>
        RedisUrl is null ? null : RedisUrl.StartsWith("redis://") ? new Uri(RedisUrl).Authority : RedisUrl;
}
