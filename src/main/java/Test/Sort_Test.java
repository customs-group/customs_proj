package Test;

import util.Sort;

/**
 * Created by edwardlol on 15/11/19.
 */
public class Sort_Test {



    public static void main(String[] args) {
        double[] s = {2.0, 1.0, 4.0, 9.0, 3.0, 5.0, 7.0, 8.0, 6.0};
        System.out.print("original s: ");
        for (double _s : s) {
            System.out.print(Double.toString(_s) + " ");
        }
        System.out.println();
        Sort.set_s(s);
        Sort.quick_sort(0, s.length - 1);
        s = Sort.get_s();
        System.out.print("sorted s: ");
        for (double _s : s) {
            System.out.print(Double.toString(_s) + " ");
        }
    }
}
