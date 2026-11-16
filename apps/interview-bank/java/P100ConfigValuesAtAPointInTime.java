// 100 · Config Values at a Point in Time
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class P100ConfigValuesAtAPointInTime {
    record Entry(int timestamp, String value) {}

    static class ConfigHistory {
        private final Map<String, List<Entry>> history = new HashMap<>(); // oldest first

        void set(String key, String value, int timestamp) {
            // timestamps only grow, so the list stays sorted
            history.computeIfAbsent(key, k -> new ArrayList<>()).add(new Entry(timestamp, value));
        }

        String get(String key, int timestamp) {
            List<Entry> entries = history.getOrDefault(key, List.of());
            int lo = 0, hi = entries.size(); // looking for the first entry set after timestamp
            while (lo < hi) {
                int mid = lo + (hi - lo) / 2;
                if (entries.get(mid).timestamp() > timestamp) hi = mid;
                else lo = mid + 1;
            }
            return lo == 0 ? "" : entries.get(lo - 1).value();
        }
    }

    public static void main(String[] args) {
        ConfigHistory h = new ConfigHistory();
        check(h.get("hold.minutes", 100).equals(""));
        h.set("hold.minutes", "10", 100);
        h.set("hold.minutes", "15", 250);
        h.set("currency", "EUR", 120);
        check(h.get("hold.minutes", 99).equals(""));
        check(h.get("hold.minutes", 100).equals("10"));
        check(h.get("hold.minutes", 249).equals("10"));
        check(h.get("hold.minutes", 250).equals("15"));
        check(h.get("hold.minutes", 1000).equals("15"));
        check(h.get("currency", 119).equals(""));
        check(h.get("currency", 500).equals("EUR"));
        check(h.get("refunds", 500).equals(""));
        h.set("hold.minutes", "20", 400);
        check(h.get("hold.minutes", 399).equals("15"));
        check(h.get("hold.minutes", 400).equals("20"));
        System.out.println("100 ok");
    }

    static void check(boolean ok) {
        if (!ok) throw new AssertionError("test failed");
    }
}
