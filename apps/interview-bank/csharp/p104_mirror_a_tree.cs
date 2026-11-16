// 104 · Mirror a Tree
Check(ToLevelOrder(MirrorTree(FromLevelOrder([6, 2, 8, 1, 4, null, 9]))).SequenceEqual([6, 8, 2, 9, null, 4, 1]));
Check(ToLevelOrder(MirrorTree(FromLevelOrder([3, 1]))).SequenceEqual([3, null, 1]));
Check(ToLevelOrder(MirrorTree(FromLevelOrder([]))).Length == 0);
Check(ToLevelOrder(MirrorTree(FromLevelOrder([7]))).SequenceEqual([7]));
Check(ToLevelOrder(MirrorTree(FromLevelOrder([1, 2, null, 3]))).SequenceEqual([1, null, 2, null, 3]));
Console.WriteLine("104 ok");

static TreeNode? MirrorTree(TreeNode? root)
{
    if (root == null) return null;
    (root.Left, root.Right) = (MirrorTree(root.Right), MirrorTree(root.Left));
    return root;
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

static int?[] ToLevelOrder(TreeNode? root)
{
    if (root == null) return [];
    var values = new List<int?> { root.Val };
    var queue = new Queue<TreeNode>();
    queue.Enqueue(root);
    while (queue.Count > 0)
    {
        var node = queue.Dequeue();
        foreach (var child in new[] { node.Left, node.Right })
        {
            values.Add(child?.Val);
            if (child != null) queue.Enqueue(child);
        }
    }
    while (values[^1] == null) values.RemoveAt(values.Count - 1);
    return [.. values];
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
