// 114 · The k-th Smallest Key in a BST
import java.util.ArrayDeque;
import java.util.Deque;

public class P114TheKThSmallestKeyInABst {
    static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;

        TreeNode(int val) {
            this.val = val;
        }
    }

    static int kthSmallest(TreeNode root, int k) {
        Deque<TreeNode> stack = new ArrayDeque<>();
        TreeNode node = root;
        while (true) {
            while (node != null) {
                stack.push(node);
                node = node.left;
            }
            TreeNode smallest = stack.pop(); // the smallest key not yet counted
            k--;
            if (k == 0) return smallest.val;
            node = smallest.right;
        }
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
        check(kthSmallest(fromLevelOrder(20, 10, 30, 5, 15, 25, 40), 3) == 15);
        check(kthSmallest(fromLevelOrder(6, 3, 8, 1, 4, null, 9, null, 2), 2) == 2);
        check(kthSmallest(fromLevelOrder(6, 3, 8, 1, 4, null, 9, null, 2), 7) == 9);
        check(kthSmallest(fromLevelOrder(42), 1) == 42);
        check(kthSmallest(fromLevelOrder(1, null, 2, null, 3), 3) == 3);
        check(kthSmallest(fromLevelOrder(3, 2, null, 1), 1) == 1);
        System.out.println("114 ok");
    }

    static void check(boolean ok) {
        if (!ok) throw new AssertionError("test failed");
    }
}
