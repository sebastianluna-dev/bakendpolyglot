// 038 · Parse an Integer Strictly
Check(ParseInteger("42") == 42);
Check(ParseInteger("-17") == -17);
Check(ParseInteger("+8") == 8);
Check(ParseInteger("0042") == 42);
Check(ParseInteger("-0") == 0);
Check(ParseInteger("2147483647") == int.MaxValue);
Check(ParseInteger("-2147483648") == int.MinValue);
Check(ParseInteger("2147483648") == null);
Check(ParseInteger("-2147483649") == null);
Check(ParseInteger("99999999999999999999") == null);
Check(ParseInteger("") == null);
Check(ParseInteger("+") == null);
Check(ParseInteger(" 42") == null);
Check(ParseInteger("42 ") == null);
Check(ParseInteger("12a") == null);
Check(ParseInteger("+-3") == null);
Check(ParseInteger("3.0") == null);
Console.WriteLine("038 ok");

static int? ParseInteger(string s)
{
    var i = 0;
    var negative = false;
    if (s.Length > 0 && (s[0] == '+' || s[0] == '-'))
    {
        negative = s[0] == '-';
        i = 1;
    }
    if (i == s.Length) return null; // empty, or a sign with no digits
    long limit = negative ? 2147483648L : int.MaxValue;
    long value = 0;
    for (; i < s.Length; i++)
    {
        var ch = s[i];
        if (ch < '0' || ch > '9') return null;
        value = value * 10 + (ch - '0');
        if (value > limit) return null;
    }
    return (int)(negative ? -value : value);
}

static void Check(bool ok)
{
    if (!ok) throw new Exception("test failed");
}
