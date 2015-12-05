package util;

/**
 *
 * Created by edwardlol on 15/11/24.
 */
public class EdMath {

    /**
     * calculate mean value of a double array
     * @param data source data
     * @return mean of data
     */
    public static double calculate_mean(double[] data) {
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
    public static double calculate_mean(int[] data) {
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
    public static double calculate_sd(double[] data) {
        double mean = calculate_mean(data);
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
    public static double calculate_sd(int[] data) {
        double mean = calculate_mean(data);
        double deviation = 0.0;
        for (double _data : data) {
            deviation += (mean - _data) * (mean - _data);
        }
        return Math.sqrt(deviation / data.length);
    }
}
