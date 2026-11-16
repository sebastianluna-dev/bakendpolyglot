// 114 · The k-th Smallest Key in a BST
Check(KthSmallest(FromLevelOrder([20, 10, 30, 5, 15, 25, 40]), 3) == 15);
Check(KthSmallest(FromLevelOrder([6, 3, 8, 1, 4, null, 9, null, 2]), 2) == 2);
Check(KthSmallest(FromLevelOrder([6, 3, 8, 1, 4, null, 9, null, 2]), 7) == 9);
Check(KthSmallest(FromLevelOrder([42]), 1) == 42);
Check(KthSmallest(FromLevelOrder([1, null, 2, null, 3]), 3) == 3);
Check(KthSmallest(FromLevelOrder([3, 2, null, 1]), 1) == 1);
Console.WriteLine("114 ok");

static int KthSmallest(TreeNode? root, int k)
{
    var stack = new Stack<TreeNode>();
    var node = root;
    while (true)
    {
        while (node != null)
        {
            stack.Push(node);
            node = node.Left;
        }
        var smallest = stack.Pop(); // the smallest key not yet counted
        k--;
        if (k == 0) return smallest.Val;
        node = smallest.Right;
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
