// 012 · Shared Members of Two Lists
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class P012SharedMembersOfTwoLists {
    static int[] sharedMembers(int[] first, int[] second) {
        Set<Integer> pending = new HashSet<>();
        for (int id : second) pending.add(id);
        List<Integer> shared = new ArrayList<>();
        for (int id : first) {
            if (pending.remove(id)) shared.add(id); // removed, so a repeat in first is skipped
        }
        return shared.stream().mapToInt(Integer::intValue).toArray();
    }

    public static void main(String[] args) {
        check(Arrays.equals(sharedMembers(new int[] {104, 211, 104, 350, 87}, new int[] {87, 104, 999, 104}), new int[] {104, 87}));
        check(Arrays.equals(sharedMembers(new int[] {1, 2}, new int[] {3, 4}), new int[0]));
        check(Arrays.equals(sharedMembers(new int[0], new int[] {1}), new int[0]));
        check(Arrays.equals(sharedMembers(new int[] {5, 5, 5}, new int[] {5}), new int[] {5}));
        check(Arrays.equals(sharedMembers(new int[] {3, 1, 2}, new int[] {2, 3}), new int[] {3, 2}));
        System.out.println("012 ok");
    }

    static void check(boolean ok) {
        if (!ok) throw new AssertionError("test failed");
    }
}
