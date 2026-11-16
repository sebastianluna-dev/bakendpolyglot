// 028 · Longest Shared Prefix
Check(LongestSharedPrefix(["QA76.73", "QA76.76", "QA76.9"]) == "QA76.");
Check(LongestSharedPrefix(["parcel", "pulse", "seat"]) == "");
Check(LongestSharedPrefix([]) == "");
Check(LongestSharedPrefix(["solo"]) == "solo");
Check(LongestSharedPrefix(["ab", "a"]) == "a");
Check(LongestSharedPrefix(["", "x"]) == "");
Check(LongestSharedPrefix(["hold", "hold"]) == "hold");
Console.WriteLine("028 ok");

static string LongestSharedPrefix(string[] words)
{
    if (words.Length == 0) return "";
    var first = words[0];
    for (var i = 0; i < first.Length; i++)
    {
        foreach (var word in words)
        {
            if (i == word.Length || word[i] != first[i]) return first[..i];
        }
    }
    return first;
}

static void Check(bool ok)
{
    if (!ok) throw new Exception("test failed");
}
