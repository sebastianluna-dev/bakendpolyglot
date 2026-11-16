// 078 · Remove k Digits for the Smallest Number
using System.Text;

Check(RemoveKDigits("4172", 2) == "12");
Check(RemoveKDigits("30200", 1) == "200");
Check(RemoveKDigits("9", 1) == "0");
Check(RemoveKDigits("12345", 2) == "123");
Check(RemoveKDigits("10", 1) == "0");
Check(RemoveKDigits("1432219", 0) == "1432219");
Check(RemoveKDigits("112", 1) == "11");
Check(RemoveKDigits("100200", 1) == "200");
Console.WriteLine("078 ok");

static string RemoveKDigits(string num, int k)
{
    var kept = new StringBuilder(); // used as a stack of digits
    foreach (var d in num)
    {
        while (k > 0 && kept.Length > 0 && kept[^1] > d)
        {
            kept.Length--;
            k--;
        }
        kept.Append(d);
    }
    kept.Length -= k; // digits never fell, so drop the rest from the end
    var start = 0;
    while (start < kept.Length - 1 && kept[start] == '0') start++;
    return kept.Length == 0 ? "0" : kept.ToString(start, kept.Length - start);
}

static void Check(bool ok)
{
    if (!ok) throw new Exception("test failed");
}
