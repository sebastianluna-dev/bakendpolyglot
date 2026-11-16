// 109 · A Root-to-Leaf Path With a Given Sum
import java.util.ArrayDeque;
import java.util.Deque;

public class P109ARootToLeafPathWithAGivenSum {
    static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;

        TreeNode(int val) {
            this.val = val;
        }
    }

    static boolean hasPathSum(TreeNode root, int target) {
        if (root == null) return false;
        if (root.left == null && root.right == null) return root.val == target;
        int rest = target - root.val;
        return hasPathSum(root.left, rest) || hasPathSum(root.right, rest);
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
        check(hasPathSum(fromLevelOrder(5, 3, 8, 1, null, null, 4), 9));
        check(!hasPathSum(fromLevelOrder(2, 7), 2));
        check(!hasPathSum(fromLevelOrder(), 0));
        check(hasPathSum(fromLevelOrder(5, 3, 8, 1, null, null, 4), 17));
        check(!hasPathSum(fromLevelOrder(5, 3, 8, 1, null, null, 4), 8));
        check(hasPathSum(fromLevelOrder(-3, 1, -2), -5));
        System.out.println("109 ok");
    }

    static void check(boolean ok) {
        if (!ok) throw new AssertionError("test failed");
    }
}
