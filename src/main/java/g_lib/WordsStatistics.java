package g_lib;

import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.ToAnalysis;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * Created by edwardlol on 15/10/14.
 */
public class WordsStatistics {

    public static List<Map.Entry<String, Integer>> mapSortInteger(Map<String, Integer> map) {
        List<Map.Entry<String, Integer>> listData = new ArrayList<>(map.entrySet());
        Collections.sort(listData,
                (Map.Entry<String, Integer> entry1, Map.Entry<String, Integer> entry2) -> entry2.getValue() - entry1.getValue()
        );
        return listData;
    }

    public static HashMap<String, Integer> read_data() {
        HashMap<String, Integer> words = new HashMap<>();
        String symbol_re = "[`~!@#$%^&*\\(\\)-_=+[{]}\\|;:'\",<.>/?\\s]*?";
        Pattern all_symbol = Pattern.compile("^" + symbol_re + "$");
        try {
            FileReader fr = new FileReader("./datasets/original/8703_model");
            BufferedReader br = new BufferedReader(fr);
            String line = br.readLine();
            while (line != null) {
                List<Term> parseResultList = ToAnalysis.parse(line);
                for (Term term : parseResultList) {
                    String sec = term.getName();
                    Matcher matcher = all_symbol.matcher(sec);
                    if (!matcher.find()) {
                        if (words.containsKey(sec)) {
                            Integer count = words.get(sec) + 1;
                            words.put(sec, count);
                        } else {
                            words.put(sec, 1);
                        }
                    }
                }
                line = br.readLine();
            }
            br.close();
            fr.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return words;
    }

    public static void record(HashMap<String, Integer> map, int count) {
        List<Map.Entry<String, Integer>> list_data = mapSortInteger(map);
        Iterator<Map.Entry<String, Integer>> it = list_data.iterator();
        try {
            FileWriter fw = new FileWriter("./datasets/8703_keyWords");
            BufferedWriter bw = new BufferedWriter(fw);
            for (int i = 0; i < count; i++) {
                Map.Entry<String, Integer> tuple = it.next();
                bw.append(tuple.getKey());
                bw.append(": ");
                bw.append(tuple.getValue().toString());
                bw.append("\n");
                System.out.println(tuple.getKey() + ": " + tuple.getValue());
            }
            bw.flush();
            bw.close();
            fw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        HashMap<String, Integer> words = read_data();
        record(words, 100);
    }
}
