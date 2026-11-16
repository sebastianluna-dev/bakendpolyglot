// 036 · Reverse the Words of a Sentence
Check(ReverseWords("seat 14 is free") == "free is 14 seat");
Check(ReverseWords("  return   the copy ") == "copy the return");
Check(ReverseWords("hold") == "hold");
Check(ReverseWords("") == "");
Check(ReverseWords("   ") == "");
Check(ReverseWords("a b c") == "c b a");
Console.WriteLine("036 ok");

static string ReverseWords(string sentence)
{
    var words = sentence.Split(' ', StringSplitOptions.RemoveEmptyEntries);
    Array.Reverse(words);
    return string.Join(" ", words);
}

static void Check(bool ok)
{
    if (!ok) throw new Exception("test failed");
}
