// 108 · Longest Path Between Two Nodes
Check(LongestPath(FromLevelOrder([8, 4, 11, 2, 6, null, null, 1])) == 4);
Check(LongestPath(FromLevelOrder([1, 2, null, 3, 4, 5, null, null, 6, 7, null, null, 8])) == 6);
Check(LongestPath(FromLevelOrder([])) == 0);
Check(LongestPath(FromLevelOrder([9])) == 0);
Check(LongestPath(FromLevelOrder([1, 2])) == 1);
Console.WriteLine("108 ok");

static int LongestPath(TreeNode? root)
{
    var best = 0;
    Depth(root);
    return best;

    // Returns the depth in nodes, which is also the number of edges from the parent down.
    int Depth(TreeNode? node)
    {
        if (node == null) return 0;
        var left = Depth(node.Left);
        var right = Depth(node.Right);
        best = Math.Max(best, left + right);
        return 1 + Math.Max(left, right);
    }
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
