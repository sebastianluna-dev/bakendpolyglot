// 076 · Colliding Parcels on a Conveyor
Check(SurvivingParcels([4, 7, -5]).SequenceEqual([4, 7]));
Check(SurvivingParcels([3, 6, -6, -8, 2]).SequenceEqual([-8, 2]));
Check(SurvivingParcels([]).Length == 0);
Check(SurvivingParcels([-3, 4]).SequenceEqual([-3, 4]));
Check(SurvivingParcels([5, -5]).Length == 0);
Check(SurvivingParcels([1, -2, -2, -2]).SequenceEqual([-2, -2, -2]));
Check(SurvivingParcels([10, 2, -5]).SequenceEqual([10]));
Console.WriteLine("076 ok");

static int[] SurvivingParcels(int[] parcels)
{
    var track = new List<int>(); // used as a stack; a List keeps left-to-right order for the result
    foreach (var p in parcels)
    {
        var alive = true;
        while (alive && p < 0 && track.Count > 0 && track[^1] > 0)
        {
            var top = track[^1];
            if (top < -p)
            {
                track.RemoveAt(track.Count - 1);
            }
            else if (top == -p)
            {
                track.RemoveAt(track.Count - 1);
                alive = false;
            }
            else
            {
                alive = false;
            }
        }
        if (alive) track.Add(p);
    }
    return [.. track];
}

static void Check(bool ok)
{
    if (!ok) throw new Exception("test failed");
}
