// 104 · Mirror a Tree
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.List;

public class P104MirrorATree {
    static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;

        TreeNode(int val) {
            this.val = val;
        }
    }

    static TreeNode mirrorTree(TreeNode root) {
        if (root == null) return null;
        TreeNode left = root.left; // keep it: the next line overwrites root.left
        root.left = mirrorTree(root.right);
        root.right = mirrorTree(left);
        return root;
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

    public static void main(String[] args) {
        check(toLevelOrder(mirrorTree(fromLevelOrder(6, 2, 8, 1, 4, null, 9))).equals(Arrays.asList(6, 8, 2, 9, null, 4, 1)));
        check(toLevelOrder(mirrorTree(fromLevelOrder(3, 1))).equals(Arrays.asList(3, null, 1)));
        check(toLevelOrder(mirrorTree(fromLevelOrder())).isEmpty());
        check(toLevelOrder(mirrorTree(fromLevelOrder(7))).equals(Arrays.asList(7)));
        check(toLevelOrder(mirrorTree(fromLevelOrder(1, 2, null, 3))).equals(Arrays.asList(1, null, 2, null, 3)));
        System.out.println("104 ok");
    }

    static void check(boolean ok) {
        if (!ok) throw new AssertionError("test failed");
    }
}
