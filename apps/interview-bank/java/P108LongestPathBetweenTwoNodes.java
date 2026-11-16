// 108 · Longest Path Between Two Nodes
import java.util.ArrayDeque;
import java.util.Deque;

public class P108LongestPathBetweenTwoNodes {
    static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;

        TreeNode(int val) {
            this.val = val;
        }
    }

    static int longestPath(TreeNode root) {
        int[] best = {0};
        depth(root, best);
        return best[0];
    }

    // Returns the depth in nodes, which is also the number of edges from the parent down.
    static int depth(TreeNode node, int[] best) {
        if (node == null) return 0;
        int left = depth(node.left, best);
        int right = depth(node.right, best);
        best[0] = Math.max(best[0], left + right);
        return 1 + Math.max(left, right);
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
        check(longestPath(fromLevelOrder(8, 4, 11, 2, 6, null, null, 1)) == 4);
        check(longestPath(fromLevelOrder(1, 2, null, 3, 4, 5, null, null, 6, 7, null, null, 8)) == 6);
        check(longestPath(fromLevelOrder()) == 0);
        check(longestPath(fromLevelOrder(9)) == 0);
        check(longestPath(fromLevelOrder(1, 2)) == 1);
        System.out.println("108 ok");
    }

    static void check(boolean ok) {
        if (!ok) throw new AssertionError("test failed");
    }
}
