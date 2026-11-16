// 107 · Is the Tree Height-Balanced?
import java.util.ArrayDeque;
import java.util.Deque;

public class P107IsTheTreeHeightBalanced {
    static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;

        TreeNode(int val) {
            this.val = val;
        }
    }

    static boolean isBalanced(TreeNode root) {
        return balancedHeight(root) != -1;
    }

    // The height of the subtree, or -1 as soon as any subtree in it is unbalanced.
    static int balancedHeight(TreeNode node) {
        if (node == null) return 0;
        int left = balancedHeight(node.left);
        if (left == -1) return -1;
        int right = balancedHeight(node.right);
        if (right == -1 || Math.abs(left - right) > 1) return -1;
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
        check(isBalanced(fromLevelOrder(10, 5, 15, null, null, 12, 20)));
        check(!isBalanced(fromLevelOrder(10, 5, null, 3, null, 1)));
        check(isBalanced(fromLevelOrder()));
        check(!isBalanced(fromLevelOrder(1, 2, 3, 4, null, null, 5, 6, null, null, 7)));
        check(isBalanced(fromLevelOrder(1, 2, 3, 4)));
        check(isBalanced(fromLevelOrder(1, 2)));
        System.out.println("107 ok");
    }

    static void check(boolean ok) {
        if (!ok) throw new AssertionError("test failed");
    }
}
