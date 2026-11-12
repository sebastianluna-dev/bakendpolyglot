using Npgsql;
using NpgsqlTypes;

namespace Shelfwise.Data;

/// <summary>Small helpers over ADO.NET: positional parameters ($1, $2 …) and one-row reads.</summary>
static class Sql
{
    public static NpgsqlCommand Command(NpgsqlDataSource db, string sql, params NpgsqlParameter[] args)
    {
        var cmd = db.CreateCommand(sql);
        cmd.Parameters.AddRange(args);
        return cmd;
    }

    public static NpgsqlParameter P(object? value) => new() { Value = value ?? DBNull.Value };

    // A typed parameter, needed when the value may be null and SQL does `$n IS NULL`.
    public static NpgsqlParameter P(object? value, NpgsqlDbType type) => new() { Value = value ?? DBNull.Value, NpgsqlDbType = type };

    public static async Task<T?> One<T>(NpgsqlCommand cmd, Func<NpgsqlDataReader, T> read, CancellationToken ct) where T : class
    {
        await using var _ = cmd;
        await using var r = await cmd.ExecuteReaderAsync(ct);
        return await r.ReadAsync(ct) ? read(r) : null;
    }

    public static async Task<List<T>> Many<T>(NpgsqlCommand cmd, Func<NpgsqlDataReader, T> read, CancellationToken ct)
    {
        await using var _ = cmd;
        await using var r = await cmd.ExecuteReaderAsync(ct);
        var list = new List<T>();
        while (await r.ReadAsync(ct)) list.Add(read(r));
        return list;
    }

    public static async Task<bool> Exists(NpgsqlDataSource db, string sql, long id, CancellationToken ct)
    {
        await using var cmd = Command(db, $"SELECT EXISTS ({sql})", P(id));
        return (bool)(await cmd.ExecuteScalarAsync(ct))!;
    }

    public static bool IsUnique(PostgresException e, string constraint) =>
        e.SqlState == PostgresErrorCodes.UniqueViolation && e.ConstraintName == constraint;

    /// <summary>DATABASE_URL is a postgres:// URI; Npgsql wants a key=value connection string.</summary>
    public static string ConnectionString(string url)
    {
        if (!url.StartsWith("postgres://") && !url.StartsWith("postgresql://")) return url;
        var uri = new Uri(url);
        var userInfo = uri.UserInfo.Split(':', 2);
        return new NpgsqlConnectionStringBuilder
        {
            Host = uri.Host,
            Port = uri.Port > 0 ? uri.Port : 5432,
            Username = Uri.UnescapeDataString(userInfo[0]),
            Password = userInfo.Length > 1 ? Uri.UnescapeDataString(userInfo[1]) : null,
            Database = uri.AbsolutePath.TrimStart('/'),
        }.ConnectionString;
    }
}
