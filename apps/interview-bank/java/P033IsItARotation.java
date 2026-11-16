// 033 · Is It a Rotation?
public class P033IsItARotation {
    static boolean isRotation(String a, String b) {
        return a.length() == b.length() && (a + a).contains(b);
    }

    public static void main(String[] args) {
        check(isRotation("parcel", "celpar"));
        check(!isRotation("parcel", "parcle"));
        check(isRotation("abc", "abc"));
        check(isRotation("", ""));
        check(!isRotation("aa", "a"));
        check(isRotation("abab", "baba"));
        System.out.println("033 ok");
    }

    static void check(boolean ok) {
        if (!ok) throw new AssertionError("test failed");
    }
}
