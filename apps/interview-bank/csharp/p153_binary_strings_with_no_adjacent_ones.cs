// 153 · Binary Strings With No Adjacent Ones
Check(NoAdjacentOnes(3).SequenceEqual(["000", "001", "010", "100", "101"]));
Check(NoAdjacentOnes(1).SequenceEqual(["0", "1"]));
Check(NoAdjacentOnes(0).SequenceEqual([""]));
Check(NoAdjacentOnes(2).SequenceEqual(["00", "01", "10"]));
Check(NoAdjacentOnes(4).SequenceEqual(["0000", "0001", "0010", "0100", "0101", "1000", "1001", "1010"]));
Check(NoAdjacentOnes(10).Count == 144);
Console.WriteLine("153 ok");

static List<string> NoAdjacentOnes(int n)
{
    var result = new List<string>();
    var path = new System.Text.StringBuilder();
    void Walk()
    {
        if (path.Length == n)
        {
            result.Add(path.ToString());
            return;
        }
        path.Append('0');
        Walk();
        path.Length--;
        if (path.Length == 0 || path[^1] == '0')
        {
            path.Append('1');
            Walk();
            path.Length--;
        }
    }
    Walk();
    return result;
}

static void Check(bool ok)
{
    if (!ok) throw new Exception("test failed");
}
