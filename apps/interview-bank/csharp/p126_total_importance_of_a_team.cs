// 126 · Total Importance of a Team
Employee[] staff = [new(1, 5, [2, 3]), new(2, 3, [4]), new(3, 3, []), new(4, 1, [])];
Check(TotalImportance(staff, 1) == 12);
Check(TotalImportance(staff, 2) == 4);
Check(TotalImportance(staff, 4) == 1);
Check(TotalImportance([new(7, -2, [])], 7) == -2);
Employee[] shuffled = [new(3, 4, []), new(9, 1, [3, 5]), new(5, 6, []), new(8, 10, [9])];
Check(TotalImportance(shuffled, 9) == 11);
Check(TotalImportance(shuffled, 8) == 21);
Console.WriteLine("126 ok");

static int TotalImportance(Employee[] employees, int employeeId)
{
    var byId = employees.ToDictionary(e => e.Id);
    int Team(int id)
    {
        var e = byId[id];
        var total = e.Importance;
        foreach (var r in e.Reports) total += Team(r);
        return total;
    }
    return Team(employeeId);
}

static void Check(bool ok)
{
    if (!ok) throw new Exception("test failed");
}

record Employee(int Id, int Importance, int[] Reports);
