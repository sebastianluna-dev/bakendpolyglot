// 155 · All Orderings
import java.util.ArrayList;
import java.util.List;

public class P155AllOrderings {
    static List<List<Integer>> allOrderings(int[] nums) {
        List<List<Integer>> result = new ArrayList<>();
        walk(nums, new boolean[nums.length], new ArrayList<>(), result);
        return result;
    }

    static void walk(int[] nums, boolean[] used, List<Integer> path, List<List<Integer>> result) {
        if (path.size() == nums.length) {
            result.add(new ArrayList<>(path));
            return;
        }
        for (int i = 0; i < nums.length; i++) {
            if (used[i]) continue;
            used[i] = true;
            path.add(nums[i]);
            walk(nums, used, path, result);
            path.remove(path.size() - 1); // by index: remove(Integer) would remove a value
            used[i] = false;
        }
    }

    public static void main(String[] args) {
        check(allOrderings(new int[] {1, 2, 3}).equals(List.of(List.of(1, 2, 3), List.of(1, 3, 2), List.of(2, 1, 3), List.of(2, 3, 1), List.of(3, 1, 2), List.of(3, 2, 1))));
        check(allOrderings(new int[] {3, 1, 2}).equals(List.of(List.of(3, 1, 2), List.of(3, 2, 1), List.of(1, 3, 2), List.of(1, 2, 3), List.of(2, 3, 1), List.of(2, 1, 3))));
        check(allOrderings(new int[0]).equals(List.of(List.of())));
        check(allOrderings(new int[] {9}).equals(List.of(List.of(9))));
        check(allOrderings(new int[] {5, -5}).equals(List.of(List.of(5, -5), List.of(-5, 5))));
        check(allOrderings(new int[] {1, 2, 3, 4, 5, 6}).size() == 720);
        System.out.println("155 ok");
    }

    static void check(boolean ok) {
        if (!ok) throw new AssertionError("test failed");
    }
}
