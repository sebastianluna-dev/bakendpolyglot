// 194 · Parse a Query String
using System.Text;

Check(Same(ParseQuery("genre=sci-fi&sort=title&genre=history"), new() { ["genre"] = ["sci-fi", "history"], ["sort"] = ["title"] }));
Check(Same(ParseQuery("q=war+%26+peace&note=50%25&empty=&flag"),
    new() { ["q"] = ["war & peace"], ["note"] = ["50%"], ["empty"] = [""], ["flag"] = [""] }));
Check(Same(ParseQuery(""), new()));
Check(Same(ParseQuery("a=1&&b=2&"), new() { ["a"] = ["1"], ["b"] = ["2"] }));
Check(Same(ParseQuery("=5&k=v"), new() { ["k"] = ["v"] }));
Check(Same(ParseQuery("expr=a=b"), new() { ["expr"] = ["a=b"] }));
Check(Same(ParseQuery("name=Jos%C3%A9&price=%E2%82%AC5"), new() { ["name"] = ["José"], ["price"] = ["€5"] }));
Check(Same(ParseQuery("bad=%zz%4&tail=%"), new() { ["bad"] = ["%zz%4"], ["tail"] = ["%"] }));
Check(Same(ParseQuery("sort%5Bby%5D=title&path=%2fa%2F"), new() { ["sort[by]"] = ["title"], ["path"] = ["/a/"] }));
Check(Same(ParseQuery("plus=%2B+"), new() { ["plus"] = ["+ "] }));
Console.WriteLine("194 ok");

static Dictionary<string, List<string>> ParseQuery(string query)
{
    var parameters = new Dictionary<string, List<string>>();
    foreach (var piece in query.Split('&'))
    {
        var parts = piece.Split('=', 2); // split at the first = only
        var key = Decode(parts[0]);
        if (key == "") continue;
        var value = parts.Length == 2 ? Decode(parts[1]) : "";
        if (!parameters.TryGetValue(key, out var values))
        {
            values = [];
            parameters[key] = values;
        }
        values.Add(value);
    }
    return parameters;
}

static string Decode(string s)
{
    var bytes = new List<byte>();
    for (var i = 0; i < s.Length; i++)
    {
        var c = s[i];
        if (c == '+') bytes.Add((byte)' ');
        else if (c == '%' && i + 2 < s.Length && HexValue(s[i + 1]) >= 0 && HexValue(s[i + 2]) >= 0)
        {
            bytes.Add((byte)(HexValue(s[i + 1]) * 16 + HexValue(s[i + 2])));
            i += 2;
        }
        else bytes.Add((byte)c); // a malformed % stays as it is
    }
    return Encoding.UTF8.GetString(bytes.ToArray()); // %XX escapes are bytes; several can make one character
}

static int HexValue(char c) => c switch
{
    >= '0' and <= '9' => c - '0',
    >= 'a' and <= 'f' => c - 'a' + 10,
    >= 'A' and <= 'F' => c - 'A' + 10,
    _ => -1,
};

static bool Same(Dictionary<string, List<string>> got, Dictionary<string, List<string>> want) =>
    got.Count == want.Count && want.All(kv => got.TryGetValue(kv.Key, out var v) && v.SequenceEqual(kv.Value));

static void Check(bool ok)
{
    if (!ok) throw new Exception("test failed");
}
