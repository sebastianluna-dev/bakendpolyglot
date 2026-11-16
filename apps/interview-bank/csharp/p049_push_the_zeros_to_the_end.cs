// 049 · Push the Zeros to the End
Check(Pushed([0, 41, 0, 0, 17, 23]).SequenceEqual([41, 17, 23, 0, 0, 0]));
Check(Pushed([5, 0, 3]).SequenceEqual([5, 3, 0]));
Check(Pushed([]).Length == 0);
Check(Pushed([0, 0]).SequenceEqual([0, 0]));
Check(Pushed([1, 2]).SequenceEqual([1, 2]));
Check(Pushed([0, 0, 1]).SequenceEqual([1, 0, 0]));
Console.WriteLine("049 ok");

static void PushZerosToEnd(int[] slots)
{
    var write = 0; // slots[0..write) holds the parcels seen so far, in order
    for (var read = 0; read < slots.Length; read++)
    {
        if (slots[read] != 0)
        {
            (slots[write], slots[read]) = (slots[read], slots[write]);
            write++;
        }
    }
}

static int[] Pushed(int[] slots)
{
    PushZerosToEnd(slots);
    return slots;
}

static void Check(bool ok)
{
    if (!ok) throw new Exception("test failed");
}
