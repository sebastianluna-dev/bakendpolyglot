// 192 · Keep the Latest Version of Each Record
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class P192KeepTheLatestVersionOfEachRecord {
    record Revision(int id, int version, String payload) {}

    static List<Revision> latestVersions(List<Revision> revisions) {
        Map<Integer, Revision> latest = new HashMap<>();
        for (Revision r : revisions) {
            Revision kept = latest.get(r.id());
            if (kept == null || r.version() > kept.version()) latest.put(r.id(), r); // strictly greater: the first of equal versions stays
        }
        List<Revision> result = new ArrayList<>(latest.values());
        result.sort(Comparator.comparingInt(Revision::id));
        return result;
    }

    static Revision r(int id, int version, String payload) {
        return new Revision(id, version, payload);
    }

    public static void main(String[] args) {
        check(latestVersions(List.of(r(7, 1, "created"), r(3, 2, "in transit"), r(7, 3, "delivered"), r(3, 1, "created"), r(7, 2, "in transit")))
                .equals(List.of(r(3, 2, "in transit"), r(7, 3, "delivered"))));
        check(latestVersions(List.of(r(5, 4, "first"), r(5, 4, "replayed"), r(1, 1, "only")))
                .equals(List.of(r(1, 1, "only"), r(5, 4, "first"))));
        check(latestVersions(List.of()).isEmpty());
        check(latestVersions(List.of(r(42, 9, "x"))).equals(List.of(r(42, 9, "x"))));
        check(latestVersions(List.of(r(10, 1, "a"), r(2, 1, "b"), r(10, 2, "c"), r(-4, 3, "d"), r(2, 5, "e")))
                .equals(List.of(r(-4, 3, "d"), r(2, 5, "e"), r(10, 2, "c"))));
        System.out.println("192 ok");
    }

    static void check(boolean ok) {
        if (!ok) throw new AssertionError("test failed");
    }
}
