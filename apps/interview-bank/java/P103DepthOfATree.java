// 103 · Depth of a Tree
import java.util.ArrayDeque;
import java.util.Deque;

public class P103DepthOfATree {
    static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;

        TreeNode(int val) {
            this.val = val;
        }
    }

    static int maxDepth(TreeNode root) {
        if (root == null) return 0;
        return 1 + Math.max(maxDepth(root.left), maxDepth(root.right));
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
        check(maxDepth(fromLevelOrder(5, 2, 9, null, 4, 7, null, null, null, 6)) == 4);
        check(maxDepth(fromLevelOrder(1, null, 2, null, 3)) == 3);
        check(maxDepth(fromLevelOrder()) == 0);
        check(maxDepth(fromLevelOrder(42)) == 1);
        check(maxDepth(fromLevelOrder(1, 2, 3)) == 2);
        System.out.println("103 ok");
    }

    static void check(boolean ok) {
        if (!ok) throw new AssertionError("test failed");
    }
}
