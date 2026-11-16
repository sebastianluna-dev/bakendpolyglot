// 166 · Count the One Bits for Every Number
Check(OneBitCounts(5).SequenceEqual([0, 1, 1, 2, 1, 2]));
Check(OneBitCounts(0).SequenceEqual([0]));
Check(OneBitCounts(1).SequenceEqual([0, 1]));
Check(OneBitCounts(8).SequenceEqual([0, 1, 1, 2, 1, 2, 2, 3, 1]));
Check(OneBitCounts(1023)[1023] == 10);
Check(OneBitCounts(100000)[65535] == 16);
Console.WriteLine("166 ok");

static int[] OneBitCounts(int n)
{
    var bits = new int[n + 1];
    for (var i = 1; i <= n; i++)
    {
        bits[i] = bits[i >> 1] + (i & 1); // i without its last bit, plus that bit
    }
    return bits;
}

static void Check(bool ok)
{
    if (!ok) throw new Exception("test failed");
}
