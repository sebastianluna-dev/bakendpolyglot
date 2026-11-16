// 062 · Stretches With a Small Product
public class P062StretchesWithASmallProduct {
    static int countSmallProducts(int[] factors, int limit) {
        if (limit <= 1) return 0; // every product is at least 1
        int left = 0, product = 1, count = 0; // product of factors[left..right]
        for (int right = 0; right < factors.length; right++) {
            product *= factors[right];
            while (product >= limit) {
                product /= factors[left];
                left++;
            }
            count += right - left + 1; // every stretch that ends at right and starts at left or later
        }
        return count;
    }

    public static void main(String[] args) {
        check(countSmallProducts(new int[] {4, 2, 6, 1, 3}, 15) == 10);
        check(countSmallProducts(new int[] {1, 1, 1}, 1) == 0);
        check(countSmallProducts(new int[] {1, 1, 1}, 2) == 6);
        check(countSmallProducts(new int[0], 10) == 0);
        check(countSmallProducts(new int[] {7}, 0) == 0);
        check(countSmallProducts(new int[] {10, 5, 2}, 100) == 5);
        check(countSmallProducts(new int[] {1000, 1000}, 1000000) == 2);
        System.out.println("062 ok");
    }

    static void check(boolean ok) {
        if (!ok) throw new AssertionError("test failed");
    }
}
