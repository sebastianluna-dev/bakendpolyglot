// 146 · Rearrange So No Neighbours Match
using System.Text;

Check(Rearrange("aab") == "aba");
Check(Rearrange("aaab") == "");
Check(Rearrange("vvvlo") == "vlvov");
Check(Rearrange("aabbcc") == "abcabc");
Check(Rearrange("aaabbc") == "ababac");
Check(Rearrange("a") == "a");
Check(Rearrange("zz") == "");
Console.WriteLine("146 ok");

static string Rearrange(string s)
{
    var counts = new int[26];
    foreach (var ch in s) counts[ch - 'a']++;
    // priority (-copies left, letter): most copies first, then the earlier letter
    var heap = new PriorityQueue<char, (int, char)>();
    for (var i = 0; i < 26; i++)
    {
        if (counts[i] > 0) heap.Enqueue((char)('a' + i), (-counts[i], (char)('a' + i)));
    }
    var sb = new StringBuilder();
    var held = ' '; // the letter just placed, kept out of the heap for one step
    var heldLeft = 0;
    while (heap.TryDequeue(out var ch, out var priority))
    {
        sb.Append(ch);
        if (heldLeft > 0) heap.Enqueue(held, (-heldLeft, held));
        held = ch;
        heldLeft = -priority.Item1 - 1;
    }
    return sb.Length == s.Length ? sb.ToString() : "";
}

static void Check(bool ok)
{
    if (!ok) throw new Exception("test failed");
}
