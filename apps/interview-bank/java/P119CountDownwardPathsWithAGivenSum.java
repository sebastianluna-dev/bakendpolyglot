// 119 · Count Downward Paths With a Given Sum
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

public class P119CountDownwardPathsWithAGivenSum {
    static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;

        TreeNode(int val) {
            this.val = val;
        }
    }

    static int countPathsWithSum(TreeNode root, int target) {
        Map<Long, Integer> seen = new HashMap<>(); // prefix sums on the current root path -> count
        seen.put(0L, 1);
        return visit(root, 0L, target, seen);
    }

    static int visit(TreeNode node, long prefix, int target, Map<Long, Integer> seen) {
        if (node == null) return 0;
        long total = prefix + node.val;
        int count = seen.getOrDefault(total - target, 0);
        seen.merge(total, 1, Integer::sum);
        count += visit(node.left, total, target, seen) + visit(node.right, total, target, seen);
        seen.merge(total, -1, Integer::sum);
        return count;
    }

    static TreeNode fromLevelOrder(Integer... values) {
        if (values.length == 0 || values[0] == null) return null;
        TreeNode root = new TreeNode(values[0]);
        Deque<TreeNode> queue = new ArrayDeque<>();
        queue.add(root);
        for (int i = 1; i < values.length; i += 2) {
            TreeNode node = queue.poll();
            if (values[i] != null) {
                node.left = new TreeNode(values[i]);
                queue.add(node.left);
            }
            if (i + 1 < values.length && values[i + 1] != null) {
                node.right = new TreeNode(values[i + 1]);
                queue.add(node.right);
            }
        }
        return root;
    }

    public static void main(String[] args) {
        check(countPathsWithSum(fromLevelOrder(4, 2, -1, 1, 3, null, 5), 5) == 2);
        check(countPathsWithSum(fromLevelOrder(1, -1, 1, 1), 1) == 4);
        check(countPathsWithSum(fromLevelOrder(), 0) == 0);
        check(countPathsWithSum(fromLevelOrder(0, 0, null, 0), 0) == 6);
        check(countPathsWithSum(fromLevelOrder(1000000000, null, 1000000000), 2000000000) == 1);
        check(countPathsWithSum(fromLevelOrder(1000000000, null, 1000000000), -294967296) == 0);
        System.out.println("119 ok");
    }

    static void check(boolean ok) {
        if (!ok) throw new AssertionError("test failed");
    }
}
