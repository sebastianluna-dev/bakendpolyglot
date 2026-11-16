// 120 · Serialize and Deserialize a Tree
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;

public class P120SerializeAndDeserializeATree {
    static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;

        TreeNode(int val) {
            this.val = val;
        }
    }

    static String serialize(TreeNode root) {
        List<String> tokens = new ArrayList<>();
        write(root, tokens);
        return String.join(",", tokens);
    }

    static void write(TreeNode node, List<String> tokens) {
        if (node == null) {
            tokens.add("#");
            return;
        }
        tokens.add(String.valueOf(node.val));
        write(node.left, tokens);
        write(node.right, tokens);
    }

    static TreeNode deserialize(String data) {
        return read(Arrays.asList(data.split(",")).iterator());
    }

    static TreeNode read(Iterator<String> tokens) {
        String token = tokens.next();
        if (token.equals("#")) return null;
        TreeNode node = new TreeNode(Integer.parseInt(token));
        node.left = read(tokens);
        node.right = read(tokens);
        return node;
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
        check(serialize(fromLevelOrder(7, 3, 9, null, 4, 8)).equals("7,3,#,4,#,#,9,8,#,#,#"));
        check(serialize(fromLevelOrder()).equals("#"));
        check(toLevelOrder(deserialize("5,-2,#,#,#")).equals(Arrays.asList(5, -2)));
        Integer[][] trees = {{7, 3, 9, null, 4, 8}, {}, {-1000}, {1, null, 2, null, 3}, {10, -20, 30, -40, 50, -60, 70}};
        for (Integer[] tree : trees) {
            check(toLevelOrder(deserialize(serialize(fromLevelOrder(tree)))).equals(Arrays.asList(tree)));
        }
        System.out.println("120 ok");
    }

    static void check(boolean ok) {
        if (!ok) throw new AssertionError("test failed");
    }
}
