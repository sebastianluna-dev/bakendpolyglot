// 143 · Medal Ranks
Check(MedalRanks([88, 95, 70, 91]).SequenceEqual(["Bronze Medal", "Gold Medal", "4", "Silver Medal"]));
Check(MedalRanks([3, 9, 1, 12, 5, 7]).SequenceEqual(["5", "Silver Medal", "6", "Gold Medal", "4", "Bronze Medal"]));
Check(MedalRanks([42]).SequenceEqual(["Gold Medal"]));
Check(MedalRanks([0, 5]).SequenceEqual(["Silver Medal", "Gold Medal"]));
Check(MedalRanks([]).Length == 0);
Check(MedalRanks([10, 20, 30, 40, 50, 60, 70, 80, 90, 100, 110])
    .SequenceEqual(["11", "10", "9", "8", "7", "6", "5", "4", "Bronze Medal", "Silver Medal", "Gold Medal"]));
Console.WriteLine("143 ok");

static string[] MedalRanks(int[] scores)
{
    // element: index, priority: score, highest score first
    var heap = new PriorityQueue<int, int>(Comparer<int>.Create((a, b) => b.CompareTo(a)));
    for (var i = 0; i < scores.Length; i++) heap.Enqueue(i, scores[i]);
    string[] medals = ["Gold Medal", "Silver Medal", "Bronze Medal"];
    var ranks = new string[scores.Length];
    for (var place = 1; heap.TryDequeue(out var i, out _); place++)
        ranks[i] = place <= 3 ? medals[place - 1] : place.ToString();
    return ranks;
}

static void Check(bool ok)
{
    if (!ok) throw new Exception("test failed");
}
