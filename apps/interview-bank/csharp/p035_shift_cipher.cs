// 035 · Shift Cipher
Check(ShiftCipher("Hold seat 14!", 3) == "Krog vhdw 14!");
Check(ShiftCipher("xyz", 2) == "zab");
Check(ShiftCipher("Abc", -1) == "Zab");
Check(ShiftCipher("abc", 52) == "abc");
Check(ShiftCipher("Zz", -27) == "Yy");
Check(ShiftCipher("Krog vhdw 14!", -3) == "Hold seat 14!");
Check(ShiftCipher("", 5) == "");
Console.WriteLine("035 ok");

static string ShiftCipher(string text, int shift)
{
    var k = (shift % 26 + 26) % 26; // % keeps the sign of shift, so -1 must become 25
    var chars = text.ToCharArray();
    for (var i = 0; i < chars.Length; i++)
    {
        var ch = chars[i];
        if (ch >= 'a' && ch <= 'z') chars[i] = (char)('a' + (ch - 'a' + k) % 26);
        else if (ch >= 'A' && ch <= 'Z') chars[i] = (char)('A' + (ch - 'A' + k) % 26);
    }
    return new string(chars);
}

static void Check(bool ok)
{
    if (!ok) throw new Exception("test failed");
}
