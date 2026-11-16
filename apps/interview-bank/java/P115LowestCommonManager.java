// 115 · Lowest Common Manager
import java.util.ArrayDeque;
import java.util.Deque;

public class P115LowestCommonManager {
    static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;

        TreeNode(int val) {
            this.val = val;
        }
    }

    static int lowestCommonManager(TreeNode root, int a, int b) {
        return lowest(root, a, b).val;
    }

    // returns the answer if both ids are below node, else whichever of the two is below it, else null
    static TreeNode lowest(TreeNode node, int a, int b) {
        if (node == null || node.val == a || node.val == b) return node;
        TreeNode left = lowest(node.left, a, b);
        TreeNode right = lowest(node.right, a, b);
        if (left != null && right != null) return node;
        return left != null ? left : right;
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
        Integer[] chart = {1, 2, 3, 4, 5, 6, 7, null, null, 8, 9};
        check(lowestCommonManager(fromLevelOrder(chart), 8, 4) == 2);
        check(lowestCommonManager(fromLevelOrder(chart), 9, 7) == 1);
        check(lowestCommonManager(fromLevelOrder(chart), 5, 9) == 5);
        check(lowestCommonManager(fromLevelOrder(chart), 6, 6) == 6);
        check(lowestCommonManager(fromLevelOrder(chart), 8, 9) == 5);
        check(lowestCommonManager(fromLevelOrder(1, 2), 2, 1) == 1);
        System.out.println("115 ok");
    }

    static void check(boolean ok) {
        if (!ok) throw new AssertionError("test failed");
    }
}
