// 129 · Clone a Graph
CheckClone([[1, 3], [0, 2], [1, 3], [0, 2]]);
CheckClone([[]]);
CheckClone([]);
CheckClone([[1], [0]]);
CheckClone([[1, 2], [0, 2], [0, 1, 3], [2]]);
Console.WriteLine("129 ok");

static GraphNode? CloneGraph(GraphNode? node)
{
    if (node == null) return null;
    var copyOf = new Dictionary<GraphNode, GraphNode> { [node] = new GraphNode(node.Val) }; // original -> copy
    var queue = new Queue<GraphNode>();
    queue.Enqueue(node);
    while (queue.Count > 0)
    {
        var current = queue.Dequeue();
        foreach (var nb in current.Neighbors)
        {
            if (!copyOf.ContainsKey(nb))
            {
                copyOf[nb] = new GraphNode(nb.Val);
                queue.Enqueue(nb);
            }
            copyOf[current].Neighbors.Add(copyOf[nb]);
        }
    }
    return copyOf[node];
}

// Node i has value i and the neighbours adjacency[i]; returns node 0.
static GraphNode? Build(int[][] adjacency)
{
    var nodes = adjacency.Select((_, i) => new GraphNode(i)).ToArray();
    for (var i = 0; i < nodes.Length; i++) nodes[i].Neighbors.AddRange(adjacency[i].Select(j => nodes[j]));
    return nodes.Length > 0 ? nodes[0] : null;
}

static HashSet<GraphNode> AllNodes(GraphNode? node)
{
    var found = new HashSet<GraphNode>();
    if (node == null) return found;
    found.Add(node);
    var queue = new Queue<GraphNode>();
    queue.Enqueue(node);
    while (queue.Count > 0)
    {
        foreach (var nb in queue.Dequeue().Neighbors)
        {
            if (found.Add(nb)) queue.Enqueue(nb);
        }
    }
    return found;
}

static int[][] ToAdjacency(GraphNode? node)
{
    var nodes = AllNodes(node);
    var adjacency = new int[nodes.Count][];
    foreach (var n in nodes) adjacency[n.Val] = [.. n.Neighbors.Select(nb => nb.Val)];
    return adjacency;
}

static void CheckClone(int[][] adjacency)
{
    var original = Build(adjacency);
    var copy = CloneGraph(original);
    var got = ToAdjacency(copy);
    Check(got.Length == adjacency.Length && got.Zip(adjacency).All(p => p.First.SequenceEqual(p.Second)));
    Check(!AllNodes(copy).Overlaps(AllNodes(original)));
}

static void Check(bool ok)
{
    if (!ok) throw new Exception("test failed");
}

class GraphNode(int val)
{
    public int Val = val;
    public List<GraphNode> Neighbors = [];
}
