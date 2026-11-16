// 106 · Is the Tree Symmetric?
import java.util.ArrayDeque;
import java.util.Deque;

public class P106IsTheTreeSymmetric {
    static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;

        TreeNode(int val) {
            this.val = val;
        }
    }

    static boolean isSymmetric(TreeNode root) {
        return root == null || isMirror(root.left, root.right);
    }

    static boolean isMirror(TreeNode a, TreeNode b) {
        if (a == null || b == null) return a == b;
        return a.val == b.val && isMirror(a.left, b.right) && isMirror(a.right, b.left);
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
        check(isSymmetric(fromLevelOrder(7, 4, 4, 1, 9, 9, 1)));
        check(!isSymmetric(fromLevelOrder(5, 3, 3, 8, null, null, 2)));
        check(isSymmetric(fromLevelOrder()));
        check(isSymmetric(fromLevelOrder(1)));
        check(!isSymmetric(fromLevelOrder(2, 3, 3, null, 4, null, 4)));
        check(!isSymmetric(fromLevelOrder(1, 2, 3)));
        System.out.println("106 ok");
    }

    static void check(boolean ok) {
        if (!ok) throw new AssertionError("test failed");
    }
}
