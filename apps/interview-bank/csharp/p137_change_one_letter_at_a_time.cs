// 137 · Change One Letter at a Time
Check(ShortestChain("cold", "warm", ["cord", "card", "ward", "warm", "worm", "word", "wore"]) == 5);
Check(ShortestChain("lead", "gold", ["load", "goad"]) == 0);
Check(ShortestChain("cat", "cot", ["cot"]) == 2);
Check(ShortestChain("cat", "dog", []) == 0);
Check(ShortestChain("hit", "hot", ["hot", "hot", "hit"]) == 2);
Check(ShortestChain("mine", "bond", ["mind", "bind", "bond", "mend"]) == 4);
Check(ShortestChain("a", "c", ["b", "c"]) == 2);
Console.WriteLine("137 ok");

static int ShortestChain(string start, string target, string[] words)
{
    var dictionary = new HashSet<string>(words);
    var length = new Dictionary<string, int> { [start] = 1 }; // words in the shortest chain that ends here
    var queue = new Queue<string>();
    queue.Enqueue(start);
    while (queue.TryDequeue(out var word))
    {
        if (word == target) return length[word];
        var chars = word.ToCharArray();
        for (var i = 0; i < chars.Length; i++)
        {
            var original = chars[i];
            for (var c = 'a'; c <= 'z'; c++)
            {
                chars[i] = c;
                var next = new string(chars);
                if (dictionary.Contains(next) && length.TryAdd(next, length[word] + 1)) queue.Enqueue(next);
            }
            chars[i] = original;
        }
    }
    return 0;
}

static void Check(bool ok)
{
    if (!ok) throw new Exception("test failed");
}
