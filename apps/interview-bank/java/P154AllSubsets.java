// 154 · All Subsets
import java.util.ArrayList;
import java.util.List;

public class P154AllSubsets {
    static List<List<Integer>> allSubsets(int[] nums) {
        List<List<Integer>> result = new ArrayList<>();
        walk(nums, 0, new ArrayList<>(), result);
        return result;
    }

    static void walk(int[] nums, int start, List<Integer> path, List<List<Integer>> result) {
        result.add(new ArrayList<>(path)); // a copy: path keeps changing
        for (int i = start; i < nums.length; i++) {
            path.add(nums[i]);
            walk(nums, i + 1, path, result);
            path.remove(path.size() - 1);
        }
    }

    public static void main(String[] args) {
        check(allSubsets(new int[] {1, 2, 3}).equals(List.of(List.of(), List.of(1), List.of(1, 2), List.of(1, 2, 3), List.of(1, 3), List.of(2), List.of(2, 3), List.of(3))));
        check(allSubsets(new int[] {4, 1}).equals(List.of(List.of(), List.of(4), List.of(4, 1), List.of(1))));
        check(allSubsets(new int[0]).equals(List.of(List.of())));
        check(allSubsets(new int[] {7}).equals(List.of(List.of(), List.of(7))));
        check(allSubsets(new int[] {-1, 0, 2}).equals(List.of(List.of(), List.of(-1), List.of(-1, 0), List.of(-1, 0, 2), List.of(-1, 2), List.of(0), List.of(0, 2), List.of(2))));
        check(allSubsets(new int[] {0, 1, 2, 3, 4, 5, 6, 7, 8, 9}).size() == 1024);
        System.out.println("154 ok");
    }

    static void check(boolean ok) {
        if (!ok) throw new AssertionError("test failed");
    }
}
