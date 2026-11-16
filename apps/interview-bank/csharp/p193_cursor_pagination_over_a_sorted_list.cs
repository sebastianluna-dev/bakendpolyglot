// 193 · Cursor Pagination Over a Sorted List
int[] ids = [3, 8, 15, 21, 30, 42, 57];
Check(Is(FetchPage(ids, null, 3), [3, 8, 15], 15));
Check(Is(FetchPage(ids, 15, 3), [21, 30, 42], 42));
Check(Is(FetchPage(ids, 42, 3), [57], null));
Check(Is(FetchPage(ids, 20, 3), [21, 30, 42], 42));
Check(Is(FetchPage(ids, 57, 3), [], null));
Check(Is(FetchPage(ids, 1, 10), [3, 8, 15, 21, 30, 42, 57], null));
Check(Is(FetchPage([3, 8, 15, 21, 30, 42], 15, 3), [21, 30, 42], null));
Check(Is(FetchPage([], null, 5), [], null));
Console.WriteLine("193 ok");

static Page FetchPage(int[] ids, int? after, int limit)
{
    var start = 0;
    if (after is int a)
    {
        int lo = 0, hi = ids.Length; // looking for the first id greater than after
        while (lo < hi)
        {
            var mid = lo + (hi - lo) / 2;
            if (ids[mid] > a) hi = mid;
            else lo = mid + 1;
        }
        start = lo;
    }
    var end = Math.Min(start + limit, ids.Length);
    var items = ids[start..end];
    return new Page(items, end < ids.Length ? items[^1] : null);
}

static bool Is(Page page, int[] items, int? next) => page.Items.SequenceEqual(items) && page.Next == next;

static void Check(bool ok)
{
    if (!ok) throw new Exception("test failed");
}

record Page(int[] Items, int? Next);
