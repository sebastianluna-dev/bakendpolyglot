// 152 · Every Upper- and Lower-Case Spelling
Check(AllSpellings("b7x").SequenceEqual(["B7X", "B7x", "b7X", "b7x"]));
Check(AllSpellings("42").SequenceEqual(["42"]));
Check(AllSpellings("Q").SequenceEqual(["Q", "q"]));
Check(AllSpellings("aB1").SequenceEqual(["AB1", "Ab1", "aB1", "ab1"]));
Check(AllSpellings("0z0").SequenceEqual(["0Z0", "0z0"]));
Check(AllSpellings("abcdefghij").Count == 1024);
Console.WriteLine("152 ok");

static List<string> AllSpellings(string code)
{
    var chars = code.ToCharArray();
    var output = new List<string>();
    void Spell(int i)
    {
        if (i == chars.Length)
        {
            output.Add(new string(chars));
            return;
        }
        var c = code[i];
        if (!char.IsLetter(c)) // a digit: one choice
        {
            Spell(i + 1);
            return;
        }
        chars[i] = char.ToUpperInvariant(c); // upper case first keeps the output sorted
        Spell(i + 1);
        chars[i] = char.ToLowerInvariant(c);
        Spell(i + 1);
    }
    Spell(0);
    return output;
}

static void Check(bool ok)
{
    if (!ok) throw new Exception("test failed");
}
