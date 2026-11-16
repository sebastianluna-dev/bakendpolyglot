// 156 · Combinations That Sum to a Target
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class P156CombinationsThatSumToATarget {
    static List<List<Integer>> combinationsToTarget(int[] candidates, int target) {
        int[] sizes = candidates.clone();
        Arrays.sort(sizes);
        List<List<Integer>> result = new ArrayList<>();
        walk(sizes, 0, target, new ArrayList<>(), result);
        return result;
    }

    static void walk(int[] sizes, int start, int remaining, List<Integer> path, List<List<Integer>> result) {
        if (remaining == 0) {
            result.add(new ArrayList<>(path));
            return;
        }
        for (int i = start; i < sizes.length; i++) {
            if (sizes[i] > remaining) break; // sorted: every later size is too big as well
            path.add(sizes[i]);
            walk(sizes, i, remaining - sizes[i], path, result); // i, not i + 1: the same size may be used again
            path.remove(path.size() - 1);
        }
    }

    public static void main(String[] args) {
        check(combinationsToTarget(new int[] {2, 3, 5}, 8).equals(List.of(List.of(2, 2, 2, 2), List.of(2, 3, 3), List.of(3, 5))));
        check(combinationsToTarget(new int[] {7, 3, 2}, 7).equals(List.of(List.of(2, 2, 3), List.of(7))));
        check(combinationsToTarget(new int[] {4, 6}, 5).isEmpty());
        check(combinationsToTarget(new int[] {1}, 3).equals(List.of(List.of(1, 1, 1))));
        check(combinationsToTarget(new int[] {9}, 9).equals(List.of(List.of(9))));
        check(combinationsToTarget(new int[] {5, 1}, 6).equals(List.of(List.of(1, 1, 1, 1, 1, 1), List.of(1, 5))));
        System.out.println("156 ok");
    }

    static void check(boolean ok) {
        if (!ok) throw new AssertionError("test failed");
    }
}
