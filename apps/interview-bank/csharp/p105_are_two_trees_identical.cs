// 105 · Are Two Trees Identical?
Check(AreIdentical(FromLevelOrder([4, 2, 6]), FromLevelOrder([4, 2, 6])));
Check(!AreIdentical(FromLevelOrder([4, 2]), FromLevelOrder([4, null, 2])));
Check(AreIdentical(FromLevelOrder([]), FromLevelOrder([])));
Check(!AreIdentical(FromLevelOrder([1]), FromLevelOrder([])));
Check(!AreIdentical(FromLevelOrder([1, 2, 3]), FromLevelOrder([1, 2, 4])));
Check(AreIdentical(FromLevelOrder([5, null, 8, 7]), FromLevelOrder([5, null, 8, 7])));
Console.WriteLine("105 ok");

static bool AreIdentical(TreeNode? a, TreeNode? b)
{
    if (a == null || b == null) return a == b;
    return a.Val == b.Val && AreIdentical(a.Left, b.Left) && AreIdentical(a.Right, b.Right);
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
