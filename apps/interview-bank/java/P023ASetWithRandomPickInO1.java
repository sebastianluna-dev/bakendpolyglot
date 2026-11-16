// 023 · A Set With Random Pick in O(1)
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.IntUnaryOperator;

public class P023ASetWithRandomPickInO1 {
    static class RandomSet {
        private final List<Integer> items = new ArrayList<>();
        private final Map<Integer, Integer> position = new HashMap<>(); // value -> its index in items
        private final IntUnaryOperator randomIndex;

        RandomSet(IntUnaryOperator randomIndex) {
            this.randomIndex = randomIndex;
        }

        boolean add(int value) {
            if (position.containsKey(value)) return false;
            position.put(value, items.size());
            items.add(value);
            return true;
        }

        boolean remove(int value) {
            Integer i = position.get(value);
            if (i == null) return false;
            int last = items.remove(items.size() - 1);
            if (last != value) {
                items.set(i, last);
                position.put(last, i);
            }
            position.remove(value);
            return true;
        }

        int pick() {
            return items.get(randomIndex.applyAsInt(items.size()));
        }
    }

    static int[] picks(RandomSet set, int count) {
        int[] result = new int[count];
        for (int k = 0; k < count; k++) result[k] = set.pick();
        Arrays.sort(result);
        return result;
    }

    public static void main(String[] args) {
        int[] calls = {0};
        RandomSet set = new RandomSet(n -> calls[0]++ % n);
        check(set.add(12));
        check(set.add(7));
        check(!set.add(12));
        check(set.add(30));
        check(set.remove(7));
        check(!set.remove(7));
        check(!set.remove(99));
        check(Arrays.equals(picks(set, 2), new int[] {12, 30}));
        check(set.add(5));
        check(Arrays.equals(picks(set, 3), new int[] {5, 12, 30}));
        check(set.remove(30));
        check(set.remove(5));
        check(set.add(5));
        check(set.remove(12));
        check(set.pick() == 5);
        System.out.println("023 ok");
    }

    static void check(boolean ok) {
        if (!ok) throw new AssertionError("test failed");
    }
}
