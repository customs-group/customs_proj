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
    public static void main(String[] args) {
        String source_file = "./datasets/original/cars.txt";
        String result_file = "./datasets/car_brands";

        String brand_filter = "\"brand\": \"(.*?)\"";
        Pattern pattern = Pattern.compile(brand_filter);

        HashSet<String> brands = new HashSet<>();

        try {
            FileReader fr = new FileReader(source_file);
            BufferedReader br = new BufferedReader(fr);

            String line = br.readLine();
            while (line != null) {
                Matcher matcher = pattern.matcher(line);
                if (matcher.find()) {
                    brands.add(matcher.group(1));
                }
                line = br.readLine();
            }
            br.close();
            fr.close();

            FileWriter fw = new FileWriter(result_file);
            BufferedWriter bw = new BufferedWriter(fw);
            for (String brand : brands) {
                bw.append(brand);
                bw.append("\n");
                bw.flush();
            }
            bw.close();
            fw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
