using System.Buffers.Text;
using System.Text.Json;

namespace Shelfwise.Domain;

/// <summary>Opaque base64url cursors (Chapter 12).</summary>
public static class Cursor
{
    static readonly JsonSerializerOptions Json = new(JsonSerializerDefaults.Web);

    public static string Encode<T>(T value) => Base64Url.EncodeToString(JsonSerializer.SerializeToUtf8Bytes(value, Json));

    public static T Decode<T>(string raw, Func<T, bool> valid) where T : class
    {
        try
        {
            var value = JsonSerializer.Deserialize<T>(Base64Url.DecodeFromChars(raw), Json);
            if (value is not null && valid(value)) return value;
        }
        catch (Exception e) when (e is FormatException or JsonException) { }
        throw DomainException.BadCursor();
    }
}
