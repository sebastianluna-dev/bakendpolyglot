// 044 · Fully Justify Text
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class P044FullyJustifyText {
    static List<String> justifyText(String[] words, int width) {
        List<String> lines = new ArrayList<>();
        int i = 0;
        while (i < words.length) {
            int j = i + 1;
            int used = words[i].length(); // the words plus one space between each pair
            while (j < words.length && used + 1 + words[j].length() <= width) {
                used += 1 + words[j].length();
                j++;
            }
            int gaps = j - i - 1;
            StringBuilder line = new StringBuilder();
            if (j == words.length || gaps == 0) {
                line.append(String.join(" ", Arrays.copyOfRange(words, i, j)));
                line.append(" ".repeat(width - line.length()));
            } else {
                int spaces = width - (used - gaps);
                int each = spaces / gaps;
                int extra = spaces % gaps;
                for (int k = i; k < j - 1; k++) {
                    line.append(words[k]).append(" ".repeat(each + (k - i < extra ? 1 : 0)));
                }
                line.append(words[j - 1]);
            }
            lines.add(line.toString());
            i = j;
        }
        return lines;
    }

    public static void main(String[] args) {
        check(justifyText(new String[] {"Your", "loan", "of", "Dune", "is", "due", "on", "Friday."}, 16)
                .equals(List.of("Your   loan   of", "Dune  is  due on", "Friday.         ")));
        check(justifyText(new String[] {"Hold", "expires", "in", "ten", "minutes"}, 10)
                .equals(List.of("Hold      ", "expires in", "ten       ", "minutes   ")));
        check(justifyText(new String[] {"to", "be", "or", "not"}, 6).equals(List.of("to  be", "or not")));
        check(justifyText(new String[] {"exact"}, 5).equals(List.of("exact")));
        check(justifyText(new String[0], 8).isEmpty());
        System.out.println("044 ok");
    }

    static void check(boolean ok) {
        if (!ok) throw new AssertionError("test failed");
    }
}
