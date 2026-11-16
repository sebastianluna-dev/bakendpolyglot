// 107 · Is the Tree Height-Balanced?
Check(IsBalanced(FromLevelOrder([10, 5, 15, null, null, 12, 20])));
Check(!IsBalanced(FromLevelOrder([10, 5, null, 3, null, 1])));
Check(IsBalanced(FromLevelOrder([])));
Check(!IsBalanced(FromLevelOrder([1, 2, 3, 4, null, null, 5, 6, null, null, 7])));
Check(IsBalanced(FromLevelOrder([1, 2, 3, 4])));
Check(IsBalanced(FromLevelOrder([1, 2])));
Console.WriteLine("107 ok");

static bool IsBalanced(TreeNode? root) => BalancedHeight(root) != -1;

// The height of the subtree, or -1 as soon as any subtree in it is unbalanced.
static int BalancedHeight(TreeNode? node)
{
    if (node == null) return 0;
    var left = BalancedHeight(node.Left);
    if (left == -1) return -1;
    var right = BalancedHeight(node.Right);
    if (right == -1 || Math.Abs(left - right) > 1) return -1;
    return 1 + Math.Max(left, right);
}

static TreeNode? FromLevelOrder(int?[] values)
{
    if (values.Length == 0 || values[0] is not int rootVal) return null;
    var root = new TreeNode(rootVal);
    var queue = new Queue<TreeNode>();
    queue.Enqueue(root);
    for (var i = 1; i < values.Length; i += 2)
    {
        var node = queue.Dequeue();
        if (values[i] is int left)
        {
            node.Left = new TreeNode(left);
            queue.Enqueue(node.Left);
        }
        if (i + 1 < values.Length && values[i + 1] is int right)
        {
            node.Right = new TreeNode(right);
            queue.Enqueue(node.Right);
        }
    }
    return root;
}

static void Check(bool ok)
{
    if (!ok) throw new Exception("test failed");
}

class TreeNode(int val, TreeNode? left = null, TreeNode? right = null)
{
    public int Val = val;
    public TreeNode? Left = left;
    public TreeNode? Right = right;
}
