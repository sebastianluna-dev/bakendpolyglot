// 075 · Simplify a File Path
import java.util.ArrayDeque;
import java.util.Deque;

public class P075SimplifyAFilePath {
    static String simplifyPath(String path) {
        Deque<String> names = new ArrayDeque<>(); // pushed and popped at the back, so it joins in order
        for (String part : path.split("/")) {
            if (part.equals("..")) {
                if (!names.isEmpty()) names.pollLast();
            } else if (!part.isEmpty() && !part.equals(".")) {
                names.offerLast(part);
            }
        }
        return "/" + String.join("/", names);
    }

    public static void main(String[] args) {
        check(simplifyPath("/shelfwise//covers/./2024/../thumbs/").equals("/shelfwise/covers/thumbs"));
        check(simplifyPath("/../a/.../b/..").equals("/a/..."));
        check(simplifyPath("/").equals("/"));
        check(simplifyPath("/../..").equals("/"));
        check(simplifyPath("/a//b////c/d//././/..").equals("/a/b/c"));
        check(simplifyPath("/.hidden/..x/.").equals("/.hidden/..x"));
        System.out.println("075 ok");
    }

    static void check(boolean ok) {
        if (!ok) throw new AssertionError("test failed");
    }
}
