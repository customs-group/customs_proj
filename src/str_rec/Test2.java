package str_rec;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Test2 {
	public static void main(String[] args) {
		String test1 = "MIC-0009/0011,无牌是生产件是毛坯";
		String brand_filter0 = "无品?牌";
		String brand_filter1 = "品牌[:：]+([A-Za-z0-9_\u4E00-\u9FA5]+)"; // 品牌:xxx
		String brand_filter2 = "(^|[^A-Za-z0-9_\u4E00-\u9FA5]+)品牌[^A-Za-z0-9_\u4E00-\u9FA5]*([A-Za-z0-9_\u4E00-\u9FA5]+)"; // 品牌xxx
		String brand_filter3 = "([A-Za-z0-9_\u4E00-\u9FA5]+?)品?牌([^A-Za-z0-9_\u4E00-\u9FA5]|$)"; // xxx(品)牌
		String brand_filter4 = "([A-Za-z0-9_\u4E00-\u9FA5]+)([^品])牌"; // xxx牌blabla
		
		Pattern pattern0 = Pattern.compile(brand_filter0);
		Matcher matcher0 = pattern0.matcher(test1);
		Pattern pattern1 = Pattern.compile(brand_filter1);
		Matcher matcher1 = pattern1.matcher(test1);
		Pattern pattern2 = Pattern.compile(brand_filter2);
		Matcher matcher2 = pattern2.matcher(test1);
		Pattern pattern3 = Pattern.compile(brand_filter3);
		Matcher matcher3 = pattern3.matcher(test1);
		Pattern pattern4 = Pattern.compile(brand_filter4);
		Matcher matcher4 = pattern4.matcher(test1);
		
		if (matcher0.find()) {
			System.out.println(matcher0.group());
		} else if (matcher1.find()) {
			System.out.println("find in 1: " + matcher1.group(1));
		} else if (matcher2.find()) {
			System.out.println("find in 2: " + matcher2.group(2));
		} else if (matcher3.find()) {
			System.out.println("find in 3: " + matcher3.group(1));	
		} else if (matcher4.find()) {
			System.out.println("find in 4: " + matcher4.group(1) + matcher4.group(2));
		} else {
			System.out.println("nope");
		}
		
		LevensteinDistance ld = new LevensteinDistance();
		String a = "bent111";
		String b = "丰田凯美瑞";
		float stringSimilarity = ld.getDistance(a, b);
		System.out.println("string sim: " + stringSimilarity);
		try {
        	File file = new File("/Users/edwardlol/Downloads/ALL_CODE/hhh");
        	BufferedReader reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            
            // 一次读入一行，直到读入null为文件结束
            while ((tempString = reader.readLine()) != null) {
                // 显示行号
                System.out.println(tempString);
                
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
}
