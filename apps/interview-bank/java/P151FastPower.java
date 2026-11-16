// 151 · Fast Power
public class P151FastPower {
    static long powerMod(long x, long n, long m) {
        if (n == 0) return 1 % m;
        long half = powerMod(x, n / 2, m); // one recursive call, used twice
        long square = half * half % m;
        return n % 2 == 0 ? square : square * (x % m) % m;
    }

    public static void main(String[] args) {
        check(powerMod(3, 13, 100) == 23);
        check(powerMod(7, 0, 13) == 1);
        check(powerMod(2, 10, 1000) == 24);
        check(powerMod(5, 3, 1) == 0);
        check(powerMod(0, 0, 7) == 1);
        check(powerMod(0, 5, 7) == 0);
        check(powerMod(999999999, 2, 1000000007) == 64);
        check(powerMod(10, 9, 1000000007) == 1000000000);
        check(powerMod(2, 1000000006, 1000000007) == 1);
        System.out.println("151 ok");
    }

    static void check(boolean ok) {
        if (!ok) throw new AssertionError("test failed");
    }
}
