// 031 · Add Two Binary Strings
Check(AddBinary("1011", "111") == "10010");
Check(AddBinary("0", "0") == "0");
Check(AddBinary("1", "1") == "10");
Check(AddBinary("1111", "1") == "10000");
Check(AddBinary("100", "110010") == "110110");
Check(AddBinary(new string('1', 70), "1") == "1" + new string('0', 70));
Console.WriteLine("031 ok");

static string AddBinary(string a, string b)
{
    var digits = new List<char>();
    int i = a.Length - 1, j = b.Length - 1, carry = 0;
    while (i >= 0 || j >= 0 || carry > 0)
    {
        var total = carry;
        if (i >= 0) total += a[i--] - '0';
        if (j >= 0) total += b[j--] - '0';
        digits.Add((char)('0' + total % 2));
        carry = total / 2;
    }
    digits.Reverse();
    return new string(digits.ToArray());
}

static void Check(bool ok)
{
    if (!ok) throw new Exception("test failed");
}
