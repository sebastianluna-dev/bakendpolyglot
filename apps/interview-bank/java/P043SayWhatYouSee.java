// 043 · Say What You See
public class P043SayWhatYouSee {
    static String sayWhatYouSee(String start, int rounds) {
        String s = start;
        for (int r = 0; r < rounds; r++) {
            StringBuilder next = new StringBuilder();
            int i = 0;
            while (i < s.length()) {
                int j = i;
                while (j < s.length() && s.charAt(j) == s.charAt(i)) j++;
                next.append(j - i).append(s.charAt(i));
                i = j;
            }
            s = next.toString();
        }
        return s;
    }

    public static void main(String[] args) {
        check(sayWhatYouSee("1", 4).equals("111221"));
        check(sayWhatYouSee("3322251", 1).equals("23321511"));
        check(sayWhatYouSee("1", 0).equals("1"));
        check(sayWhatYouSee("22", 5).equals("22"));
        check(sayWhatYouSee("1111111111", 1).equals("101"));
        check(sayWhatYouSee("1", 9).equals("13211311123113112211"));
        System.out.println("043 ok");
    }

    static void check(boolean ok) {
        if (!ok) throw new AssertionError("test failed");
    }
}
