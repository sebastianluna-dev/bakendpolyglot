// 052 · Compare Strings With Backspaces
public class P052CompareStringsWithBackspaces {
    static boolean sameAfterBackspaces(String a, String b) {
        int i = a.length() - 1, j = b.length() - 1;
        while (i >= 0 || j >= 0) {
            i = lastKept(a, i);
            j = lastKept(b, j);
            if (i < 0 || j < 0) return i < 0 && j < 0;
            if (a.charAt(i) != b.charAt(j)) return false;
            i--;
            j--;
        }
        return true;
    }

    // Index of the last character at or before i that no later '#' deletes, or -1.
    static int lastKept(String s, int i) {
        int skip = 0;
        while (i >= 0) {
            if (s.charAt(i) == '#') skip++;
            else if (skip > 0) skip--;
            else break;
            i--;
        }
        return i;
    }

    public static void main(String[] args) {
        check(sameAfterBackspaces("A1#2", "A2"));
        check(!sameAfterBackspaces("B7##C", "C#B"));
        check(sameAfterBackspaces("##", ""));
        check(sameAfterBackspaces("ab##", "c#d#"));
        check(sameAfterBackspaces("x#y", "y"));
        check(!sameAfterBackspaces("abc", "ab"));
        check(!sameAfterBackspaces("a", "ba"));
        check(sameAfterBackspaces("a#b#c", "#c"));
        System.out.println("052 ok");
    }

    static void check(boolean ok) {
        if (!ok) throw new AssertionError("test failed");
    }
}
