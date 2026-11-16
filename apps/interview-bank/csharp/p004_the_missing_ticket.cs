// 004 · The Missing Ticket
var many = Enumerable.Range(1, 70000).Where(t => t != 31415).ToArray();

Check(MissingTicket([3, 1, 4, 5]) == 2);
Check(MissingTicket([2]) == 1);
Check(MissingTicket([1]) == 2);
Check(MissingTicket([]) == 1);
Check(MissingTicket(many) == 31415);
Console.WriteLine("004 ok");

static int MissingTicket(int[] scanned)
{
    long n = scanned.Length + 1;
    var total = n * (n + 1) / 2; // long: n * (n + 1) overflows int above 46,340 tickets
    foreach (var t in scanned) total -= t;
    return (int)total;
}

static void Check(bool ok)
{
    if (!ok) throw new Exception("test failed");
}
