// 099 · Search a Sorted Matrix
public class P099SearchASortedMatrix {
    static boolean searchSortedMatrix(int[][] shelves, int target) {
        int rows = shelves.length;
        int cols = rows == 0 ? 0 : shelves[0].length;
        int total = rows * cols;
        int lo = 0, hi = total; // positions 0 .. total - 1, read row by row as one sorted array
        while (lo < hi) {
            int mid = lo + (hi - lo) / 2;
            if (shelves[mid / cols][mid % cols] >= target) hi = mid;
            else lo = mid + 1;
        }
        return lo < total && shelves[lo / cols][lo % cols] == target;
    }

    public static void main(String[] args) {
        int[][] branch = {
            {1, 4, 7, 11},
            {15, 18, 21, 26},
            {30, 34, 40, 52},
        };
        int[][] column = {{1}, {3}, {5}};
        check(searchSortedMatrix(branch, 21));
        check(!searchSortedMatrix(branch, 13));
        check(!searchSortedMatrix(new int[0][], 1));
        check(!searchSortedMatrix(new int[][] {{}}, 1));
        check(searchSortedMatrix(new int[][] {{5}}, 5));
        check(!searchSortedMatrix(new int[][] {{5}}, 6));
        check(!searchSortedMatrix(branch, 60));
        check(!searchSortedMatrix(branch, 0));
        check(searchSortedMatrix(branch, 1));
        check(searchSortedMatrix(branch, 52));
        check(searchSortedMatrix(branch, 26));
        check(searchSortedMatrix(branch, 30));
        check(searchSortedMatrix(column, 3));
        check(!searchSortedMatrix(column, 4));
        System.out.println("099 ok");
    }

    static void check(boolean ok) {
        if (!ok) throw new AssertionError("test failed");
    }
}
