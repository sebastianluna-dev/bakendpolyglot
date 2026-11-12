using System.Globalization;
using System.Text.Json;
using SeatLock.Domain;

namespace SeatLock.Http;

/// <summary>
/// A JSON object read field by field, so we can reject unknown fields and report every invalid
/// field at once (Chapter 13). Nested objects share the parent's error map under a path prefix.
/// </summary>
public sealed class Body
{
    readonly Dictionary<string, JsonElement> fields = new();
    readonly Dictionary<string, List<string>> errors;
    readonly string path;

    Body(Dictionary<string, List<string>> errors, string path)
    {
        this.errors = errors;
        this.path = path;
    }

    public static async Task<Body> Read(HttpRequest req, params string[] allowed)
    {
        JsonDocument doc;
        try
        {
            doc = await JsonDocument.ParseAsync(req.Body, default, req.HttpContext.RequestAborted);
        }
        catch (JsonException)
        {
            throw DomainException.InvalidBody();
        }
        using (doc)
        {
            var body = new Body(new Dictionary<string, List<string>>(), "");
            body.Load(doc.RootElement, allowed);
            return body;
        }
    }

    void Load(JsonElement root, string[] allowed)
    {
        if (root.ValueKind != JsonValueKind.Object)
        {
            Fail("", "must be a JSON object");
            return;
        }
        foreach (var p in root.EnumerateObject())
        {
            fields[p.Name] = p.Value.Clone();
            if (!allowed.Contains(p.Name)) Fail(p.Name, "is not allowed");
        }
    }

    public void Fail(string field, string message)
    {
        var name = (path + field).TrimEnd('.');
        if (name == "") name = "body";
        if (!errors.TryGetValue(name, out var list)) errors[name] = list = [];
        list.Add(message);
    }

    public void ThrowIfInvalid()
    {
        if (errors.Count > 0) throw DomainException.Validation(errors);
    }

    bool Get(string field, out JsonElement el)
    {
        if (fields.TryGetValue(field, out el)) return true;
        Fail(field, "is required");
        return false;
    }

    public string Str(string field, int min, int max)
    {
        if (!Get(field, out var el)) return "";
        if (el.ValueKind != JsonValueKind.String)
        {
            Fail(field, "must be a string");
            return "";
        }
        var s = el.GetString()!;
        var length = s.EnumerateRunes().Count();
        if (length < min || length > max) Fail(field, $"must be {min}–{max} characters");
        return s;
    }

    public int Int(string field, int min, int max)
    {
        if (!Get(field, out var el)) return 0;
        if (el.ValueKind != JsonValueKind.Number || !el.TryGetInt32(out var n))
        {
            Fail(field, "must be an integer");
            return 0;
        }
        if (n < min || n > max) Fail(field, $"must be between {min} and {max}");
        return n;
    }

    public DateTimeOffset DateTime(string field)
    {
        var s = Str(field, 1, 64);
        if (s == "") return default;
        if (!DateTimeOffset.TryParse(s, CultureInfo.InvariantCulture, DateTimeStyles.RoundtripKind, out var t) || !s.Contains('T'))
        {
            Fail(field, "must be an RFC 3339 date-time");
            return default;
        }
        return t.ToUniversalTime();
    }

    public Guid Uuid(string field)
    {
        var s = Str(field, 1, 64);
        if (s != "" && Guid.TryParseExact(s, "D", out var id)) return id;
        if (s != "") Fail(field, "must be a UUID");
        return Guid.Empty;
    }

    /// <summary>A non-empty array of distinct UUIDs.</summary>
    public List<Guid> Uuids(string field, int max)
    {
        var list = new List<Guid>();
        if (!Get(field, out var el)) return list;
        if (el.ValueKind != JsonValueKind.Array)
        {
            Fail(field, "must be an array of UUIDs");
            return list;
        }
        var i = 0;
        foreach (var item in el.EnumerateArray())
        {
            if (item.ValueKind == JsonValueKind.String && Guid.TryParseExact(item.GetString(), "D", out var id)) list.Add(id);
            else Fail($"{field}[{i}]", "must be a UUID");
            i++;
        }
        if (list.Distinct().Count() != list.Count) Fail(field, "seats must be unique");
        if (i == 0 || i > max) Fail(field, $"must hold 1 to {max} seats");
        return list;
    }

    /// <summary>An array of nested objects, each validated with the same error map.</summary>
    public List<Body> Objects(string field, int min, int max, params string[] allowed)
    {
        var list = new List<Body>();
        if (!Get(field, out var el)) return list;
        if (el.ValueKind != JsonValueKind.Array)
        {
            Fail(field, "must be an array");
            return list;
        }
        var i = 0;
        foreach (var item in el.EnumerateArray())
        {
            var child = new Body(errors, $"{path}{field}[{i++}].");
            child.Load(item, allowed);
            list.Add(child);
        }
        if (i < min || i > max) Fail(field, $"must have {min} to {max} items");
        return list;
    }
}
