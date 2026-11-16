// 111 · Level-by-Level Traversal
Check(SameLevels(LevelOrder(FromLevelOrder([8, 4, 12, 2, null, 10, 14])), [[8], [4, 12], [2, 10, 14]]));
Check(SameLevels(LevelOrder(FromLevelOrder([5, 6, null, 7, 8])), [[5], [6], [7, 8]]));
Check(LevelOrder(FromLevelOrder([])).Length == 0);
Check(SameLevels(LevelOrder(FromLevelOrder([9])), [[9]]));
Check(SameLevels(LevelOrder(FromLevelOrder([1, 2, 3, null, 4, 5, null, null, null, 6])), [[1], [2, 3], [4, 5], [6]]));
Console.WriteLine("111 ok");

static int[][] LevelOrder(TreeNode? root)
{
    var levels = new List<int[]>();
    List<TreeNode> level = root == null ? [] : [root];
    while (level.Count > 0)
    {
        var next = new List<TreeNode>();
        levels.Add(level.Select(node => node.Val).ToArray());
        foreach (var node in level)
        {
            if (node.Left != null) next.Add(node.Left);
            if (node.Right != null) next.Add(node.Right);
        }
        level = next;
    }
    return [.. levels];
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

static bool SameLevels(int[][] a, int[][] b) =>
    a.Length == b.Length && a.Zip(b).All(p => p.First.SequenceEqual(p.Second));

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
