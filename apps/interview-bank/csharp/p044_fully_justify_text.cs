// 044 · Fully Justify Text
using System.Text;

Check(JustifyText(["Your", "loan", "of", "Dune", "is", "due", "on", "Friday."], 16)
    .SequenceEqual(["Your   loan   of", "Dune  is  due on", "Friday.         "]));
Check(JustifyText(["Hold", "expires", "in", "ten", "minutes"], 10)
    .SequenceEqual(["Hold      ", "expires in", "ten       ", "minutes   "]));
Check(JustifyText(["to", "be", "or", "not"], 6).SequenceEqual(["to  be", "or not"]));
Check(JustifyText(["exact"], 5).SequenceEqual(["exact"]));
Check(JustifyText([], 8).Count == 0);
Console.WriteLine("044 ok");

static List<string> JustifyText(string[] words, int width)
{
    var lines = new List<string>();
    var i = 0;
    while (i < words.Length)
    {
        var j = i + 1;
        var used = words[i].Length; // the words plus one space between each pair
        while (j < words.Length && used + 1 + words[j].Length <= width)
        {
            used += 1 + words[j].Length;
            j++;
        }
        var gaps = j - i - 1;
        if (j == words.Length || gaps == 0)
        {
            lines.Add(string.Join(" ", words[i..j]).PadRight(width));
        }
        else
        {
            var spaces = width - (used - gaps);
            var each = spaces / gaps;
            var extra = spaces % gaps;
            var line = new StringBuilder();
            for (var k = i; k < j - 1; k++)
            {
                line.Append(words[k]).Append(' ', each + (k - i < extra ? 1 : 0));
            }
            line.Append(words[j - 1]);
            lines.Add(line.ToString());
        }
        i = j;
    }
    return lines;
}

static void Check(bool ok)
{
    if (!ok) throw new Exception("test failed");
}
