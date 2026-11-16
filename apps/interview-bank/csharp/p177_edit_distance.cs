// 177 · Edit Distance
Check(EditDistance("shelf", "shelves") == 3);
Check(EditDistance("loan", "lone") == 2);
Check(EditDistance("", "abc") == 3);
Check(EditDistance("abc", "") == 3);
Check(EditDistance("seat", "seat") == 0);
Check(EditDistance("parcel", "pencil") == 3);
Check(EditDistance("trucks", "truck") == 1);
Check(EditDistance("abc", "cab") == 2);
Check(EditDistance("booking", "looking") == 1);
Console.WriteLine("177 ok");

static int EditDistance(string source, string target)
{
    var m = target.Length;
    // cur[j]: fewest edits turning the first i letters of source into the first j of target; prev holds row i - 1
    var prev = new int[m + 1];
    for (var j = 0; j <= m; j++) prev[j] = j;
    for (var i = 1; i <= source.Length; i++)
    {
        var cur = new int[m + 1];
        cur[0] = i;
        for (var j = 1; j <= m; j++)
        {
            if (source[i - 1] == target[j - 1]) cur[j] = prev[j - 1];
            else cur[j] = 1 + Math.Min(prev[j], Math.Min(cur[j - 1], prev[j - 1])); // delete, insert, replace
        }
        prev = cur;
    }
    return prev[m];
}

static void Check(bool ok)
{
    if (!ok) throw new Exception("test failed");
}
