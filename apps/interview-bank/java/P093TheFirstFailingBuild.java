// 093 · The First Failing Build
import java.util.function.IntPredicate;

public class P093TheFirstFailingBuild {
    static int firstFailingBuild(int n, IntPredicate isBad) {
        int lo = 1, hi = n + 1; // n + 1 stands for "no build fails"
        while (lo < hi) {
            int mid = lo + (hi - lo) / 2;
            if (isBad.test(mid)) hi = mid;
            else lo = mid + 1;
        }
        return lo <= n ? lo : -1;
    }

    static int calls = 0;

    static IntPredicate failingFrom(int first) {
        return build -> {
            calls++;
            return build >= first;
        };
    }

    public static void main(String[] args) {
        check(firstFailingBuild(5, failingFrom(4)) == 4);
        check(firstFailingBuild(3, failingFrom(4)) == -1);
        check(firstFailingBuild(1, failingFrom(1)) == 1);
        check(firstFailingBuild(1, failingFrom(2)) == -1);
        check(firstFailingBuild(5, failingFrom(1)) == 1);
        calls = 0;
        check(firstFailingBuild(1000, failingFrom(777)) == 777);
        check(calls <= 10);
        calls = 0;
        check(firstFailingBuild(2_000_000_000, failingFrom(1_999_999_999)) == 1_999_999_999);
        check(calls <= 31);
        System.out.println("093 ok");
    }

    static void check(boolean ok) {
        if (!ok) throw new AssertionError("test failed");
    }
}
