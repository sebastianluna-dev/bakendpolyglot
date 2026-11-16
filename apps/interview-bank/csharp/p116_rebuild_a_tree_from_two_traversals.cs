// 116 · Rebuild a Tree From Two Traversals
Check(ToLevelOrder(BuildTree([8, 4, 2, 6, 12, 14], [2, 4, 6, 8, 12, 14])).SequenceEqual([8, 4, 12, 2, 6, null, 14]));
Check(ToLevelOrder(BuildTree([1, 2, 3], [3, 2, 1])).SequenceEqual([1, 2, null, 3]));
Check(ToLevelOrder(BuildTree([], [])).Length == 0);
Check(ToLevelOrder(BuildTree([5], [5])).SequenceEqual([5]));
Check(ToLevelOrder(BuildTree([1, 2, 3], [1, 2, 3])).SequenceEqual([1, null, 2, null, 3]));
Check(ToLevelOrder(BuildTree([-1, -3, 0], [-3, -1, 0])).SequenceEqual([-1, -3, 0]));
Console.WriteLine("116 ok");

static TreeNode? BuildTree(int[] preorder, int[] inorder)
{
    var position = new Dictionary<int, int>();
    for (var i = 0; i < inorder.Length; i++) position[inorder[i]] = i;

    // builds the subtree whose preorder starts at pre and whose inorder is inorder[lo..hi]
    TreeNode? Build(int pre, int lo, int hi)
    {
        if (lo > hi) return null;
        var root = new TreeNode(preorder[pre]);
        var mid = position[root.Val];
        root.Left = Build(pre + 1, lo, mid - 1);
        root.Right = Build(pre + 1 + (mid - lo), mid + 1, hi);
        return root;
    }
    return Build(0, 0, inorder.Length - 1);
}

static int?[] ToLevelOrder(TreeNode? root)
{
    if (root == null) return [];
    var values = new List<int?> { root.Val };
    var queue = new Queue<TreeNode>();
    queue.Enqueue(root);
    while (queue.Count > 0)
    {
        var node = queue.Dequeue();
        foreach (var child in new[] { node.Left, node.Right })
        {
            values.Add(child?.Val);
            if (child != null) queue.Enqueue(child);
        }
    }
    while (values[^1] == null) values.RemoveAt(values.Count - 1);
    return [.. values];
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
