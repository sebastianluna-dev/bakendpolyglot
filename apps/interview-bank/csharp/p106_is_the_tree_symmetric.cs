// 106 · Is the Tree Symmetric?
Check(IsSymmetric(FromLevelOrder([7, 4, 4, 1, 9, 9, 1])));
Check(!IsSymmetric(FromLevelOrder([5, 3, 3, 8, null, null, 2])));
Check(IsSymmetric(FromLevelOrder([])));
Check(IsSymmetric(FromLevelOrder([1])));
Check(!IsSymmetric(FromLevelOrder([2, 3, 3, null, 4, null, 4])));
Check(!IsSymmetric(FromLevelOrder([1, 2, 3])));
Console.WriteLine("106 ok");

static bool IsSymmetric(TreeNode? root) => root == null || IsMirror(root.Left, root.Right);

static bool IsMirror(TreeNode? a, TreeNode? b)
{
    if (a == null || b == null) return a == b;
    return a.Val == b.Val && IsMirror(a.Left, b.Right) && IsMirror(a.Right, b.Left);
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
