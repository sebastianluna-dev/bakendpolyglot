// 040 · Pack and Unpack a List of Strings
using System.Text;

(List<string> Words, string Packed)[] cases =
[
    (["truck 7", "", "door#2"], "7#truck 70#6#door#2"),
    (["parcel-12345"], "12#parcel-12345"),
    ([], ""),
    ([""], "0#"),
    (["#", "12#", "a b", "3#x"], "1##3#12#3#a b3#3#x"),
];
foreach (var (words, packed) in cases)
{
    Check(Pack(words) == packed);
    Check(Unpack(packed).SequenceEqual(words));
}
Console.WriteLine("040 ok");

static string Pack(List<string> words)
{
    var result = new StringBuilder();
    foreach (var w in words) result.Append(w.Length).Append('#').Append(w);
    return result.ToString();
}

static List<string> Unpack(string packed)
{
    var words = new List<string>();
    var i = 0;
    while (i < packed.Length)
    {
        var length = 0;
        while (packed[i] != '#')
        {
            length = length * 10 + (packed[i] - '0');
            i++;
        }
        i++; // skip the '#'
        words.Add(packed.Substring(i, length));
        i += length;
    }
    return words;
}

static void Check(bool ok)
{
    if (!ok) throw new Exception("test failed");
}
