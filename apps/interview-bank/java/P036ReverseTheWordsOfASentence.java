// 036 · Reverse the Words of a Sentence
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class P036ReverseTheWordsOfASentence {
    static String reverseWords(String sentence) {
        List<String> words = new ArrayList<>();
        for (String w : sentence.split(" ")) {
            if (!w.isEmpty()) words.add(w); // split keeps the empty pieces before leading spaces
        }
        Collections.reverse(words);
        return String.join(" ", words);
    }

    public static void main(String[] args) {
        check(reverseWords("seat 14 is free").equals("free is 14 seat"));
        check(reverseWords("  return   the copy ").equals("copy the return"));
        check(reverseWords("hold").equals("hold"));
        check(reverseWords("").equals(""));
        check(reverseWords("   ").equals(""));
        check(reverseWords("a b c").equals("c b a"));
        System.out.println("036 ok");
    }

    static void check(boolean ok) {
        if (!ok) throw new AssertionError("test failed");
    }
}
