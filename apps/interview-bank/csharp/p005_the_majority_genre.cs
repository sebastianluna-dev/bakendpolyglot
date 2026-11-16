// 005 · The Majority Genre
Check(MajorityGenre(["mystery", "poetry", "mystery", "history", "mystery"]) == "mystery");
Check(MajorityGenre(["poetry", "history", "poetry", "history"]) == "");
Check(MajorityGenre(["drama", "comedy", "drama"]) == "drama");
Check(MajorityGenre(["sci-fi"]) == "sci-fi");
Check(MajorityGenre([]) == "");
Console.WriteLine("005 ok");

static string MajorityGenre(string[] loans)
{
    var counts = new Dictionary<string, int>();
    foreach (var genre in loans)
    {
        var c = counts.GetValueOrDefault(genre) + 1;
        counts[genre] = c;
        if (c * 2 > loans.Length) return genre;
    }
    return "";
}

static void Check(bool ok)
{
    if (!ok) throw new Exception("test failed");
}
