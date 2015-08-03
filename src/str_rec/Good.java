package str_rec;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.*;

public class Good {
	private static String brand_filter0;
	private static String brand_filter1;
	private static String brand_filter2;
	private static String brand_filter3;
	private static String brand_filter4;
	private static String type_filter0;
	private static String type_filter1;
	private static String type_filter2;
	private static String type_filter3;
	private static String type_filter4;
	
	private static Map<String, String> normalizer_map;
	private static Set<String> brand_pre_dict_2;
	private static Set<String> brand_pre_dict_3;
	
	private String original_string;
	private String normalized_string;
	private String filtered_string;
	
	private String brand;
	private String type;
	private String discription;
	
	public Good() {
		this.brand = "无";
		this.type = "无";
		this.discription = "";
	}
	static {
		brand_filter0 = "无品?牌";
		brand_filter1 = "品牌:+([A-Za-z0-9_\u4E00-\u9FA5]+)"; // 品牌:xxx
		brand_filter2 = "(^|[^A-Za-z0-9_\u4E00-\u9FA5]+)品牌[^A-Za-z0-9_\u4E00-\u9FA5]*([A-Za-z0-9_\u4E00-\u9FA5]+)"; // 品牌xxx
		brand_filter3 = "([A-Za-z0-9_\u4E00-\u9FA5]+?)(品?牌)([^A-Za-z0-9_\u4E00-\u9FA5]|$)"; // xxx品牌
		brand_filter4 = "([A-Za-z0-9_\u4E00-\u9FA5]+)([^品])牌"; // xxx牌blabla
		
		type_filter0 = "无型号";
		type_filter1 = "型号:+([A-Za-z0-9_\u4E00-\u9FA5]+)"; // 型号:xxx
		type_filter2 = "(^|[^A-Za-z0-9_\u4E00-\u9FA5]+)型号[^A-Za-z0-9_\u4E00-\u9FA5]*([A-Za-z0-9_\u4E00-\u9FA5]+)"; // 型号xxx
		type_filter3 = "([A-Za-z0-9_\u4E00-\u9FA5]+?)(型?号)([^A-Za-z0-9_\u4E00-\u9FA5]|$)"; // xxx型号
		type_filter4 = "([A-Za-z0-9_\u4E00-\u9FA5]+)([^型])号"; // xxx型blabla
		
		normalizer_map = new HashMap<String, String>();
		normalizer_map.put("｀", "`");
		normalizer_map.put("～", "~");
		normalizer_map.put("！", "!");
		normalizer_map.put("@", "@");
		normalizer_map.put("＃", "#");
		normalizer_map.put("¥", "\\$");
		normalizer_map.put("％", "%");
		normalizer_map.put("……", "^");
		normalizer_map.put("&", "&");
		normalizer_map.put("＊", "*");
		normalizer_map.put("（", "(");
		normalizer_map.put("）", ")");
		normalizer_map.put("－", "-");
		normalizer_map.put("＝", "=");
		normalizer_map.put("——", "_");
		normalizer_map.put("＋", "+");
		normalizer_map.put("［", "[");
		normalizer_map.put("］", "]");
		normalizer_map.put("｛", "{");
		normalizer_map.put("｝", "}");
		normalizer_map.put("；", ";");
		normalizer_map.put("‘", "'");
		normalizer_map.put("’", "'");
		normalizer_map.put("：", ":");
		normalizer_map.put("“", "\"");
		normalizer_map.put("”", "\"");
		normalizer_map.put("，", ",");
		normalizer_map.put("。", ".");
		normalizer_map.put("《", "<");
		normalizer_map.put("》", ">");
		normalizer_map.put("／", "/");
		normalizer_map.put("？", "?");
		normalizer_map.put("、", "\\\\");
		normalizer_map.put("｜", "|");
		
		brand_pre_dict_2 = new HashSet<String>();
		brand_pre_dict_2.add("吊牌");
		brand_pre_dict_2.add("门牌");
		brand_pre_dict_2.add("车牌");
		brand_pre_dict_2.add("铭牌");
		brand_pre_dict_2.add("名牌");
		brand_pre_dict_2.add("挂牌");
		brand_pre_dict_2.add("标牌");
		brand_pre_dict_2.add("奖牌");

		brand_pre_dict_3 = new HashSet<String>();
		brand_pre_dict_3.add("塑料牌");
		brand_pre_dict_3.add("指示牌");
		brand_pre_dict_3.add("标价牌");
		brand_pre_dict_3.add("标记牌");
		brand_pre_dict_3.add("标识牌");
		brand_pre_dict_3.add("广告牌");
		brand_pre_dict_3.add("行李牌");
		brand_pre_dict_3.add("警示牌");
		brand_pre_dict_3.add("价格牌");
		brand_pre_dict_3.add("价钱牌");
		brand_pre_dict_3.add("麻将牌");
		brand_pre_dict_3.add("扑克牌");
	}
	
	private void normalize_symbol() {
		this.normalized_string = this.original_string;
		Set<String> keys = normalizer_map.keySet();
		for(String key : keys) {
			this.normalized_string.replaceAll(key, normalizer_map.get(key));
		}
	}
	
	private void filter_words() {
		this.filtered_string = this.normalized_string;
		//this.filtered_string.replaceAll("", replacement);
		for (String s : brand_pre_dict_2) {
			this.filtered_string = this.filtered_string.replaceAll(s, "");
		}
		for (String s : brand_pre_dict_3) {
			this.filtered_string = this.filtered_string.replaceAll(s, "");
		}
	}
	
	public void set_good_by_gname(String string) {
		this.original_string = string;
		this.normalize_symbol();
		this.filter_words();
		Pattern brand_pattern0 = Pattern.compile(brand_filter0);
		Matcher brand_matcher0 = brand_pattern0.matcher(this.filtered_string);
		Pattern brand_pattern1 = Pattern.compile(brand_filter1);
		Matcher brand_matcher1 = brand_pattern1.matcher(this.filtered_string);
		Pattern brand_pattern2 = Pattern.compile(brand_filter2);
		Matcher brand_matcher2 = brand_pattern2.matcher(this.filtered_string);
		Pattern brand_pattern3 = Pattern.compile(brand_filter3);
		Matcher brand_matcher3 = brand_pattern3.matcher(this.filtered_string);
		Pattern brand_pattern4 = Pattern.compile(brand_filter4);
		Matcher brand_matcher4 = brand_pattern4.matcher(this.filtered_string);
		if (this.brand.equals("无")) {
			if (brand_matcher0.find()) {
				this.brand = "无";
			} else if (brand_matcher1.find()) {
				this.brand = brand_matcher1.group(1);
			} else if (brand_matcher2.find()) {
				this.brand = brand_matcher2.group(2);
			} else if (brand_matcher3.find()) {
				this.brand = brand_matcher3.group(1);
			} else if (brand_matcher4.find()) {
				this.brand = brand_matcher4.group(1) + brand_matcher4.group(2);
			} else {
				this.brand = "无";
			}
		}
	}
	
	public void set_good_by_gmodel(String string) {
		this.original_string = string;
		this.normalize_symbol();
		this.filter_words();
		this.discription = this.normalized_string;
		
		Pattern brand_pattern0 = Pattern.compile(brand_filter0);
		Matcher brand_matcher0 = brand_pattern0.matcher(this.filtered_string);
		Pattern brand_pattern1 = Pattern.compile(brand_filter1);
		Matcher brand_matcher1 = brand_pattern1.matcher(this.filtered_string);
		Pattern brand_pattern2 = Pattern.compile(brand_filter2);
		Matcher brand_matcher2 = brand_pattern2.matcher(this.filtered_string);
		Pattern brand_pattern3 = Pattern.compile(brand_filter3);
		Matcher brand_matcher3 = brand_pattern3.matcher(this.filtered_string);
		Pattern brand_pattern4 = Pattern.compile(brand_filter4);
		Matcher brand_matcher4 = brand_pattern4.matcher(this.filtered_string);
		if (this.brand.equals("无")) {
			if (brand_matcher0.find()) {
				this.brand = "无";
				this.discription = this.discription.replaceFirst(brand_matcher0.group(), "");
			} else if (brand_matcher1.find()) {
				this.brand = brand_matcher1.group(1);
				this.discription = this.discription.replaceFirst("品牌:" + this.brand, "");
			} else if (brand_matcher2.find()) {
				this.brand = brand_matcher2.group(2);
				this.discription = this.discription.replaceFirst("品牌" + this.brand, "");
			} else if (brand_matcher3.find()) {
				this.brand = brand_matcher3.group(1);
				this.discription = this.discription.replaceFirst(this.brand + brand_matcher3.group(2), "");
			} else if (brand_matcher4.find()) {
				this.brand = brand_matcher4.group(1) + brand_matcher4.group(2);
				this.discription = this.discription.replace(this.brand + "牌", "");
			} else {
				this.brand = "无";
			}
		}
		
		Pattern type_pattern0 = Pattern.compile(type_filter0);
		Matcher type_matcher0 = type_pattern0.matcher(this.filtered_string);
		Pattern type_pattern1 = Pattern.compile(type_filter1);
		Matcher type_matcher1 = type_pattern1.matcher(this.filtered_string);
		Pattern type_pattern2 = Pattern.compile(type_filter2);
		Matcher type_matcher2 = type_pattern2.matcher(this.filtered_string);
		Pattern type_pattern3 = Pattern.compile(type_filter3);
		Matcher type_matcher3 = type_pattern3.matcher(this.filtered_string);
		Pattern type_pattern4 = Pattern.compile(type_filter4);
		Matcher type_matcher4 = type_pattern4.matcher(this.filtered_string);
		if (this.type.equals("无")) {
			if (type_matcher0.find()) {
				this.type = "无";
				this.discription = this.discription.replaceFirst(type_matcher0.group(), "");
			} else if (type_matcher1.find()) {
				this.type = type_matcher1.group(1);
				this.discription = this.discription.replaceFirst("型号:" + this.type, "");
			} else if (type_matcher2.find()) {
				this.type = type_matcher2.group(2);
				this.discription = this.discription.replaceFirst("型号" + this.type, "");
			} else if (type_matcher3.find()) {
				this.type = type_matcher3.group(1);
				this.discription = this.discription.replaceFirst(this.type + type_matcher3.group(2), "");
			} else if (type_matcher4.find()) {
				this.type = type_matcher4.group(1) + type_matcher4.group(2);
				this.discription = this.discription.replace(this.type + "号", "");
			} else {
				this.type = "无";
			}
			Pattern discryption_pattern = Pattern.compile("^[A-Za-z0-9_.-]+$");
			Matcher discryption_matcher = discryption_pattern.matcher(this.discription);
			if (this.type.equals("无") && discryption_matcher.find()) {
				this.type = this.discription;
				this.discription = "";
			}
		}
	}

	/* get/set methods */
	public String get_original_string() {
		return this.original_string;
	}
	public String get_normalized_string() {
		return this.normalized_string;
	}
	public String get_filtered_string() {
		return this.filtered_string;
	}
	public String get_brand() {
		return this.brand;
	}
	public String get_type() {
		return this.type;
	}
	public String get_discription() {
		return this.discription;
	}
}
