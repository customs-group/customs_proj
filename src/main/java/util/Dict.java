package util;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 *
 * Created by edwardlol on 15/10/13.
 */
public class Dict {
    private static HashSet<String> symbol_set;
    private static Map<String, String> normalizer_map;
    private static Set<String> brand_pre_dict_2;
    private static Set<String> brand_pre_dict_3;

    static {
        symbol_set = new HashSet<>();
        symbol_set.add("`");
        symbol_set.add("~");
        symbol_set.add("!");
        symbol_set.add("@");
        symbol_set.add("#");
        symbol_set.add("$");
        symbol_set.add("%");
        symbol_set.add("^");
        symbol_set.add("&");
        symbol_set.add("*");
        symbol_set.add("(");
        symbol_set.add(")");
        symbol_set.add("-");
        symbol_set.add("_");
        symbol_set.add("=");
        symbol_set.add("+");
        symbol_set.add("[");
        symbol_set.add("{");
        symbol_set.add("]");
        symbol_set.add("}");
        symbol_set.add("\\");
        symbol_set.add(";");
        symbol_set.add(":");
        symbol_set.add("'");
        symbol_set.add("\"");
        symbol_set.add(",");
        symbol_set.add("<");
        symbol_set.add(".");
        symbol_set.add(">");
        symbol_set.add("/");
        symbol_set.add("?");


        normalizer_map = new HashMap<>();
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

        brand_pre_dict_2 = new HashSet<>();
        brand_pre_dict_2.add("吊牌");
        brand_pre_dict_2.add("门牌");
        brand_pre_dict_2.add("车牌");
        brand_pre_dict_2.add("铭牌");
        brand_pre_dict_2.add("名牌");
        brand_pre_dict_2.add("挂牌");
        brand_pre_dict_2.add("标牌");
        brand_pre_dict_2.add("奖牌");

        brand_pre_dict_3 = new HashSet<>();
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

    public static HashSet<String> get_symbol_set() {
        return symbol_set;
    }
    public static Map<String, String> get_normalizer_map() {
        return normalizer_map;
    }
    public static Set<String> get_brand_pre_dict_2() {
        return brand_pre_dict_2;
    }
    public static Set<String> get_brand_pre_dict_3() {
        return brand_pre_dict_3;
    }
}
