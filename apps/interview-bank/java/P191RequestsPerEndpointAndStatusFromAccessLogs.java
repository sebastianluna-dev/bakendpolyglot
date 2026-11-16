// 191 · Requests per Endpoint and Status From Access Logs
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class P191RequestsPerEndpointAndStatusFromAccessLogs {
    static List<String> countRequests(List<String> lines) {
        Map<String, Integer> counts = new HashMap<>(); // "METHOD PATH STATUS" -> requests
        for (String line : lines) {
            String[] fields = line.split(" ", -1); // -1 keeps trailing empty fields
            if (fields.length != 5 || Arrays.asList(fields).contains("")) continue;
            counts.merge(fields[1] + " " + fields[2] + " " + fields[3], 1, Integer::sum);
        }
        List<Map.Entry<String, Integer>> ordered = new ArrayList<>(counts.entrySet());
        ordered.sort(Map.Entry.<String, Integer>comparingByValue().reversed()
                .thenComparing(Map.Entry.comparingByKey()));
        List<String> result = new ArrayList<>();
        for (Map.Entry<String, Integer> e : ordered) result.add(e.getKey() + " " + e.getValue());
        return result;
    }

    public static void main(String[] args) {
        check(countRequests(List.of(
                "2026-03-02T09:00:01Z GET /books 200 12",
                "2026-03-02T09:00:02Z POST /loans 201 40",
                "2026-03-02T09:00:03Z GET /books 200 9",
                "2026-03-02T09:00:04Z POST /loans 409 15",
                "2026-03-02T09:00:05Z GET /books 200 11",
                "2026-03-02T09:00:06Z POST /loans 201 38"))
                .equals(List.of("GET /books 200 3", "POST /loans 201 2", "POST /loans 409 1")));
        check(countRequests(List.of(
                "2026-03-02T09:01:00Z GET /members/7 404 3",
                "",
                "garbage",
                "2026-03-02T09:01:02Z DELETE /holds/9 204 5",
                "2026-03-02T09:01:03Z GET /members/7 200 4"))
                .equals(List.of("DELETE /holds/9 204 1", "GET /members/7 200 1", "GET /members/7 404 1")));
        check(countRequests(List.of()).isEmpty());
        check(countRequests(List.of(
                "2026-03-02T09:02:00Z GET  /books 200 7",
                "2026-03-02T09:02:01Z GET /books 200 7 ",
                "2026-03-02T09:02:02Z GET /books 200")).isEmpty());
        check(countRequests(List.of("t1 GET /a 500 1", "t2 GET /b 200 1", "t3 GET /a 500 1", "t4 GET /b 200 1", "t5 GET /a 200 1"))
                .equals(List.of("GET /a 500 2", "GET /b 200 2", "GET /a 200 1")));
        System.out.println("191 ok");
    }

    static void check(boolean ok) {
        if (!ok) throw new AssertionError("test failed");
    }
}
