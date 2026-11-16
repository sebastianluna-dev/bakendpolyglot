// 075 · Simplify a File Path
Check(SimplifyPath("/shelfwise//covers/./2024/../thumbs/") == "/shelfwise/covers/thumbs");
Check(SimplifyPath("/../a/.../b/..") == "/a/...");
Check(SimplifyPath("/") == "/");
Check(SimplifyPath("/../..") == "/");
Check(SimplifyPath("/a//b////c/d//././/..") == "/a/b/c");
Check(SimplifyPath("/.hidden/..x/.") == "/.hidden/..x");
Console.WriteLine("075 ok");

static string SimplifyPath(string path)
{
    var names = new List<string>(); // a List, not a Stack: Stack<T> enumerates from the top
    foreach (var part in path.Split('/'))
    {
        if (part == "..")
        {
            if (names.Count > 0) names.RemoveAt(names.Count - 1);
        }
        else if (part != "" && part != ".")
        {
            names.Add(part);
        }
    }
    return "/" + string.Join("/", names);
}

static void Check(bool ok)
{
    if (!ok) throw new Exception("test failed");
}
