// 094 · Integer Square Root
public class P094IntegerSquareRoot {
    static int integerSqrt(int x) {
        // long, because mid * mid and x + 1 overflow int near Integer.MAX_VALUE
        long lo = 0, hi = (long) x + 1; // looking for the first k with k * k > x
        while (lo < hi) {
            long mid = lo + (hi - lo) / 2;
            if (mid * mid > x) hi = mid;
            else lo = mid + 1;
        }
        return (int) (lo - 1);
    }

    public static void main(String[] args) {
        check(integerSqrt(17) == 4);
        check(integerSqrt(36) == 6);
        check(integerSqrt(0) == 0);
        check(integerSqrt(1) == 1);
        check(integerSqrt(2) == 1);
        check(integerSqrt(4) == 2);
        check(integerSqrt(2_147_395_599) == 46_339);
        check(integerSqrt(2_147_395_600) == 46_340);
        check(integerSqrt(Integer.MAX_VALUE) == 46_340);
        System.out.println("094 ok");
    }

    static void check(boolean ok) {
        if (!ok) throw new AssertionError("test failed");
    }
}
