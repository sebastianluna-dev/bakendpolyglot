// 184 · Seat Everyone With the Fewest Moves
Check(FewestMoves([4, 1, 7], [2, 8, 3]) == 3);
Check(FewestMoves([2, 6, 9, 10], [5, 5, 1, 12]) == 8);
Check(FewestMoves([], []) == 0);
Check(FewestMoves([3, 5], [5, 3]) == 0);
Check(FewestMoves([10], [1]) == 9);
Console.WriteLine("184 ok");

static int FewestMoves(int[] seats, int[] guests)
{
    var s = seats.Order().ToArray();
    var g = guests.Order().ToArray();
    var moves = 0;
    for (var i = 0; i < s.Length; i++) moves += Math.Abs(s[i] - g[i]);
    return moves;
}

static void Check(bool ok)
{
    if (!ok) throw new Exception("test failed");
}
