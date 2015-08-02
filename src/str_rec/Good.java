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
	private static Map<String, String> normalizer_map;
	private static Set<String> pre_dict_2;
	private static Set<String> pre_dict_3
	;
	private String original_string;
	private String normalized_string;
	private String filtered_string;
	
	private String brand;
	private String type;
	
	static {
		brand_filter0 = "无品?牌";
		brand_filter1 = "品牌:+([A-Za-z0-9_\u4E00-\u9FA5]+)"; // 品牌:xxx
		brand_filter2 = "(^|[^A-Za-z0-9_\u4E00-\u9FA5]+)品牌[^A-Za-z0-9_\u4E00-\u9FA5]*([A-Za-z0-9_\u4E00-\u9FA5]+)"; // 品牌xxx
		brand_filter3 = "([A-Za-z0-9_\u4E00-\u9FA5]+?)品?牌([^A-Za-z0-9_\u4E00-\u9FA5]|$)"; // xxx品牌
		brand_filter4 = "([A-Za-z0-9_\u4E00-\u9FA5]+)([^品])牌"; // xxx牌blabla
		
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
		
		pre_dict_2 = new HashSet<String>();
		pre_dict_2.add("吊牌");
		pre_dict_2.add("门牌");
		pre_dict_2.add("车牌");
		pre_dict_2.add("铭牌");
		pre_dict_2.add("名牌");
		pre_dict_2.add("挂牌");
		pre_dict_2.add("标牌");
		pre_dict_2.add("奖牌");

		pre_dict_3 = new HashSet<String>();
		pre_dict_3.add("塑料牌");
		pre_dict_3.add("指示牌");
		pre_dict_3.add("标价牌");
		pre_dict_3.add("标记牌");
		pre_dict_3.add("标识牌");
		pre_dict_3.add("广告牌");
		pre_dict_3.add("行李牌");
		pre_dict_3.add("警示牌");
		pre_dict_3.add("价格牌");
		pre_dict_3.add("价钱牌");
		pre_dict_3.add("麻将牌");
		pre_dict_3.add("扑克牌");
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
		for (String s : pre_dict_2) {
			this.filtered_string = this.filtered_string.replaceAll(s, "");
		}
		for (String s : pre_dict_3) {
			this.filtered_string = this.filtered_string.replaceAll(s, "");
		}
	}
	
	public void set_good(String string) {
		this.original_string = string;
		this.normalize_symbol();
		this.filter_words();
		Pattern pattern0 = Pattern.compile(brand_filter0);
		Matcher matcher0 = pattern0.matcher(this.filtered_string);
		Pattern pattern1 = Pattern.compile(brand_filter1);
		Matcher matcher1 = pattern1.matcher(this.filtered_string);
		Pattern pattern2 = Pattern.compile(brand_filter2);
		Matcher matcher2 = pattern2.matcher(this.filtered_string);
		Pattern pattern3 = Pattern.compile(brand_filter3);
		Matcher matcher3 = pattern3.matcher(this.filtered_string);
		Pattern pattern4 = Pattern.compile(brand_filter4);
		Matcher matcher4 = pattern4.matcher(this.filtered_string);
		
		if (matcher0.find()) {
			this.brand = "无";
		} else if (matcher1.find()) {
			this.brand = matcher1.group(1);
		} else if (matcher2.find()) {
			this.brand = matcher2.group(2);
		} else if (matcher3.find()) {
			this.brand = matcher3.group(1);
		} else if (matcher4.find()) {
			this.brand = matcher4.group(1) + matcher4.group(2);
		} else {
			this.brand = "无";
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
}
