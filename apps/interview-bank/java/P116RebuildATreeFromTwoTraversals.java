// 116 · Rebuild a Tree From Two Traversals
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class P116RebuildATreeFromTwoTraversals {
    static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;

        TreeNode(int val) {
            this.val = val;
        }
    }

    static TreeNode buildTree(int[] preorder, int[] inorder) {
        Map<Integer, Integer> position = new HashMap<>();
        for (int i = 0; i < inorder.length; i++) position.put(inorder[i], i);
        return build(preorder, position, 0, 0, inorder.length - 1);
    }

    // builds the subtree whose preorder starts at pre and whose inorder is inorder[lo..hi]
    static TreeNode build(int[] preorder, Map<Integer, Integer> position, int pre, int lo, int hi) {
        if (lo > hi) return null;
        TreeNode root = new TreeNode(preorder[pre]);
        int mid = position.get(root.val);
        root.left = build(preorder, position, pre + 1, lo, mid - 1);
        root.right = build(preorder, position, pre + 1 + (mid - lo), mid + 1, hi);
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
        check(toLevelOrder(buildTree(new int[] {8, 4, 2, 6, 12, 14}, new int[] {2, 4, 6, 8, 12, 14}))
                .equals(Arrays.asList(8, 4, 12, 2, 6, null, 14)));
        check(toLevelOrder(buildTree(new int[] {1, 2, 3}, new int[] {3, 2, 1})).equals(Arrays.asList(1, 2, null, 3)));
        check(toLevelOrder(buildTree(new int[] {}, new int[] {})).isEmpty());
        check(toLevelOrder(buildTree(new int[] {5}, new int[] {5})).equals(Arrays.asList(5)));
        check(toLevelOrder(buildTree(new int[] {1, 2, 3}, new int[] {1, 2, 3})).equals(Arrays.asList(1, null, 2, null, 3)));
        check(toLevelOrder(buildTree(new int[] {-1, -3, 0}, new int[] {-3, -1, 0})).equals(Arrays.asList(-1, -3, 0)));
        System.out.println("116 ok");
    }

    static void check(boolean ok) {
        if (!ok) throw new AssertionError("test failed");
    }
}
