// 192 · Keep the Latest Version of Each Record
Check(LatestVersions([new(7, 1, "created"), new(3, 2, "in transit"), new(7, 3, "delivered"), new(3, 1, "created"), new(7, 2, "in transit")])
    .SequenceEqual([new Revision(3, 2, "in transit"), new Revision(7, 3, "delivered")]));
Check(LatestVersions([new(5, 4, "first"), new(5, 4, "replayed"), new(1, 1, "only")])
    .SequenceEqual([new Revision(1, 1, "only"), new Revision(5, 4, "first")]));
Check(LatestVersions([]).Length == 0);
Check(LatestVersions([new(42, 9, "x")]).SequenceEqual([new Revision(42, 9, "x")]));
Check(LatestVersions([new(10, 1, "a"), new(2, 1, "b"), new(10, 2, "c"), new(-4, 3, "d"), new(2, 5, "e")])
    .SequenceEqual([new Revision(-4, 3, "d"), new Revision(2, 5, "e"), new Revision(10, 2, "c")]));
Console.WriteLine("192 ok");

static Revision[] LatestVersions(Revision[] revisions)
{
    var latest = new Dictionary<int, Revision>();
    foreach (var r in revisions)
    {
        // strictly greater: the first of equal versions stays
        if (!latest.TryGetValue(r.Id, out var kept) || r.Version > kept.Version) latest[r.Id] = r;
    }
    return latest.Values.OrderBy(r => r.Id).ToArray();
}

static void Check(bool ok)
{
    if (!ok) throw new Exception("test failed");
}

record Revision(int Id, int Version, string Payload);
