package Test;

import org.ansj.domain.Term;
import org.ansj.library.UserDefineLibrary;
import org.ansj.splitWord.analysis.NlpAnalysis;
import org.ansj.splitWord.analysis.ToAnalysis;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.math3.stat.inference.*;
import util.LevensteinDistance;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 *
 * Created by edwardlol on 15/10/12.
 */
public class Test {

    public static void main(String[] args) {
        List<Term> terms = ToAnalysis.parse("奥迪2498CC小轿车@@汽油型|整车|4座|奥迪|A7|2498CC|A7 2.5 FSI");
        System.out.println(terms);

        // re test
//        String test = "bentian本田品牌";
//        String brand_filter = "([A-Za-z0-9_\u4E00-\u9FA5]+?)(品?牌)([^A-Za-z0-9_\u4E00-\u9FA5]|$)"; // xxx品牌
//        Pattern brand_pattern = Pattern.compile(brand_filter);
//        Matcher brand_matcher = brand_pattern.matcher(test);
//        if (brand_matcher.find()) { // xxx牌blabla
//            System.out.println("group1: " + brand_matcher.group(1) + "; group2: " + brand_matcher.group(2));
//        } else {
//            System.out.println("nothing");
//        }

//        double max_derivate = Double.MIN_VALUE;
//        int the_day = 0;
//        HashMap<Integer, Integer> days_map = new HashMap<>();
//        days_map.put(1,1);
//        days_map.put(2,10);
//        days_map.put(4,172);
//        days_map.put(8,19);
//        days_map.put(10,15);
//        days_map.put(13,16);
//        days_map.put(15,13);
//
//        List<Integer> keyList = days_map.keySet().stream().collect(Collectors.toList());
//        Collections.sort(keyList);
//        for (int i = 0; i < keyList.size() - 1; i++) {
//            int base_day = days_map.get(keyList.get(i));
//            int cmp_day = days_map.get(keyList.get(i + 1));
//            double temp = 1.0 * (cmp_day - base_day) / base_day;
//            if (temp > max_derivate) {
//                max_derivate = temp;
//                the_day = keyList.get(i + 1);
//            }
//        }
//        System.out.println(the_day);

//        String timea = "2008-01-25 00:02:00";
//        String timeb = "2008-01-25 00:02:00";
//        System.out.println(timea.compareTo(timeb));
//
//
//        SimpleDateFormat format =   new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
//
//        try {
//            Date date1 = format.parse(timea);
//            Date date2 = format.parse(timeb);
//            long diff = date1.getTime() - date2.getTime();
//            System.out.print("time diff: " + diff);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//

    }
}
