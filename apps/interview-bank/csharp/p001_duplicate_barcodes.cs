// 001 · Duplicate Barcodes
Check(HasDuplicate(["SW-1042", "SW-0007", "SW-2210", "SW-0007"]));
Check(!HasDuplicate(["SW-1042", "SW-0007", "SW-2210"]));
Check(!HasDuplicate(["SW-0007", "sw-0007"]));
Check(!HasDuplicate(["SW-1042"]));
Check(!HasDuplicate([]));
Console.WriteLine("001 ok");

static bool HasDuplicate(string[] barcodes)
{
    var seen = new HashSet<string>();
    foreach (var code in barcodes)
    {
        if (!seen.Add(code)) return true; // Add returns false when the code is already there
    }
    return false;
}

static void Check(bool ok)
{
    if (!ok) throw new Exception("test failed");
}
