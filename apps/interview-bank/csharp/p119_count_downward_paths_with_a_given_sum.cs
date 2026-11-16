// 119 · Count Downward Paths With a Given Sum
Check(CountPathsWithSum(FromLevelOrder([4, 2, -1, 1, 3, null, 5]), 5) == 2);
Check(CountPathsWithSum(FromLevelOrder([1, -1, 1, 1]), 1) == 4);
Check(CountPathsWithSum(FromLevelOrder([]), 0) == 0);
Check(CountPathsWithSum(FromLevelOrder([0, 0, null, 0]), 0) == 6);
Check(CountPathsWithSum(FromLevelOrder([1000000000, null, 1000000000]), 2000000000) == 1);
Check(CountPathsWithSum(FromLevelOrder([1000000000, null, 1000000000]), -294967296) == 0);
Console.WriteLine("119 ok");

static int CountPathsWithSum(TreeNode? root, int target)
{
    var seen = new Dictionary<long, int> { [0] = 1 }; // prefix sums on the current root path -> count
    int Visit(TreeNode? node, long prefix)
    {
        if (node == null) return 0;
        var total = prefix + node.Val;
        var count = seen.GetValueOrDefault(total - target);
        seen[total] = seen.GetValueOrDefault(total) + 1;
        count += Visit(node.Left, total) + Visit(node.Right, total);
        seen[total]--;
        return count;
    }
    return Visit(root, 0);
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
