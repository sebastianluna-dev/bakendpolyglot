// 194 · Parse a Query String
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class P194ParseAQueryString {
    static Map<String, List<String>> parseQuery(String query) {
        Map<String, List<String>> params = new HashMap<>();
        for (String piece : query.split("&")) {
            String[] parts = piece.split("=", 2); // split at the first = only
            String key = decode(parts[0]);
            if (key.isEmpty()) continue;
            String value = parts.length == 2 ? decode(parts[1]) : "";
            params.computeIfAbsent(key, k -> new ArrayList<>()).add(value);
        }
        return params;
    }

    static String decode(String s) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == '+') bytes.write(' ');
            else if (c == '%' && i + 2 < s.length() && hexValue(s.charAt(i + 1)) >= 0 && hexValue(s.charAt(i + 2)) >= 0) {
                bytes.write(hexValue(s.charAt(i + 1)) * 16 + hexValue(s.charAt(i + 2)));
                i += 2;
            } else bytes.write(c); // a malformed % stays as it is
        }
        return bytes.toString(StandardCharsets.UTF_8); // %XX escapes are bytes; several can make one character
    }

    static int hexValue(char c) {
        if (c >= '0' && c <= '9') return c - '0';
        if (c >= 'a' && c <= 'f') return c - 'a' + 10;
        if (c >= 'A' && c <= 'F') return c - 'A' + 10;
        return -1;
    }

    public static void main(String[] args) {
        check(parseQuery("genre=sci-fi&sort=title&genre=history")
                .equals(Map.of("genre", List.of("sci-fi", "history"), "sort", List.of("title"))));
        check(parseQuery("q=war+%26+peace&note=50%25&empty=&flag")
                .equals(Map.of("q", List.of("war & peace"), "note", List.of("50%"), "empty", List.of(""), "flag", List.of(""))));
        check(parseQuery("").equals(Map.of()));
        check(parseQuery("a=1&&b=2&").equals(Map.of("a", List.of("1"), "b", List.of("2"))));
        check(parseQuery("=5&k=v").equals(Map.of("k", List.of("v"))));
        check(parseQuery("expr=a=b").equals(Map.of("expr", List.of("a=b"))));
        check(parseQuery("name=Jos%C3%A9&price=%E2%82%AC5").equals(Map.of("name", List.of("José"), "price", List.of("€5"))));
        check(parseQuery("bad=%zz%4&tail=%").equals(Map.of("bad", List.of("%zz%4"), "tail", List.of("%"))));
        check(parseQuery("sort%5Bby%5D=title&path=%2fa%2F").equals(Map.of("sort[by]", List.of("title"), "path", List.of("/a/"))));
        check(parseQuery("plus=%2B+").equals(Map.of("plus", List.of("+ "))));
        System.out.println("194 ok");
    }

    static void check(boolean ok) {
        if (!ok) throw new AssertionError("test failed");
    }
}
