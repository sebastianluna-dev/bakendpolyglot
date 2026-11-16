// 121 · Maximum Path Sum
import java.util.ArrayDeque;
import java.util.Deque;

public class P121MaximumPathSum {
    static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;

        TreeNode(int val) {
            this.val = val;
        }
    }

    static int maxPathSum(TreeNode root) {
        int[] best = {root.val}; // an array, so gain can update it
        gain(root, best);
        return best[0];
    }

    // returns the best sum of a path that starts at node and goes down, or 0 for no node
    static int gain(TreeNode node, int[] best) {
        if (node == null) return 0;
        int left = Math.max(0, gain(node.left, best)); // a negative branch is better left out
        int right = Math.max(0, gain(node.right, best));
        best[0] = Math.max(best[0], node.val + left + right);
        return node.val + Math.max(left, right);
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
        check(maxPathSum(fromLevelOrder(6, -8, 3, null, 9, -1, 4)) == 14);
        check(maxPathSum(fromLevelOrder(-3, -1, -2)) == -1);
        check(maxPathSum(fromLevelOrder(-5)) == -5);
        check(maxPathSum(fromLevelOrder(-10, 4, -1, 6, 7)) == 17);
        check(maxPathSum(fromLevelOrder(2, -1, null, 5)) == 6);
        check(maxPathSum(fromLevelOrder(1, -2, 3, 4, 5, -6, 2)) == 9);
        System.out.println("121 ok");
    }

    static void check(boolean ok) {
        if (!ok) throw new AssertionError("test failed");
    }
}
