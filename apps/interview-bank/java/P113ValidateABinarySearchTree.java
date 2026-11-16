// 113 · Validate a Binary Search Tree
import java.util.ArrayDeque;
import java.util.Deque;

public class P113ValidateABinarySearchTree {
    static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;

        TreeNode(int val) {
            this.val = val;
        }
    }

    static boolean isSearchTree(TreeNode root) {
        return within(root, null, null);
    }

    // lo and hi are the nearest ancestors the key must stay above and below; null means no bound
    static boolean within(TreeNode node, TreeNode lo, TreeNode hi) {
        if (node == null) return true;
        if (lo != null && node.val <= lo.val) return false;
        if (hi != null && node.val >= hi.val) return false;
        return within(node.left, lo, node) && within(node.right, node, hi);
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
        check(isSearchTree(fromLevelOrder(8, 4, 12, 2, 6, 10, 14)));
        check(!isSearchTree(fromLevelOrder(8, 4, 12, 2, 9, 10, 14)));
        check(isSearchTree(fromLevelOrder()));
        check(!isSearchTree(fromLevelOrder(5, 5)));
        check(isSearchTree(fromLevelOrder(Integer.MIN_VALUE, null, Integer.MAX_VALUE)));
        check(!isSearchTree(fromLevelOrder(Integer.MAX_VALUE, Integer.MAX_VALUE)));
        check(!isSearchTree(fromLevelOrder(Integer.MIN_VALUE, Integer.MIN_VALUE)));
        System.out.println("113 ok");
    }

    static void check(boolean ok) {
        if (!ok) throw new AssertionError("test failed");
    }
}
