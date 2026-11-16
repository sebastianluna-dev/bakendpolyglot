// 113 · Validate a Binary Search Tree
Check(IsSearchTree(FromLevelOrder([8, 4, 12, 2, 6, 10, 14])));
Check(!IsSearchTree(FromLevelOrder([8, 4, 12, 2, 9, 10, 14])));
Check(IsSearchTree(FromLevelOrder([])));
Check(!IsSearchTree(FromLevelOrder([5, 5])));
Check(IsSearchTree(FromLevelOrder([int.MinValue, null, int.MaxValue])));
Check(!IsSearchTree(FromLevelOrder([int.MaxValue, int.MaxValue])));
Check(!IsSearchTree(FromLevelOrder([int.MinValue, int.MinValue])));
Console.WriteLine("113 ok");

static bool IsSearchTree(TreeNode? root) => Within(root, null, null);

// lo and hi are the nearest ancestors the key must stay above and below; null means no bound
static bool Within(TreeNode? node, TreeNode? lo, TreeNode? hi)
{
    if (node == null) return true;
    if (lo != null && node.Val <= lo.Val) return false;
    if (hi != null && node.Val >= hi.Val) return false;
    return Within(node.Left, lo, node) && Within(node.Right, node, hi);
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
