// 118 · Flatten a Tree Into a List
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.List;

public class P118FlattenATreeIntoAList {
    static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;

        TreeNode(int val) {
            this.val = val;
        }
    }

    static void flatten(TreeNode root) {
        TreeNode node = root;
        while (node != null) {
            if (node.left != null) {
                TreeNode tail = node.left; // the last node of the left subtree in preorder
                while (tail.right != null) tail = tail.right;
                tail.right = node.right;
                node.right = node.left;
                node.left = null;
            }
            node = node.right;
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

    static List<Integer> toLevelOrder(TreeNode root) {
        List<Integer> values = new ArrayList<>();
        if (root == null) return values;
        values.add(root.val);
        Deque<TreeNode> queue = new ArrayDeque<>();
        queue.add(root);
        while (!queue.isEmpty()) {
            TreeNode node = queue.poll();
            for (TreeNode child : new TreeNode[] {node.left, node.right}) {
                values.add(child == null ? null : child.val);
                if (child != null) queue.add(child);
            }
        }
        while (values.get(values.size() - 1) == null) values.remove(values.size() - 1);
        return values;
    }

    static List<Integer> flattened(Integer... values) {
        TreeNode root = fromLevelOrder(values);
        flatten(root);
        return toLevelOrder(root);
    }

    public static void main(String[] args) {
        check(flattened(10, 5, 20, 3, 7, null, 25).equals(Arrays.asList(10, null, 5, null, 3, null, 7, null, 20, null, 25)));
        check(flattened(1, 2, null, 3).equals(Arrays.asList(1, null, 2, null, 3)));
        check(flattened().isEmpty());
        check(flattened(8).equals(Arrays.asList(8)));
        check(flattened(1, null, 2).equals(Arrays.asList(1, null, 2)));
        check(flattened(1, 2, 3, null, null, 4).equals(Arrays.asList(1, null, 2, null, 3, null, 4)));
        System.out.println("118 ok");
    }

    static void check(boolean ok) {
        if (!ok) throw new AssertionError("test failed");
    }
}
