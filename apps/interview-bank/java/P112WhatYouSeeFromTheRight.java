// 112 · What You See From the Right
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class P112WhatYouSeeFromTheRight {
    static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;

        TreeNode(int val) {
            this.val = val;
        }
    }

    static List<Integer> rightSideView(TreeNode root) {
        List<Integer> view = new ArrayList<>();
        visit(root, 0, view);
        return view;
    }

    static void visit(TreeNode node, int depth, List<Integer> view) {
        if (node == null) return;
        if (depth == view.size()) view.add(node.val); // first node reached at this depth
        visit(node.right, depth + 1, view);
        visit(node.left, depth + 1, view);
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
        check(rightSideView(fromLevelOrder(10, 4, 15, 2, 6, null, 20, null, null, 5)).equals(List.of(10, 15, 20, 5)));
        check(rightSideView(fromLevelOrder(1, 2)).equals(List.of(1, 2)));
        check(rightSideView(fromLevelOrder()).isEmpty());
        check(rightSideView(fromLevelOrder(4)).equals(List.of(4)));
        check(rightSideView(fromLevelOrder(1, 2, null, 3)).equals(List.of(1, 2, 3)));
        check(rightSideView(fromLevelOrder(1, 2, 3, 4, 5, 6, 7)).equals(List.of(1, 3, 7)));
        System.out.println("112 ok");
    }

    static void check(boolean ok) {
        if (!ok) throw new AssertionError("test failed");
    }
}
