// 147 · Single-Worker Job Order
Check(JobOrder([[1, 3], [2, 1], [3, 2], [4, 1]]).SequenceEqual([0, 1, 3, 2]));
Check(JobOrder([[5, 2], [5, 2], [5, 1], [20, 1]]).SequenceEqual([2, 0, 1, 3]));
Check(JobOrder([[7, 4]]).SequenceEqual([0]));
Check(JobOrder([[1, 2], [3, 5], [3, 1]]).SequenceEqual([0, 2, 1]));
Check(JobOrder([[9, 1], [1, 1]]).SequenceEqual([1, 0]));
Check(JobOrder([[1, 1000000000], [1, 1000000000], [1000000000, 1]]).SequenceEqual([0, 2, 1]));
Console.WriteLine("147 ok");

static int[] JobOrder(int[][] jobs)
{
    var byArrival = Enumerable.Range(0, jobs.Length).OrderBy(i => jobs[i][0]).ToArray();
    var ready = new PriorityQueue<int, (int Duration, int Index)>(); // shortest first, then smaller index
    var order = new List<int>();
    long time = 0; // can pass int.MaxValue
    var next = 0; // position in byArrival of the first job not yet queued
    while (order.Count < jobs.Length)
    {
        if (ready.Count == 0 && time < jobs[byArrival[next]][0]) time = jobs[byArrival[next]][0]; // idle
        while (next < jobs.Length && jobs[byArrival[next]][0] <= time)
        {
            var i = byArrival[next++];
            ready.Enqueue(i, (jobs[i][1], i));
        }
        var j = ready.Dequeue();
        time += jobs[j][1];
        order.Add(j);
    }
    return [.. order];
}

static void Check(bool ok)
{
    if (!ok) throw new Exception("test failed");
}
