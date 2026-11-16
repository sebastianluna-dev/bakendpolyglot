// 042 · Multiply Numbers Written as Strings
Check(MultiplyStrings("123", "45") == "5535");
Check(MultiplyStrings("0", "9081") == "0");
Check(MultiplyStrings("999", "999") == "998001");
Check(MultiplyStrings("7", "8") == "56");
Check(MultiplyStrings("1", "1") == "1");
Check(MultiplyStrings("123456789", "987654321") == "121932631112635269");
Check(MultiplyStrings("99999999999999999999", "99999999999999999999")
    == "9999999999999999999800000000000000000001");
Console.WriteLine("042 ok");

static string MultiplyStrings(string a, string b)
{
    var digits = new int[a.Length + b.Length];
    for (var i = a.Length - 1; i >= 0; i--)
    {
        for (var j = b.Length - 1; j >= 0; j--)
        {
            var sum = (a[i] - '0') * (b[j] - '0') + digits[i + j + 1];
            digits[i + j + 1] = sum % 10;
            digits[i + j] += sum / 10; // may pass 9 for now; fixed when its own turn comes
        }
    }
    var start = 0;
    while (start < digits.Length - 1 && digits[start] == 0) start++;
    return string.Concat(digits[start..]);
}

static void Check(bool ok)
{
    if (!ok) throw new Exception("test failed");
}
