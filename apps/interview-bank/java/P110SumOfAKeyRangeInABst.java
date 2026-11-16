// 110 · Sum of a Key Range in a BST
import java.util.ArrayDeque;
import java.util.Deque;

public class P110SumOfAKeyRangeInABst {
    static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;

        TreeNode(int val) {
            this.val = val;
        }
    }

    static int rangeSum(TreeNode root, int lo, int hi) {
        if (root == null) return 0;
        if (root.val < lo) return rangeSum(root.right, lo, hi); // the left subtree is even smaller
        if (root.val > hi) return rangeSum(root.left, lo, hi);
        return root.val + rangeSum(root.left, lo, hi) + rangeSum(root.right, lo, hi);
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
        check(rangeSum(fromLevelOrder(20, 10, 30, 5, 15, 25, 40), 12, 27) == 60);
        check(rangeSum(fromLevelOrder(8, 3, 12, null, 6), 9, 11) == 0);
        check(rangeSum(fromLevelOrder(), 1, 100) == 0);
        check(rangeSum(fromLevelOrder(7), 7, 7) == 7);
        check(rangeSum(fromLevelOrder(20, 10, 30, 5, 15, 25, 40), 1, 100) == 145);
        check(rangeSum(fromLevelOrder(20, 10, 30, 5, 15, 25, 40), 10, 20) == 45);
        System.out.println("110 ok");
    }

    static void check(boolean ok) {
        if (!ok) throw new AssertionError("test failed");
    }
}
