// 026 · Palindrome, Ignoring Punctuation
Check(IsPalindrome("Step on no pets!"));
Check(!IsPalindrome("Pets on no steps"));
Check(IsPalindrome(""));
Check(IsPalindrome(",.! ?"));
Check(IsPalindrome("a."));
Check(IsPalindrome("Ab1bA"));
Check(!IsPalindrome("1a2"));
Check(!IsPalindrome("0P"));
Console.WriteLine("026 ok");

static bool IsPalindrome(string text)
{
    int left = 0, right = text.Length - 1;
    while (left < right)
    {
        if (!char.IsLetterOrDigit(text[left])) left++;
        else if (!char.IsLetterOrDigit(text[right])) right--;
        else if (char.ToLowerInvariant(text[left]) != char.ToLowerInvariant(text[right])) return false;
        else
        {
            left++;
            right--;
        }
    }
    return true;
}

static void Check(bool ok)
{
    if (!ok) throw new Exception("test failed");
}
