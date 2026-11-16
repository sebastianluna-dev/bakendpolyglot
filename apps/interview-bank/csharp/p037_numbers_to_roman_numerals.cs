// 037 · Numbers to Roman Numerals
using System.Text;

Check(ToRoman(2026) == "MMXXVI");
Check(ToRoman(1994) == "MCMXCIV");
Check(ToRoman(1) == "I");
Check(ToRoman(4) == "IV");
Check(ToRoman(49) == "XLIX");
Check(ToRoman(58) == "LVIII");
Check(ToRoman(444) == "CDXLIV");
Check(ToRoman(3999) == "MMMCMXCIX");
Console.WriteLine("037 ok");

static string ToRoman(int n)
{
    int[] values = [1000, 900, 500, 400, 100, 90, 50, 40, 10, 9, 5, 4, 1];
    string[] symbols = ["M", "CM", "D", "CD", "C", "XC", "L", "XL", "X", "IX", "V", "IV", "I"];
    var result = new StringBuilder();
    for (var i = 0; i < values.Length; i++)
    {
        while (n >= values[i])
        {
            result.Append(symbols[i]);
            n -= values[i];
        }
    }
    return result.ToString();
}

static void Check(bool ok)
{
    if (!ok) throw new Exception("test failed");
}
