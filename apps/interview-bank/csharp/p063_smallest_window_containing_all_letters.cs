// 063 · Smallest Window Containing All Letters
Check(SmallestWindow("deliverytruck", "tire") == "iveryt");
Check(SmallestWindow("banana", "nn") == "nan");
Check(SmallestWindow("abba", "ab") == "ab");
Check(SmallestWindow("abc", "abd") == "");
Check(SmallestWindow("a", "aa") == "");
Check(SmallestWindow("Aa", "a") == "a");
Check(SmallestWindow("", "a") == "");
Check(SmallestWindow("parcel", "parcel") == "parcel");
Console.WriteLine("063 ok");

static string SmallestWindow(string text, string letters)
{
    var need = new int[128]; // copies still wanted, by character code
    foreach (var c in letters) need[c]++;
    int missing = letters.Length, bestStart = 0, bestLength = text.Length + 1, left = 0;
    for (var right = 0; right < text.Length; right++)
    {
        var c = text[right];
        if (need[c] > 0) missing--;
        need[c]--;
        while (missing == 0)
        {
            if (right - left + 1 < bestLength)
            {
                bestStart = left;
                bestLength = right - left + 1;
            }
            var d = text[left];
            need[d]++;
            if (need[d] > 0) missing++; // a wanted copy just left the window
            left++;
        }
    }
    return bestLength > text.Length ? "" : text.Substring(bestStart, bestLength);
}

static void Check(bool ok)
{
    if (!ok) throw new Exception("test failed");
}
