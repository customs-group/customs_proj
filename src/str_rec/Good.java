package str_rec;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.regex.*;

public class Good {
	private String original_string;
	private String brand;
	private String type;
	private static String brand_filter1 = "无牌";
	private static String brand_filter2 = "无品牌";
	private static String brand_filter3 = "品牌[:：]+[A-Za-z0-9_\u4E00-\u9FA5]+"; // 需要添加: 品牌中可能出现的符号
	private static String brand_filter4 = "[A-Za-z0-9_\u4E00-\u9FA5]+品牌"; // 需要添加: 品牌中可能出现的符号
	private static String brand_filter5 = "品牌[^A-Za-z0-9_\u4E00-\u9FA5]*[A-Za-z0-9_\u4E00-\u9FA5]+"; // 需要添加: 品牌中可能出现的符号
	//private static String brand_filter6 = "[^A-Za-z0-9_\u4E00-\u9FA5]*.*牌";
	private static String brand_filter6 = "[A-Za-z0-9_\u4E00-\u9FA5]+牌"; // 需要添加: 品牌中可能出现的符号
	
	public void set_good(String string) {
		this.original_string = string;
		Pattern pattern1 = Pattern.compile(brand_filter1);
		Matcher matcher1 = pattern1.matcher(string);
		Pattern pattern2 = Pattern.compile(brand_filter2);
		Matcher matcher2 = pattern2.matcher(string);
		Pattern pattern3 = Pattern.compile(brand_filter3);
		Matcher matcher3 = pattern3.matcher(string);
		Pattern pattern4 = Pattern.compile(brand_filter4);
		Matcher matcher4 = pattern4.matcher(string);
		Pattern pattern5 = Pattern.compile(brand_filter5);
		Matcher matcher5 = pattern5.matcher(string);
		Pattern pattern6 = Pattern.compile(brand_filter6);
		Matcher matcher6 = pattern6.matcher(string);
		
		if (matcher1.find() || matcher2.find()) {
			this.brand = "无";
		} else if (matcher3.find()) {
			String tmp = matcher3.group().replaceAll("[^A-Za-z0-9_\u4E00-\u9FA5]*","");
			this.brand = tmp.replaceAll("品牌",""); // 需要添加: 品牌中可能出现的符号
		} else if (matcher4.find()) {
			String tmp = matcher4.group().replaceAll("[^A-Za-z0-9_\u4E00-\u9FA5]*","");
			this.brand = tmp.replaceAll("品牌",""); // 需要添加: 品牌中可能出现的符号
		} else if (matcher5.find()) {
			String tmp = matcher5.group().replaceAll("[^A-Za-z0-9_\u4E00-\u9FA5]*","");
			this.brand = tmp.replaceAll("品牌",""); // 需要添加: 品牌中可能出现的符号
		} else if (matcher6.find()) {
			String tmp = matcher6.group().replaceAll("[^A-Za-z0-9_\u4E00-\u9FA5]*","");
			this.brand = tmp.replaceFirst("牌",""); // 需要添加: 品牌中可能出现的符号
		} else {
			this.brand = "无";
		}
		
	}
	
	public void record(OutputStreamWriter writer) throws IOException {
		
		/* set file name for record */
		
		
	    writer.append("品牌: " + this.brand);
	    
	    
	    
	}
	public String get_original_string() {
		return this.original_string;
	}
	public String get_brand() {
		return this.brand;
	}
	public String get_type() {
		return this.type;
	}
}
