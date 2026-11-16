// 069 · Ledger With Undo and Double
Check(LedgerTotal(["5", "-2", "D", "+", "U", "10", "+"]) == 15);
Check(LedgerTotal(["7", "U"]) == 0);
Check(LedgerTotal([]) == 0);
Check(LedgerTotal(["100"]) == 100);
Check(LedgerTotal(["2", "3", "+"]) == 10);
Check(LedgerTotal(["3", "D", "D", "U", "+"]) == 18);
Check(LedgerTotal(["-4", "D", "U", "U"]) == 0);
Console.WriteLine("069 ok");

static int LedgerTotal(string[] commands)
{
    var amounts = new List<int>();
    foreach (var cmd in commands)
    {
        switch (cmd)
        {
            case "U": amounts.RemoveAt(amounts.Count - 1); break;
            case "D": amounts.Add(2 * amounts[^1]); break;
            case "+": amounts.Add(amounts[^1] + amounts[^2]); break;
            default: amounts.Add(int.Parse(cmd)); break;
        }
    }
    return amounts.Sum();
}

static void Check(bool ok)
{
    if (!ok) throw new Exception("test failed");
}
