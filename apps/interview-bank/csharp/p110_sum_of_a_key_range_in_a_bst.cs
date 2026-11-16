// 110 · Sum of a Key Range in a BST
Check(RangeSum(FromLevelOrder([20, 10, 30, 5, 15, 25, 40]), 12, 27) == 60);
Check(RangeSum(FromLevelOrder([8, 3, 12, null, 6]), 9, 11) == 0);
Check(RangeSum(FromLevelOrder([]), 1, 100) == 0);
Check(RangeSum(FromLevelOrder([7]), 7, 7) == 7);
Check(RangeSum(FromLevelOrder([20, 10, 30, 5, 15, 25, 40]), 1, 100) == 145);
Check(RangeSum(FromLevelOrder([20, 10, 30, 5, 15, 25, 40]), 10, 20) == 45);
Console.WriteLine("110 ok");

static int RangeSum(TreeNode? root, int lo, int hi)
{
    if (root == null) return 0;
    if (root.Val < lo) return RangeSum(root.Right, lo, hi); // the left subtree is even smaller
    if (root.Val > hi) return RangeSum(root.Left, lo, hi);
    return root.Val + RangeSum(root.Left, lo, hi) + RangeSum(root.Right, lo, hi);
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
