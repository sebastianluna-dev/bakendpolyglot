// 040 · Pack and Unpack a List of Strings
import java.util.ArrayList;
import java.util.List;

public class P040PackAndUnpackAListOfStrings {
    static String pack(List<String> words) {
        StringBuilder result = new StringBuilder();
        for (String w : words) result.append(w.length()).append('#').append(w);
        return result.toString();
    }

    static List<String> unpack(String packed) {
        List<String> words = new ArrayList<>();
        int i = 0;
        while (i < packed.length()) {
            int length = 0;
            while (packed.charAt(i) != '#') {
                length = length * 10 + (packed.charAt(i) - '0');
                i++;
            }
            i++; // skip the '#'
            words.add(packed.substring(i, i + length));
            i += length;
        }
        return words;
    }

    record Case(List<String> words, String packed) {}

    public static void main(String[] args) {
        List<Case> cases = List.of(
                new Case(List.of("truck 7", "", "door#2"), "7#truck 70#6#door#2"),
                new Case(List.of("parcel-12345"), "12#parcel-12345"),
                new Case(List.of(), ""),
                new Case(List.of(""), "0#"),
                new Case(List.of("#", "12#", "a b", "3#x"), "1##3#12#3#a b3#3#x"));
        for (Case c : cases) {
            check(pack(c.words()).equals(c.packed()));
            check(unpack(c.packed()).equals(c.words()));
        }
        System.out.println("040 ok");
    }

    static void check(boolean ok) {
        if (!ok) throw new AssertionError("test failed");
    }
}
