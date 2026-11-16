// 129 · Clone a Graph
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class P129CloneAGraph {
    static class GraphNode {
        int val;
        List<GraphNode> neighbors = new ArrayList<>();

        GraphNode(int val) {
            this.val = val;
        }
    }

    static GraphNode cloneGraph(GraphNode node) {
        if (node == null) return null;
        Map<GraphNode, GraphNode> copyOf = new HashMap<>(); // original -> copy
        copyOf.put(node, new GraphNode(node.val));
        Deque<GraphNode> queue = new ArrayDeque<>();
        queue.offer(node);
        while (!queue.isEmpty()) {
            GraphNode current = queue.poll();
            for (GraphNode nb : current.neighbors) {
                if (!copyOf.containsKey(nb)) {
                    copyOf.put(nb, new GraphNode(nb.val));
                    queue.offer(nb);
                }
                copyOf.get(current).neighbors.add(copyOf.get(nb));
            }
        }
        return copyOf.get(node);
    }

    // Node i has value i and the neighbours adjacency[i]; returns node 0.
    static GraphNode build(int[][] adjacency) {
        GraphNode[] nodes = new GraphNode[adjacency.length];
        for (int i = 0; i < nodes.length; i++) nodes[i] = new GraphNode(i);
        for (int i = 0; i < nodes.length; i++) {
            for (int j : adjacency[i]) nodes[i].neighbors.add(nodes[j]);
        }
        return nodes.length > 0 ? nodes[0] : null;
    }

    static Set<GraphNode> allNodes(GraphNode node) {
        Set<GraphNode> found = new HashSet<>();
        if (node == null) return found;
        found.add(node);
        Deque<GraphNode> queue = new ArrayDeque<>();
        queue.offer(node);
        while (!queue.isEmpty()) {
            for (GraphNode nb : queue.poll().neighbors) {
                if (found.add(nb)) queue.offer(nb);
            }
        }
        return found;
    }

    static int[][] toAdjacency(GraphNode node) {
        Set<GraphNode> nodes = allNodes(node);
        int[][] adjacency = new int[nodes.size()][];
        for (GraphNode n : nodes) adjacency[n.val] = n.neighbors.stream().mapToInt(nb -> nb.val).toArray();
        return adjacency;
    }

    static void checkClone(int[][] adjacency) {
        GraphNode original = build(adjacency);
        GraphNode copy = cloneGraph(original);
        check(Arrays.deepEquals(toAdjacency(copy), adjacency));
        check(Collections.disjoint(allNodes(original), allNodes(copy)));
    }

    public static void main(String[] args) {
        checkClone(new int[][] {{1, 3}, {0, 2}, {1, 3}, {0, 2}});
        checkClone(new int[][] {{}});
        checkClone(new int[0][]);
        checkClone(new int[][] {{1}, {0}});
        checkClone(new int[][] {{1, 2}, {0, 2}, {0, 1, 3}, {2}});
        System.out.println("129 ok");
    }

    static void check(boolean ok) {
        if (!ok) throw new AssertionError("test failed");
    }
}
