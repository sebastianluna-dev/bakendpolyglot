// 099 · Search a Sorted Matrix
int[][] branch =
[
    [1, 4, 7, 11],
    [15, 18, 21, 26],
    [30, 34, 40, 52],
];
Check(SearchSortedMatrix(branch, 21));
Check(!SearchSortedMatrix(branch, 13));
Check(!SearchSortedMatrix([], 1));
Check(!SearchSortedMatrix([[]], 1));
Check(SearchSortedMatrix([[5]], 5));
Check(!SearchSortedMatrix([[5]], 6));
Check(!SearchSortedMatrix(branch, 60));
Check(!SearchSortedMatrix(branch, 0));
Check(SearchSortedMatrix(branch, 1));
Check(SearchSortedMatrix(branch, 52));
Check(SearchSortedMatrix(branch, 26));
Check(SearchSortedMatrix(branch, 30));
Check(SearchSortedMatrix([[1], [3], [5]], 3));
Check(!SearchSortedMatrix([[1], [3], [5]], 4));
Console.WriteLine("099 ok");

static bool SearchSortedMatrix(int[][] shelves, int target)
{
    var rows = shelves.Length;
    var cols = rows == 0 ? 0 : shelves[0].Length;
    var total = rows * cols;
    int lo = 0, hi = total; // positions 0 .. total - 1, read row by row as one sorted array
    while (lo < hi)
    {
        var mid = lo + (hi - lo) / 2;
        if (shelves[mid / cols][mid % cols] >= target) hi = mid;
        else lo = mid + 1;
    }
    return lo < total && shelves[lo / cols][lo % cols] == target;
}

static void Check(bool ok)
{
    if (!ok) throw new Exception("test failed");
}
