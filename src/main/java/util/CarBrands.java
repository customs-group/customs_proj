package util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * Created by edwardlol on 15/10/12.
 */
public class CarBrands {

    private static final String brand_filter = "\"brand\": \"(.*?)\"";
    private static final Pattern pattern = Pattern.compile(brand_filter);

    public static void main(String[] args) {
        String source_file = "./datasets/original/cars.txt";
        String result_file = "./datasets/car_brands";

        HashSet<String> brands = new HashSet<>();

        try(FileReader fr = new FileReader(source_file);
            BufferedReader br = new BufferedReader(fr);
            FileWriter fw = new FileWriter(result_file);
            BufferedWriter bw = new BufferedWriter(fw)) {

            String line = br.readLine();
            while (line != null) {
                Matcher matcher = pattern.matcher(line);
                if (matcher.find()) {
                    brands.add(matcher.group(1));
                }
                line = br.readLine();
            }

            for (String brand : brands) {
                bw.append(brand).append('\n');
                bw.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
