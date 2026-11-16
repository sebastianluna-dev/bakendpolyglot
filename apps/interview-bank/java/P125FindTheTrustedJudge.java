// 125 · Find the Trusted Judge
public class P125FindTheTrustedJudge {
    static int findJudge(int n, int[][] trust) {
        int[] score = new int[n + 1]; // trusted by, minus trusts
        for (int[] t : trust) {
            score[t[0]]--;
            score[t[1]]++;
        }
        for (int person = 1; person <= n; person++) {
            if (score[person] == n - 1) return person;
        }
        return -1;
    }

    public static void main(String[] args) {
        check(findJudge(4, new int[][] {{1, 3}, {2, 3}, {4, 3}, {4, 1}}) == 3);
        check(findJudge(3, new int[][] {{1, 3}, {2, 3}, {3, 1}}) == -1);
        check(findJudge(1, new int[0][]) == 1);
        check(findJudge(2, new int[0][]) == -1);
        check(findJudge(2, new int[][] {{1, 2}, {2, 1}}) == -1);
        check(findJudge(3, new int[][] {{1, 2}, {2, 3}}) == -1);
        System.out.println("125 ok");
    }

    static void check(boolean ok) {
        if (!ok) throw new AssertionError("test failed");
    }
}
