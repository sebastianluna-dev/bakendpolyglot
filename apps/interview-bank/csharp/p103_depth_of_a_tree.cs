// 103 · Depth of a Tree
Check(MaxDepth(FromLevelOrder([5, 2, 9, null, 4, 7, null, null, null, 6])) == 4);
Check(MaxDepth(FromLevelOrder([1, null, 2, null, 3])) == 3);
Check(MaxDepth(FromLevelOrder([])) == 0);
Check(MaxDepth(FromLevelOrder([42])) == 1);
Check(MaxDepth(FromLevelOrder([1, 2, 3])) == 2);
Console.WriteLine("103 ok");

static int MaxDepth(TreeNode? root)
{
    if (root == null) return 0;
    return 1 + Math.Max(MaxDepth(root.Left), MaxDepth(root.Right));
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
