// 029 · Roman Numerals to Numbers
Check(RomanToNumber("MMXXVI") == 2026);
Check(RomanToNumber("CDXLIV") == 444);
Check(RomanToNumber("I") == 1);
Check(RomanToNumber("IV") == 4);
Check(RomanToNumber("IX") == 9);
Check(RomanToNumber("XIV") == 14);
Check(RomanToNumber("CM") == 900);
Check(RomanToNumber("MMMCMXCIX") == 3999);
Console.WriteLine("029 ok");

static int RomanToNumber(string roman)
{
    var values = new Dictionary<char, int>
    {
        ['I'] = 1, ['V'] = 5, ['X'] = 10, ['L'] = 50, ['C'] = 100, ['D'] = 500, ['M'] = 1000,
    };
    var total = 0;
    for (var i = 0; i < roman.Length; i++)
    {
        var value = values[roman[i]];
        var next = i + 1 < roman.Length ? values[roman[i + 1]] : 0;
        // a smaller symbol before a larger one is subtracted (IV = 5 - 1)
        total += value < next ? -value : value;
    }
    return total;
}

static void Check(bool ok)
{
    if (!ok) throw new Exception("test failed");
}
