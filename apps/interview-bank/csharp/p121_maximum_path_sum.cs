// 121 · Maximum Path Sum
Check(MaxPathSum(FromLevelOrder([6, -8, 3, null, 9, -1, 4])!) == 14);
Check(MaxPathSum(FromLevelOrder([-3, -1, -2])!) == -1);
Check(MaxPathSum(FromLevelOrder([-5])!) == -5);
Check(MaxPathSum(FromLevelOrder([-10, 4, -1, 6, 7])!) == 17);
Check(MaxPathSum(FromLevelOrder([2, -1, null, 5])!) == 6);
Check(MaxPathSum(FromLevelOrder([1, -2, 3, 4, 5, -6, 2])!) == 9);
Console.WriteLine("121 ok");

static int MaxPathSum(TreeNode root)
{
    var best = root.Val;
    // returns the best sum of a path that starts at node and goes down, or 0 for no node
    int Gain(TreeNode? node)
    {
        if (node == null) return 0;
        var left = Math.Max(0, Gain(node.Left)); // a negative branch is better left out
        var right = Math.Max(0, Gain(node.Right));
        best = Math.Max(best, node.Val + left + right);
        return node.Val + Math.Max(left, right);
    }
    Gain(root);
    return best;
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
