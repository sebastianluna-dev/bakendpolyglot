// 006 · The Unpaired Seat
Check(UnpairedSeat([14, 3, 14, 8, 3]) == 8);
Check(UnpairedSeat([21]) == 21);
Check(UnpairedSeat([4, 4, 9]) == 9);
Check(UnpairedSeat([7, 2, 2]) == 7);
Check(UnpairedSeat([1000000000, 5, 5]) == 1000000000);
Console.WriteLine("006 ok");

static int UnpairedSeat(int[] log)
{
    var result = 0;
    foreach (var seat in log) result ^= seat; // pairs cancel: x ^ x is 0
    return result;
}

static void Check(bool ok)
{
    if (!ok) throw new Exception("test failed");
}
