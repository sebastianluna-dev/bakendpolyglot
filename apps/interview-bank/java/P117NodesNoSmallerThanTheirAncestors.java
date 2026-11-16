// 117 · Nodes No Smaller Than Their Ancestors
import java.util.ArrayDeque;
import java.util.Deque;

public class P117NodesNoSmallerThanTheirAncestors {
    static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;

        TreeNode(int val) {
            this.val = val;
        }
    }

    static int countGoodNodes(TreeNode root) {
        return root == null ? 0 : countFrom(root, root.val);
    }

    // highest is the largest value on the path from the root down to node's parent
    static int countFrom(TreeNode node, int highest) {
        if (node == null) return 0;
        int good = node.val >= highest ? 1 : 0;
        int next = Math.max(highest, node.val);
        return good + countFrom(node.left, next) + countFrom(node.right, next);
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
        check(countGoodNodes(fromLevelOrder(5, 3, 8, 6, 2, 5, 9)) == 4);
        check(countGoodNodes(fromLevelOrder(2, 2, 2)) == 3);
        check(countGoodNodes(fromLevelOrder()) == 0);
        check(countGoodNodes(fromLevelOrder(7)) == 1);
        check(countGoodNodes(fromLevelOrder(9, 8, null, 7)) == 1);
        check(countGoodNodes(fromLevelOrder(-5, -10, -1)) == 2);
        System.out.println("117 ok");
    }

    static void check(boolean ok) {
        if (!ok) throw new AssertionError("test failed");
    }
}
