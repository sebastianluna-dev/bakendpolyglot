// 153 · Binary Strings With No Adjacent Ones
import java.util.ArrayList;
import java.util.List;

public class P153BinaryStringsWithNoAdjacentOnes {
    static List<String> noAdjacentOnes(int n) {
        List<String> result = new ArrayList<>();
        walk(n, new StringBuilder(), result);
        return result;
    }

    static void walk(int n, StringBuilder path, List<String> result) {
        if (path.length() == n) {
            result.add(path.toString());
            return;
        }
        path.append('0');
        walk(n, path, result);
        path.setLength(path.length() - 1);
        if (path.isEmpty() || path.charAt(path.length() - 1) == '0') {
            path.append('1');
            walk(n, path, result);
            path.setLength(path.length() - 1);
        }
    }

    public static void main(String[] args) {
        check(noAdjacentOnes(3).equals(List.of("000", "001", "010", "100", "101")));
        check(noAdjacentOnes(1).equals(List.of("0", "1")));
        check(noAdjacentOnes(0).equals(List.of("")));
        check(noAdjacentOnes(2).equals(List.of("00", "01", "10")));
        check(noAdjacentOnes(4).equals(List.of("0000", "0001", "0010", "0100", "0101", "1000", "1001", "1010")));
        check(noAdjacentOnes(10).size() == 144);
        System.out.println("153 ok");
    }

    static void check(boolean ok) {
        if (!ok) throw new AssertionError("test failed");
    }
}
