package g_lib;

import util.Dict;

import java.util.regex.*;

public class Good {
	private static String[] brand_filter;
	private static String[] type_filter;

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
		brand_filter = new String[5];
		brand_filter[0] = "无品?牌"; // 无品牌
		brand_filter[1] = "品牌:+([A-Za-z0-9_\u4E00-\u9FA5]+)"; // 品牌:xxx
		brand_filter[2] = "(^|[^A-Za-z0-9_\u4E00-\u9FA5]+)品牌[^A-Za-z0-9_\u4E00-\u9FA5]*([A-Za-z0-9_\u4E00-\u9FA5]+)"; // 品牌xxx
		brand_filter[3] = "([A-Za-z0-9_\u4E00-\u9FA5]+?)(品?牌)([^A-Za-z0-9_\u4E00-\u9FA5]|$)"; // xxx品牌
		brand_filter[4] = "([A-Za-z0-9_\u4E00-\u9FA5]+)([^品])牌"; // xxx牌blabla

		type_filter = new String[5];
		type_filter[0] = "无型号";
		type_filter[1] = "型号:+([A-Za-z0-9_\u4E00-\u9FA5]+)"; // 型号:xxx
		type_filter[2] = "(^|[^A-Za-z0-9_\u4E00-\u9FA5]+)型号[^A-Za-z0-9_\u4E00-\u9FA5]*([A-Za-z0-9_\u4E00-\u9FA5]+)"; // 型号xxx
		type_filter[3] = "([A-Za-z0-9_\u4E00-\u9FA5]+?)(型?号)([^A-Za-z0-9_\u4E00-\u9FA5]|$)"; // xxx型号
		type_filter[4] = "([A-Za-z0-9_\u4E00-\u9FA5]+)([^型])号"; // xxx型blabla
	}
	
	/**
	 * 符号标准化
	 */
	private void normalize_symbol() {
		this.normalized_string = this.original_string;
		Dict.get_normalizer_map().forEach((String key, String value) -> this.normalized_string = this.normalized_string.replaceAll(key, value));
	}
	
	/**
	 * 过滤固定词组
	 */
	private void filter_words() {
		this.filtered_string = this.normalized_string;
		for (String s : Dict.get_brand_pre_dict_2()) {
			this.filtered_string = this.filtered_string.replaceAll(s, "");
		}
		for (String s : Dict.get_brand_pre_dict_3()) {
			this.filtered_string = this.filtered_string.replaceAll(s, "");
		}
	}
	
	/**
	 * 通过g_model设定商品属性
	 * @param string g_model
	 */
	public void set_good(String string) {
		this.original_string = string;
		this.normalize_symbol();
		this.filter_words();
		this.discription = this.normalized_string;

		Pattern[] brand_pattern = new Pattern[5];
		Matcher[] brand_matcher = new Matcher[5];
		for (int i = 0; i < 5; i++) {
			brand_pattern[i] = Pattern.compile(brand_filter[i]);
			brand_matcher[i] = brand_pattern[i].matcher(this.filtered_string);
		}

		if (this.brand.equals("无")) {
			if (brand_matcher[0].find()) {
				this.brand = "无";
				this.discription = this.discription.replaceFirst(brand_matcher[0].group(), "");
			} else if (brand_matcher[1].find()) {
				this.brand = brand_matcher[1].group(1);
				this.discription = this.discription.replaceFirst("品牌:" + this.brand, "");
			} else if (brand_matcher[2].find()) {
				this.brand = brand_matcher[2].group(2);
				this.discription = this.discription.replaceFirst("品牌" + this.brand, "");
			} else if (brand_matcher[3].find()) {
				this.brand = brand_matcher[3].group(1);
				this.discription = this.discription.replaceFirst(this.brand + brand_matcher[3].group(2), "");
			} else if (brand_matcher[4].find()) {
				this.brand = brand_matcher[4].group(1) + brand_matcher[4].group(2);
				this.discription = this.discription.replace(this.brand + "牌", "");
			} else {
				this.brand = "无";
			}
		}

		Pattern[] type_pattern = new Pattern[5];
		Matcher[] type_matcher = new Matcher[5];
		for (int i = 0; i < 5; i++) {
			type_pattern[i] = Pattern.compile(type_filter[i]);
			type_matcher[i] = type_pattern[i].matcher(this.filtered_string);
		}

		if (this.type.equals("无")) {
			if (type_matcher[0].find()) {
				this.type = "无";
				this.discription = this.discription.replaceFirst(type_matcher[0].group(), "");
			} else if (type_matcher[1].find()) {
				this.type = type_matcher[1].group(1);
				this.discription = this.discription.replaceFirst("型号:" + this.type, "");
			} else if (type_matcher[2].find()) {
				this.type = type_matcher[2].group(2);
				this.discription = this.discription.replaceFirst("型号" + this.type, "");
			} else if (type_matcher[3].find()) {
				this.type = type_matcher[3].group(1);
				this.discription = this.discription.replaceFirst(this.type + type_matcher[3].group(2), "");
			} else if (type_matcher[4].find()) {
				this.type = type_matcher[4].group(1) + type_matcher[4].group(2);
				this.discription = this.discription.replace(this.type + "号", "");
			} else {
				this.type = "无";
			}
			Pattern discription_pattern = Pattern.compile("^[A-Za-z0-9_.-]+$");
			Matcher discription_matcher = discription_pattern.matcher(this.discription);
			if (this.type.equals("无") && discription_matcher.find()) {
				this.type = this.discription;
				this.discription = "";
			}
		}
	}

	/** get/set methods */
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
