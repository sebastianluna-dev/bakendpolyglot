// 183 · Make Change at the Box Office
public class P183MakeChangeAtTheBoxOffice {
    static boolean canMakeChange(int[] bills) {
        int fives = 0, tens = 0;
        for (int bill : bills) {
            if (bill == 5) {
                fives++;
            } else if (bill == 10) {
                if (fives == 0) return false;
                fives--;
                tens++;
            } else if (tens > 0 && fives > 0) {
                // a 10 can only ever change a 20, so spend it before the fives
                tens--;
                fives--;
            } else if (fives >= 3) {
                fives -= 3;
            } else {
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) {
        check(canMakeChange(new int[] {5, 5, 10, 5, 20}));
        check(!canMakeChange(new int[] {5, 10, 10, 20}));
        check(canMakeChange(new int[0]));
        check(!canMakeChange(new int[] {10}));
        check(!canMakeChange(new int[] {5, 5, 20}));
        check(canMakeChange(new int[] {5, 5, 5, 20}));
        check(canMakeChange(new int[] {5, 5, 5, 5, 10, 20, 10}));
        System.out.println("183 ok");
    }

    static void check(boolean ok) {
        if (!ok) throw new AssertionError("test failed");
    }
}
