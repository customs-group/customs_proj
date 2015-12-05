package g_lib;

import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.ToAnalysis;
import util.LevensteinDistance;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * Created by edwardlol on 15/11/30.
 */
public class Test {

    private static HashMap<String, HashSet<String>> hs_to_attrs;

    public static void read_keys(String file) {
        hs_to_attrs = new HashMap<>();
        HashSet<String> attr_set;
        try {
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            String line = br.readLine();
            line = br.readLine(); // 第一行是sb
            while (line != null) {
                String[] content = line.split(";");
                String hs_code = content[0];
                if (hs_to_attrs.containsKey(hs_code)) {
                    attr_set = hs_to_attrs.get(hs_code);
                } else {
                    attr_set = new HashSet<>();
                }
                attr_set.add(content[3]);
                hs_to_attrs.put(hs_code, attr_set);
                line = br.readLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void record() {
        hs_to_attrs.forEach((String hs_code, HashSet<String> attr_set) -> {
            try {
                FileWriter fw = new FileWriter("datasets/attrs/" + hs_code);
                BufferedWriter bw = new BufferedWriter(fw);
                attr_set.forEach(attr -> {
                    try {
                        bw.append(attr);
                        bw.append("\n");
                        bw.flush();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
                bw.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public static void do_sth() {
        String symbol_re = "[`~!@#\\$%\\^&\\*\\(\\)\\-_=\\+[{]}\\|;:'\",<\\.>/\\?\\s]+";
        Pattern all_symbol = Pattern.compile("^" + symbol_re + "$");
        LevensteinDistance ld = new LevensteinDistance();
        try {
            FileReader fr = new FileReader("datasets/original/end8703");
            BufferedReader br = new BufferedReader(fr);

            FileWriter fw = new FileWriter("datasets/id_test");
            BufferedWriter bw = new BufferedWriter(fw);

            String line = br.readLine();
            while (line != null) {
                String[] content = line.split("\t");
                if (content.length != 6) {
                    System.out.println("end file format error");
                    System.out.println(line);
                    line = br.readLine();
                    continue;
                }
                String id = content[0] + ":::" + content[1];
                List<Term> parseResultList = ToAnalysis.parse(content[5]);

                HashMap<String, String> property = new HashMap<>(); // 一条entry_list的 k -> v

                for (String hs : hs_to_attrs.keySet()) {
                    if (content[2].substring(0, hs.length()).equals(hs)) { // 找到对应的attrs_set
                        HashSet<String> attrs = hs_to_attrs.get(hs);
                        attrs.forEach(attr -> property.put(attr, ""));

                        String whats_left = "";
                        String sep_result = "";
                        for (Term term : parseResultList) {
                            String sec = term.getName();
                            sep_result += sec + ";";
                            Matcher matcher = all_symbol.matcher(sec);
                            if (!matcher.find()) { // 不全是符号
                                float max_distance = Float.MIN_VALUE;
                                String most_sim_attr = "";
                                for (String attr : attrs) {
                                    float distance = ld.getDistance(attr, sec);
                                    if (distance > max_distance) {
                                        max_distance = distance;
                                        most_sim_attr = attr;
                                    }
                                }
                                if (property.containsKey(most_sim_attr)) {
                                    property.put(most_sim_attr, sec);
                                } else {
                                    whats_left += sec + ";";
                                }
                            }
                        }
                        property.put("_whats_left", whats_left);
                        property.put("_original", content[5]);
                        property.put("_sep_result", sep_result);
                        break;
                    }
                }

                bw.append(id);
                bw.append("\n");

                property.forEach((String k, String v) -> {
                    try {
                        bw.append("\t");
                        bw.append(k);
                        bw.append(": ");
                        bw.append(v);
                        bw.append("\n");
                        bw.flush();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
                bw.flush();
                line = br.readLine();
            }
            bw.close();
            fw.close();
            br.close();
            fr.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        read_keys("datasets/original/attrs_2015");
        record();
        do_sth();
    }
}
