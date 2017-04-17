package util;

/**
 *
 * Created by edwardlol on 15/11/24.
 */
// TODO: 17-4-17 simplify this stupid class
public class EdMath {

    /**
     * calculate mean value of a double array
     * @param data source data
     * @return mean of data
     */
    public static double mean(double[] data) {
        double sum = 0.0;
        for (int i = 1; i < data.length; i++) {
            sum += data[i];
        }
        return sum / data.length;
    }

    /**
     * calculate mean value of an int array
     * @param data source data
     * @return mean of data
     */
    public static double mean(int[] data) {
        double sum = 0.0;
        for (int i = 1; i < data.length; i++) {
            sum += data[i];
        }
        return sum / data.length;
    }

    /**
     * calculate standard deviation of a double array
     * @param data source data
     * @return standard deviation of data
     */
    public static double standardDeviation(double[] data) {
        double mean = mean(data);
        double deviation = 0.0;
        for (double _data : data) {
            deviation += (mean - _data) * (mean - _data);
        }
        return Math.sqrt(deviation / data.length);
    }

    /**
     * calculate standard deviation of an int array
     * @param data source data
     * @return standard deviation of data
     */
    public static double standardDeviation(int[] data) {
        double mean = mean(data);
        double deviation = 0.0;
        for (double _data : data) {
            deviation += (mean - _data) * (mean - _data);
        }
        return Math.sqrt(deviation / data.length);
    }
}
