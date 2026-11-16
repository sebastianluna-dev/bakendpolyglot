// 041 · Compare Version Strings
Check(CompareVersions("1.2.10", "1.2.9") == 1);
Check(CompareVersions("3.0.0", "3") == 0);
Check(CompareVersions("1.01", "1.001") == 0);
Check(CompareVersions("0.9", "1.0") == -1);
Check(CompareVersions("7.5.2.4", "7.5.3") == -1);
Check(CompareVersions("1.0.1", "1") == 1);
Check(CompareVersions("10.4", "9.12") == 1);
Console.WriteLine("041 ok");

static int CompareVersions(string a, string b)
{
    int i = 0, j = 0;
    while (i < a.Length || j < b.Length)
    {
        var x = 0;
        while (i < a.Length && a[i] != '.')
        {
            x = x * 10 + (a[i] - '0');
            i++;
        }
        var y = 0;
        while (j < b.Length && b[j] != '.')
        {
            y = y * 10 + (b[j] - '0');
            j++;
        }
        if (x != y) return x < y ? -1 : 1;
        i++; // step over the dot; past the end, the next part reads as 0
        j++;
    }
    return 0;
}

static void Check(bool ok)
{
    if (!ok) throw new Exception("test failed");
}
