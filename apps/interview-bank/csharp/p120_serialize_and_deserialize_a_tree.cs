// 120 · Serialize and Deserialize a Tree
Check(Serialize(FromLevelOrder([7, 3, 9, null, 4, 8])) == "7,3,#,4,#,#,9,8,#,#,#");
Check(Serialize(FromLevelOrder([])) == "#");
Check(ToLevelOrder(Deserialize("5,-2,#,#,#")).SequenceEqual([5, -2]));
int?[][] trees = [[7, 3, 9, null, 4, 8], [], [-1000], [1, null, 2, null, 3], [10, -20, 30, -40, 50, -60, 70]];
foreach (var tree in trees)
{
    Check(ToLevelOrder(Deserialize(Serialize(FromLevelOrder(tree)))).SequenceEqual(tree));
}
Console.WriteLine("120 ok");

static string Serialize(TreeNode? root)
{
    var tokens = new List<string>();
    void Write(TreeNode? node)
    {
        if (node == null)
        {
            tokens.Add("#");
            return;
        }
        tokens.Add(node.Val.ToString());
        Write(node.Left);
        Write(node.Right);
    }
    Write(root);
    return string.Join(",", tokens);
}

static TreeNode? Deserialize(string data)
{
    var tokens = data.Split(',');
    var next = 0; // index of the next token to read
    TreeNode? Read()
    {
        var token = tokens[next++];
        if (token == "#") return null;
        var node = new TreeNode(int.Parse(token));
        node.Left = Read();
        node.Right = Read();
        return node;
    }
    return Read();
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
