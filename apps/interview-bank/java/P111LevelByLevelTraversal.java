// 111 · Level-by-Level Traversal
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class P111LevelByLevelTraversal {
    static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;

        TreeNode(int val) {
            this.val = val;
        }
    }

    static List<List<Integer>> levelOrder(TreeNode root) {
        List<List<Integer>> levels = new ArrayList<>();
        List<TreeNode> level = root == null ? List.of() : List.of(root);
        while (!level.isEmpty()) {
            List<TreeNode> next = new ArrayList<>();
            List<Integer> values = new ArrayList<>();
            for (TreeNode node : level) {
                values.add(node.val);
                if (node.left != null) next.add(node.left);
                if (node.right != null) next.add(node.right);
            }
            levels.add(values);
            level = next;
        }
        return levels;
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
        check(levelOrder(fromLevelOrder(8, 4, 12, 2, null, 10, 14)).equals(List.of(List.of(8), List.of(4, 12), List.of(2, 10, 14))));
        check(levelOrder(fromLevelOrder(5, 6, null, 7, 8)).equals(List.of(List.of(5), List.of(6), List.of(7, 8))));
        check(levelOrder(fromLevelOrder()).isEmpty());
        check(levelOrder(fromLevelOrder(9)).equals(List.of(List.of(9))));
        check(levelOrder(fromLevelOrder(1, 2, 3, null, 4, 5, null, null, null, 6))
                .equals(List.of(List.of(1), List.of(2, 3), List.of(4, 5), List.of(6))));
        System.out.println("111 ok");
    }

    static void check(boolean ok) {
        if (!ok) throw new AssertionError("test failed");
    }
}
