namespace Shelfwise;

/// <summary>Typed configuration, validated at startup; every problem is reported at once (Chapter 16).</summary>
public sealed record AppConfig(int Port, string DatabaseUrl, int LoanDays, int MaxLoansPerMember, LogLevel LogLevel)
{
    public static AppConfig Load(Func<string, string?> env, bool needDatabase = true)
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

        var config = new AppConfig(
            Num("PORT", 3000, 1, 65535),
            env("DATABASE_URL") ?? "",
            Num("LOAN_DAYS", 21, 1, 60),
            Num("MAX_LOANS_PER_MEMBER", 5, 1, 50),
            LogLevel.Information);
        if (needDatabase && config.DatabaseUrl == "") problems.Add("DATABASE_URL: is required");

        var level = (env("LOG_LEVEL") ?? "info") switch
        {
            "debug" => LogLevel.Debug,
            "info" => LogLevel.Information,
            "warn" => LogLevel.Warning,
            "error" => LogLevel.Error,
            _ => (LogLevel?)null,
        };
        if (level is null) problems.Add("LOG_LEVEL: must be debug, info, warn or error");

        if (problems.Count > 0)
            throw new InvalidOperationException("Invalid configuration:\n  " + string.Join("\n  ", problems));
        return config with { LogLevel = level!.Value };
    }
}
