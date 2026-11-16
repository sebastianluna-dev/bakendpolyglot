// 105 · Are Two Trees Identical?
import java.util.ArrayDeque;
import java.util.Deque;

public class P105AreTwoTreesIdentical {
    static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;

        TreeNode(int val) {
            this.val = val;
        }
    }

    static boolean areIdentical(TreeNode a, TreeNode b) {
        if (a == null || b == null) return a == b;
        return a.val == b.val && areIdentical(a.left, b.left) && areIdentical(a.right, b.right);
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
        check(areIdentical(fromLevelOrder(4, 2, 6), fromLevelOrder(4, 2, 6)));
        check(!areIdentical(fromLevelOrder(4, 2), fromLevelOrder(4, null, 2)));
        check(areIdentical(fromLevelOrder(), fromLevelOrder()));
        check(!areIdentical(fromLevelOrder(1), fromLevelOrder()));
        check(!areIdentical(fromLevelOrder(1, 2, 3), fromLevelOrder(1, 2, 4)));
        check(areIdentical(fromLevelOrder(5, null, 8, 7), fromLevelOrder(5, null, 8, 7)));
        System.out.println("105 ok");
    }

    static void check(boolean ok) {
        if (!ok) throw new AssertionError("test failed");
    }
}
