// 200 · Consistent Hash Ring
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class P200ConsistentHashRing {
    static int fnv1a(String s) {
        int h = 0x811c9dc5;
        for (byte b : s.getBytes(StandardCharsets.UTF_8)) {
            h ^= b & 0xff; // byte is signed: without the mask, bytes over 127 flip the high bits
            h *= 0x01000193; // int arithmetic wraps at 2^32, as the hash needs
        }
        return h;
    }

    static int mix32(int h) {
        h ^= h >>> 16;
        h *= 0x85ebca6b;
        h ^= h >>> 13;
        h *= 0xc2b2ae35;
        h ^= h >>> 16;
        return h;
    }

    static long ringHash(String s) {
        return Integer.toUnsignedLong(mix32(fnv1a(s))); // compare as unsigned
    }

    record Point(long hash, String node) {}

    static class HashRing {
        private final int replicas;
        private final Set<String> nodes = new HashSet<>();
        private final List<Point> points = new ArrayList<>(); // sorted by hash, then node

        HashRing(int replicas) {
            this.replicas = replicas;
        }

        void addNode(String node) {
            if (!nodes.add(node)) return;
            for (int i = 0; i < replicas; i++) points.add(new Point(ringHash(node + "#" + i), node));
            points.sort(Comparator.comparingLong(Point::hash).thenComparing(Point::node));
        }

        void removeNode(String node) {
            if (!nodes.remove(node)) return;
            points.removeIf(p -> p.node().equals(node));
        }

        String nodeFor(String key) {
            if (points.isEmpty()) return "";
            long h = ringHash(key);
            int lo = 0, hi = points.size(); // looking for the first point with hash >= h
            while (lo < hi) {
                int mid = lo + (hi - lo) / 2;
                if (points.get(mid).hash() >= h) hi = mid;
                else lo = mid + 1;
            }
            return points.get(lo % points.size()).node(); // past the last point: wrap to the first
        }
    }

    static String owners(HashRing ring) {
        StringBuilder sb = new StringBuilder(); // the last letter of each key's node
        for (String k : List.of("book:1", "book:2", "book:3", "book:4", "book:5", "book:6", "book:7", "book:37")) {
            String node = ring.nodeFor(k);
            if (!node.isEmpty()) sb.append(node.charAt(node.length() - 1));
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        check(fnv1a("") == 0x811c9dc5);
        check(fnv1a("a") == 0xe40c292c);
        check(fnv1a("foobar") == 0xbf9cf968);
        check(mix32(0) == 0);
        check(ringHash("cache-a#0") == 0x6901e554L);
        check(ringHash("book:37") == 0xfcfa1e45L);

        HashRing ring = new HashRing(3);
        check(ring.nodeFor("book:1").equals(""));
        for (String name : List.of("cache-a", "cache-b", "cache-c")) ring.addNode(name);
        check(owners(ring).equals("babccccc"));
        ring.addNode("cache-a");
        ring.removeNode("cache-z");
        check(owners(ring).equals("babccccc"));
        ring.removeNode("cache-b");
        check(owners(ring).equals("aacccccc")); // only cache-b's keys moved
        ring.addNode("cache-b");
        check(owners(ring).equals("babccccc"));
        ring.addNode("cache-d");
        check(owners(ring).equals("babdccdc")); // keys move only to the new node
        for (String name : List.of("cache-a", "cache-b", "cache-c", "cache-d")) ring.removeNode(name);
        check(ring.nodeFor("book:1").equals(""));
        System.out.println("200 ok");
    }

    static void check(boolean ok) {
        if (!ok) throw new AssertionError("test failed");
    }
}
