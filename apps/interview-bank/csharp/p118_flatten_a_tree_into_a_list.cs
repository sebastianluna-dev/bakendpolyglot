// 118 · Flatten a Tree Into a List
Check(Flattened([10, 5, 20, 3, 7, null, 25]).SequenceEqual([10, null, 5, null, 3, null, 7, null, 20, null, 25]));
Check(Flattened([1, 2, null, 3]).SequenceEqual([1, null, 2, null, 3]));
Check(Flattened([]).Length == 0);
Check(Flattened([8]).SequenceEqual([8]));
Check(Flattened([1, null, 2]).SequenceEqual([1, null, 2]));
Check(Flattened([1, 2, 3, null, null, 4]).SequenceEqual([1, null, 2, null, 3, null, 4]));
Console.WriteLine("118 ok");

static void Flatten(TreeNode? root)
{
    var node = root;
    while (node != null)
    {
        if (node.Left != null)
        {
            var tail = node.Left; // the last node of the left subtree in preorder
            while (tail.Right != null) tail = tail.Right;
            tail.Right = node.Right;
            node.Right = node.Left;
            node.Left = null;
        }
        node = node.Right;
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

static int?[] Flattened(int?[] values)
{
    var root = FromLevelOrder(values);
    Flatten(root);
    return ToLevelOrder(root);
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
