// 027 · First Letter That Appears Once
Check(FirstUniqueIndex("shelfwise") == 1);
Check(FirstUniqueIndex("noon") == -1);
Check(FirstUniqueIndex("") == -1);
Check(FirstUniqueIndex("z") == 0);
Check(FirstUniqueIndex("aabbc") == 4);
Check(FirstUniqueIndex("pear") == 0);
Console.WriteLine("027 ok");

static int FirstUniqueIndex(string word)
{
    var counts = new int[26];
    foreach (var ch in word) counts[ch - 'a']++;
    for (var i = 0; i < word.Length; i++)
    {
        if (counts[word[i] - 'a'] == 1) return i;
    }
    return -1;
}

static void Check(bool ok)
{
    if (!ok) throw new Exception("test failed");
}
