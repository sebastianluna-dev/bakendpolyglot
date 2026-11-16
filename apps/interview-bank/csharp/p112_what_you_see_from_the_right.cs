// 112 · What You See From the Right
Check(RightSideView(FromLevelOrder([10, 4, 15, 2, 6, null, 20, null, null, 5])).SequenceEqual([10, 15, 20, 5]));
Check(RightSideView(FromLevelOrder([1, 2])).SequenceEqual([1, 2]));
Check(RightSideView(FromLevelOrder([])).Count == 0);
Check(RightSideView(FromLevelOrder([4])).SequenceEqual([4]));
Check(RightSideView(FromLevelOrder([1, 2, null, 3])).SequenceEqual([1, 2, 3]));
Check(RightSideView(FromLevelOrder([1, 2, 3, 4, 5, 6, 7])).SequenceEqual([1, 3, 7]));
Console.WriteLine("112 ok");

static List<int> RightSideView(TreeNode? root)
{
    var view = new List<int>();
    void Visit(TreeNode? node, int depth)
    {
        if (node == null) return;
        if (depth == view.Count) view.Add(node.Val); // first node reached at this depth
        Visit(node.Right, depth + 1);
        Visit(node.Left, depth + 1);
    }
    Visit(root, 0);
    return view;
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
