// 115 · Lowest Common Manager
int?[] chart = [1, 2, 3, 4, 5, 6, 7, null, null, 8, 9];
Check(LowestCommonManager(FromLevelOrder(chart), 8, 4) == 2);
Check(LowestCommonManager(FromLevelOrder(chart), 9, 7) == 1);
Check(LowestCommonManager(FromLevelOrder(chart), 5, 9) == 5);
Check(LowestCommonManager(FromLevelOrder(chart), 6, 6) == 6);
Check(LowestCommonManager(FromLevelOrder(chart), 8, 9) == 5);
Check(LowestCommonManager(FromLevelOrder([1, 2]), 2, 1) == 1);
Console.WriteLine("115 ok");

static int LowestCommonManager(TreeNode? root, int a, int b) => Lowest(root, a, b)!.Val;

// returns the answer if both ids are below node, else whichever of the two is below it, else null
static TreeNode? Lowest(TreeNode? node, int a, int b)
{
    if (node == null || node.Val == a || node.Val == b) return node;
    var left = Lowest(node.Left, a, b);
    var right = Lowest(node.Right, a, b);
    if (left != null && right != null) return node;
    return left ?? right;
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
