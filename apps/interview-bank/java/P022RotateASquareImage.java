// 022 · Rotate a Square Image
import java.util.Arrays;

public class P022RotateASquareImage {
    static void rotateImage(int[][] image) {
        int n = image.length;
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                int tmp = image[i][j];
                image[i][j] = image[j][i];
                image[j][i] = tmp;
            }
        }
        for (int[] row : image) {
            for (int l = 0, r = n - 1; l < r; l++, r--) {
                int tmp = row[l];
                row[l] = row[r];
                row[r] = tmp;
            }
        }
    }

    static int[][] rotated(int[][] image) {
        rotateImage(image);
        return image;
    }

    public static void main(String[] args) {
        check(Arrays.deepEquals(rotated(new int[][] {{1, 2}, {3, 4}}), new int[][] {{3, 1}, {4, 2}}));
        check(Arrays.deepEquals(
                rotated(new int[][] {{11, 12, 13}, {21, 22, 23}, {31, 32, 33}}),
                new int[][] {{31, 21, 11}, {32, 22, 12}, {33, 23, 13}}));
        check(Arrays.deepEquals(rotated(new int[0][]), new int[0][]));
        check(Arrays.deepEquals(rotated(new int[][] {{7}}), new int[][] {{7}}));
        check(Arrays.deepEquals(
                rotated(new int[][] {{1, 2, 3, 4}, {5, 6, 7, 8}, {9, 10, 11, 12}, {13, 14, 15, 16}}),
                new int[][] {{13, 9, 5, 1}, {14, 10, 6, 2}, {15, 11, 7, 3}, {16, 12, 8, 4}}));
        System.out.println("022 ok");
    }

    static void check(boolean ok) {
        if (!ok) throw new AssertionError("test failed");
    }
}
