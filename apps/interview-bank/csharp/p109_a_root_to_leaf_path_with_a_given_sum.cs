// 109 · A Root-to-Leaf Path With a Given Sum
Check(HasPathSum(FromLevelOrder([5, 3, 8, 1, null, null, 4]), 9));
Check(!HasPathSum(FromLevelOrder([2, 7]), 2));
Check(!HasPathSum(FromLevelOrder([]), 0));
Check(HasPathSum(FromLevelOrder([5, 3, 8, 1, null, null, 4]), 17));
Check(!HasPathSum(FromLevelOrder([5, 3, 8, 1, null, null, 4]), 8));
Check(HasPathSum(FromLevelOrder([-3, 1, -2]), -5));
Console.WriteLine("109 ok");

static bool HasPathSum(TreeNode? root, int target)
{
    if (root == null) return false;
    if (root.Left == null && root.Right == null) return root.Val == target;
    var rest = target - root.Val;
    return HasPathSum(root.Left, rest) || HasPathSum(root.Right, rest);
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
