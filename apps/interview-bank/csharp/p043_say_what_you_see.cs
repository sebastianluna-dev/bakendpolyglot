// 043 · Say What You See
using System.Text;

Check(SayWhatYouSee("1", 4) == "111221");
Check(SayWhatYouSee("3322251", 1) == "23321511");
Check(SayWhatYouSee("1", 0) == "1");
Check(SayWhatYouSee("22", 5) == "22");
Check(SayWhatYouSee("1111111111", 1) == "101");
Check(SayWhatYouSee("1", 9) == "13211311123113112211");
Console.WriteLine("043 ok");

static string SayWhatYouSee(string start, int rounds)
{
    var s = start;
    for (var r = 0; r < rounds; r++)
    {
        var next = new StringBuilder();
        var i = 0;
        while (i < s.Length)
        {
            var j = i;
            while (j < s.Length && s[j] == s[i]) j++;
            next.Append(j - i).Append(s[i]);
            i = j;
        }
        s = next.ToString();
    }
    return s;
}

static void Check(bool ok)
{
    if (!ok) throw new Exception("test failed");
}
