// 033 · Is It a Rotation?
Check(IsRotation("parcel", "celpar"));
Check(!IsRotation("parcel", "parcle"));
Check(IsRotation("abc", "abc"));
Check(IsRotation("", ""));
Check(!IsRotation("aa", "a"));
Check(IsRotation("abab", "baba"));
Console.WriteLine("033 ok");

static bool IsRotation(string a, string b)
{
    return a.Length == b.Length && (a + a).Contains(b);
}

static void Check(bool ok)
{
    if (!ok) throw new Exception("test failed");
}
