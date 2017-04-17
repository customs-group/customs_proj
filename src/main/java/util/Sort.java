package util;

/**
 *
 * Created by edwardlol on 15/11/19.
 */
public class Sort {
    private static double[] s;

    public static void set_s(double[] _s) {
        s = _s;
    }
    public static double[] get_s() {
        return s;
    }

    // 快排
    public static void quick_sort(int left, int right) {
        if (left < right) {
            int i = left, j = right;
            double x = s[left];
            while (i < j) {
                // 从右向左找第一个小于x的数
                while (i < j && s[j] >= x) {
                    j--;
                }
                if (i < j) {
                    s[i++] = s[j];
                }
                // 从左向右找第一个大于等于x的数
                while (i < j && s[i] < x) {
                    i++;
                }
                if (i < j) {
                    s[j--] = s[i];
                }
            }
            s[i] = x;
            quick_sort(left, i - 1); // 递归调用
            quick_sort(i + 1, right);
        }
    }


}
