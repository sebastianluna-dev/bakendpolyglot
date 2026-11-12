using System.Net.Mail;
using System.Text.Json;
using System.Text.RegularExpressions;
using Shelfwise.Domain;

namespace Shelfwise.Http;

/// <summary>
/// A JSON request body read field by field, so we can reject unknown fields and report every invalid
/// field at once (Chapter 13). Model binding plus DataAnnotations can't tell "absent" from "null".
/// </summary>
public sealed partial class Body
{
    readonly Dictionary<string, JsonElement> fields = new();
    readonly Dictionary<string, List<string>> errors = new();

    public int Count => fields.Count;

    public static async Task<Body> Read(HttpRequest req, params string[] allowed)
    {
        var body = new Body();
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
            if (doc.RootElement.ValueKind != JsonValueKind.Object)
            {
                body.Fail("body", "must be a JSON object");
                return body;
            }
            foreach (var p in doc.RootElement.EnumerateObject())
            {
                body.fields[p.Name] = p.Value.Clone();
                if (!allowed.Contains(p.Name)) body.Fail(p.Name, "is not allowed");
            }
        }
        return body;
    }

    public void Fail(string field, string message)
    {
        if (!errors.TryGetValue(field, out var list)) errors[field] = list = [];
        list.Add(message);
    }

    public bool Has(string field) => fields.ContainsKey(field);

    /// <summary>Throws a 422 validation problem if any field failed.</summary>
    public void ThrowIfInvalid()
    {
        if (errors.Count > 0) throw DomainException.Validation(errors);
    }

    public string? Str(string field, bool required, int min, int max, Regex? pattern = null, string patternMessage = "")
    {
        if (!fields.TryGetValue(field, out var el))
        {
            if (required) Fail(field, "is required");
            return null;
        }
        if (el.ValueKind != JsonValueKind.String)
        {
            Fail(field, "must be a string");
            return null;
        }
        var s = el.GetString()!;
        var length = s.EnumerateRunes().Count();
        if (length < min || length > max)
        {
            Fail(field, $"must be {min}–{max} characters");
            return null;
        }
        if (pattern is not null && !pattern.IsMatch(s))
        {
            Fail(field, patternMessage);
            return null;
        }
        return s;
    }

    /// <summary>An integer within [min, max]; nullable fields may be JSON null.</summary>
    public long? Integer(string field, bool required, bool nullable, long min, long max, string maxMessage)
    {
        if (!fields.TryGetValue(field, out var el))
        {
            if (required) Fail(field, "is required");
            return null;
        }
        if (el.ValueKind == JsonValueKind.Null)
        {
            if (!nullable) Fail(field, "cannot be null");
            return null;
        }
        if (el.ValueKind != JsonValueKind.Number || !el.TryGetInt64(out var n))
        {
            Fail(field, "must be an integer");
            return null;
        }
        if (n < min)
        {
            Fail(field, $"must be at least {min}");
            return null;
        }
        if (n > max)
        {
            Fail(field, maxMessage);
            return null;
        }
        return n;
    }

    public string? Email(string field)
    {
        var s = Str(field, true, 3, 254);
        if (s is null) return null;
        if (!MailAddress.TryCreate(s, out var address) || address.Address != s)
        {
            Fail(field, "must be a valid email address");
            return null;
        }
        return s;
    }

    [GeneratedRegex("^97[89][0-9]{10}$")]
    public static partial Regex Isbn13();

    public static long ThisYear => DateTime.UtcNow.Year;
}
