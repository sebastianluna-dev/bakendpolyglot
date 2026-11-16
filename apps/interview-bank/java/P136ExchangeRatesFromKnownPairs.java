// 136 · Exchange Rates From Known Pairs
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class P136ExchangeRatesFromKnownPairs {
    record Edge(String to, double rate) {}

    static double[] exchangeRates(String[][] pairs, double[] rates, String[][] queries) {
        Map<String, List<Edge>> graph = new HashMap<>();
        for (int i = 0; i < pairs.length; i++) {
            String from = pairs[i][0], to = pairs[i][1];
            graph.computeIfAbsent(from, k -> new ArrayList<>()).add(new Edge(to, rates[i]));
            graph.computeIfAbsent(to, k -> new ArrayList<>()).add(new Edge(from, 1 / rates[i]));
        }
        Map<String, Integer> group = new HashMap<>();
        Map<String, Double> value = new HashMap<>(); // units of this currency worth one unit of its group's first
        for (String start : graph.keySet()) {
            if (group.containsKey(start)) continue;
            int id = group.size();
            group.put(start, id);
            value.put(start, 1.0);
            Deque<String> queue = new ArrayDeque<>();
            queue.offer(start);
            while (!queue.isEmpty()) {
                String cur = queue.poll();
                for (Edge e : graph.get(cur)) {
                    if (group.containsKey(e.to())) continue;
                    group.put(e.to(), id);
                    value.put(e.to(), value.get(cur) * e.rate());
                    queue.offer(e.to());
                }
            }
        }
        double[] answers = new double[queries.length];
        for (int i = 0; i < queries.length; i++) {
            String x = queries[i][0], y = queries[i][1];
            Integer gx = group.get(x), gy = group.get(y);
            answers[i] = gx != null && gx.equals(gy) ? value.get(y) / value.get(x) : -1.0;
        }
        return answers;
    }

    static boolean near(double[] got, double[] want) {
        if (got.length != want.length) return false;
        for (int i = 0; i < got.length; i++) {
            if (Math.abs(got[i] - want[i]) >= 1e-9) return false;
        }
        return true;
    }

    public static void main(String[] args) {
        check(near(
            exchangeRates(
                new String[][] {{"EUR", "USD"}, {"USD", "MXN"}},
                new double[] {1.25, 20.0},
                new String[][] {{"EUR", "MXN"}, {"MXN", "EUR"}, {"USD", "USD"}, {"EUR", "JPY"}, {"JPY", "JPY"}}),
            new double[] {25.0, 0.04, 1.0, -1.0, -1.0}));
        check(near(
            exchangeRates(
                new String[][] {{"GBP", "EUR"}, {"CHF", "SEK"}, {"SEK", "NOK"}},
                new double[] {1.2, 12.0, 0.5},
                new String[][] {{"EUR", "GBP"}, {"CHF", "NOK"}, {"NOK", "CHF"}, {"GBP", "NOK"}}),
            new double[] {0.8333333333, 6.0, 0.1666666667, -1.0}));
        check(near(exchangeRates(new String[][] {{"EUR", "USD"}}, new double[] {1.25}, new String[0][]), new double[0]));
        check(near(
            exchangeRates(
                new String[][] {{"USD", "CAD"}, {"CAD", "AUD"}, {"USD", "AUD"}},
                new double[] {1.5, 1.2, 1.8},
                new String[][] {{"AUD", "USD"}, {"CAD", "CAD"}}),
            new double[] {0.5555555556, 1.0}));
        System.out.println("136 ok");
    }

    static void check(boolean ok) {
        if (!ok) throw new AssertionError("test failed");
    }
}
