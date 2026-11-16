// 117 · Nodes No Smaller Than Their Ancestors
Check(CountGoodNodes(FromLevelOrder([5, 3, 8, 6, 2, 5, 9])) == 4);
Check(CountGoodNodes(FromLevelOrder([2, 2, 2])) == 3);
Check(CountGoodNodes(FromLevelOrder([])) == 0);
Check(CountGoodNodes(FromLevelOrder([7])) == 1);
Check(CountGoodNodes(FromLevelOrder([9, 8, null, 7])) == 1);
Check(CountGoodNodes(FromLevelOrder([-5, -10, -1])) == 2);
Console.WriteLine("117 ok");

static int CountGoodNodes(TreeNode? root) => root == null ? 0 : CountFrom(root, root.Val);

// highest is the largest value on the path from the root down to node's parent
static int CountFrom(TreeNode? node, int highest)
{
    if (node == null) return 0;
    var good = node.Val >= highest ? 1 : 0;
    var next = Math.Max(highest, node.Val);
    return good + CountFrom(node.Left, next) + CountFrom(node.Right, next);
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
