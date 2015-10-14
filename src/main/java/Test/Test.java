package Test;

import util.Dict;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by edwardlol on 15/10/12.
 */
public class Test {
    public static void main(String[] args) {
        String brand_filter = "\"brand\": \"(.*)\"";
        Pattern pattern = Pattern.compile(brand_filter);

        String test = "\"brand\": \"benzsafsafaw\"";
        Matcher matcher = pattern.matcher(test);
        if (matcher.find()) {
            System.out.println(matcher.group(2));
        } else {
            System.out.println("nope");
        }
    }
}
