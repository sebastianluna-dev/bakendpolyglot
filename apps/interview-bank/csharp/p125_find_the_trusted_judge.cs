// 125 · Find the Trusted Judge
Check(FindJudge(4, [[1, 3], [2, 3], [4, 3], [4, 1]]) == 3);
Check(FindJudge(3, [[1, 3], [2, 3], [3, 1]]) == -1);
Check(FindJudge(1, []) == 1);
Check(FindJudge(2, []) == -1);
Check(FindJudge(2, [[1, 2], [2, 1]]) == -1);
Check(FindJudge(3, [[1, 2], [2, 3]]) == -1);
Console.WriteLine("125 ok");

static int FindJudge(int n, int[][] trust)
{
    var score = new int[n + 1]; // trusted by, minus trusts
    foreach (var t in trust)
    {
        score[t[0]]--;
        score[t[1]]++;
    }
    for (var person = 1; person <= n; person++)
    {
        if (score[person] == n - 1) return person;
    }
    return -1;
}

static void Check(bool ok)
{
    if (!ok) throw new Exception("test failed");
}
