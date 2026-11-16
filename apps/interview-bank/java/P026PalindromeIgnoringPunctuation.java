// 026 · Palindrome, Ignoring Punctuation

public class P026PalindromeIgnoringPunctuation {
    static boolean isPalindrome(String text) {
        int left = 0, right = text.length() - 1;
        while (left < right) {
            char a = text.charAt(left), b = text.charAt(right);
            if (!Character.isLetterOrDigit(a)) left++;
            else if (!Character.isLetterOrDigit(b)) right--;
            else if (Character.toLowerCase(a) != Character.toLowerCase(b)) return false;
            else {
                left++;
                right--;
            }
        }
        return true;
    }

    public static void main(String[] args) {
        check(isPalindrome("Step on no pets!"));
        check(!isPalindrome("Pets on no steps"));
        check(isPalindrome(""));
        check(isPalindrome(",.! ?"));
        check(isPalindrome("a."));
        check(isPalindrome("Ab1bA"));
        check(!isPalindrome("1a2"));
        check(!isPalindrome("0P"));
        System.out.println("026 ok");
    }

    static void check(boolean ok) {
        if (!ok) throw new AssertionError("test failed");
    }
}
