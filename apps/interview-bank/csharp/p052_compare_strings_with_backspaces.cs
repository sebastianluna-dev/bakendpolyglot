// 052 · Compare Strings With Backspaces
Check(SameAfterBackspaces("A1#2", "A2"));
Check(!SameAfterBackspaces("B7##C", "C#B"));
Check(SameAfterBackspaces("##", ""));
Check(SameAfterBackspaces("ab##", "c#d#"));
Check(SameAfterBackspaces("x#y", "y"));
Check(!SameAfterBackspaces("abc", "ab"));
Check(!SameAfterBackspaces("a", "ba"));
Check(SameAfterBackspaces("a#b#c", "#c"));
Console.WriteLine("052 ok");

static bool SameAfterBackspaces(string a, string b)
{
    int i = a.Length - 1, j = b.Length - 1;
    while (i >= 0 || j >= 0)
    {
        i = LastKept(a, i);
        j = LastKept(b, j);
        if (i < 0 || j < 0) return i < 0 && j < 0;
        if (a[i] != b[j]) return false;
        i--;
        j--;
    }
    return true;
}

// Index of the last character at or before i that no later '#' deletes, or -1.
static int LastKept(string s, int i)
{
    var skip = 0;
    while (i >= 0)
    {
        if (s[i] == '#') skip++;
        else if (skip > 0) skip--;
        else break;
        i--;
    }
    return i;
}

static void Check(bool ok)
{
    if (!ok) throw new Exception("test failed");
}
