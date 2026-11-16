// 173 · Ways to Decode a Digit String
Check(CountDecodings("2612") == 4);
Check(CountDecodings("301") == 0);
Check(CountDecodings("1201") == 1);
Check(CountDecodings("106") == 1);
Check(CountDecodings("0") == 0);
Check(CountDecodings("100") == 0);
Check(CountDecodings("27") == 1);
Check(CountDecodings("11106") == 2);
Check(CountDecodings(new string('1', 45)) == 1836311903);
Console.WriteLine("173 ok");

static int CountDecodings(string digits)
{
    var twoBack = 0; // decodings of the first i - 2 digits
    var oneBack = 1; // decodings of the first i - 1 digits; the empty prefix has one
    for (var i = 1; i <= digits.Length; i++)
    {
        var ways = digits[i - 1] != '0' ? oneBack : 0;
        if (i >= 2)
        {
            var two = int.Parse(digits[(i - 2)..i]);
            if (two >= 10 && two <= 26) ways += twoBack; // at least 10: "06" is not a letter
        }
        twoBack = oneBack;
        oneBack = ways;
    }
    return oneBack;
}

static void Check(bool ok)
{
    if (!ok) throw new Exception("test failed");
}
