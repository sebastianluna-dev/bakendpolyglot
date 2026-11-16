// 190 · Hand Out Candy Fairly
import java.util.Arrays;

public class P190HandOutCandyFairly {
    static int minCandy(int[] scores) {
        int n = scores.length;
        int[] candy = new int[n];
        Arrays.fill(candy, 1);
        for (int i = 1; i < n; i++) {
            if (scores[i] > scores[i - 1]) candy[i] = candy[i - 1] + 1;
        }
        for (int i = n - 2; i >= 0; i--) {
            // max keeps what the first pass gave for the left neighbour
            if (scores[i] > scores[i + 1]) candy[i] = Math.max(candy[i], candy[i + 1] + 1);
        }
        return Arrays.stream(candy).sum();
    }

    public static void main(String[] args) {
        check(minCandy(new int[] {1, 3, 2, 2, 1}) == 7);
        check(minCandy(new int[] {5, 4, 3, 4}) == 8);
        check(minCandy(new int[0]) == 0);
        check(minCandy(new int[] {7}) == 1);
        check(minCandy(new int[] {2, 2, 2}) == 3);
        check(minCandy(new int[] {1, 2, 3, 4}) == 10);
        check(minCandy(new int[] {1, 2, 3, 4, 1}) == 11);
        check(minCandy(new int[] {4, 3, 2, 1}) == 10);
        System.out.println("190 ok");
    }

    static void check(boolean ok) {
        if (!ok) throw new AssertionError("test failed");
    }
}
