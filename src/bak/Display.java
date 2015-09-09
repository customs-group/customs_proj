package bak;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by edwardlol on 15/9/1.
 */
public class Display {

    public static void main(String[] args) {
        String original_file = "datasets/original/end";
        String display_file = "datasets/display";
        String status_file = "datasets/status";

        try {
            FileReader file_reader = new FileReader(original_file);
            BufferedReader buffered_reader = new BufferedReader(file_reader);

            HashMap<String, HashMap<String, HashMap<String, HashMap<String, HashSet<String>>>>> map1 = new HashMap<>();
            HashMap<String, HashMap<String, HashMap<String, HashSet<String>>>> map2;
            HashMap<String, HashMap<String, HashSet<String>>> map3;
            HashMap<String, HashSet<String>> map4;
            HashSet<String> set;

            String read_result = buffered_reader.readLine();
            while (read_result != null) {
                String[] _entry = read_result.split("\t");
                if (_entry.length != 5) {
                    System.out.println("wrong entry length");
                } else {
                    String entry_id = _entry[0];
                    String code_ts = _entry[1];
                    String origin_country = _entry[2];
                    String g_name = _entry[3];
                    // \t后内容
                    String[] _goods_info = _entry[4].split("@@");
                    String brand = _goods_info[0];
                    String model = _goods_info[1];
                    String description = _goods_info[2];
                    if (_goods_info.length == 4) {
                        description += _goods_info[3];
                    }

                    if (map1.containsKey(code_ts)) {
                        map2 = map1.get(code_ts);
                        if (map2.containsKey(g_name)) {
                            map3 = map2.get(g_name);
                            if (map3.containsKey(origin_country)) {
                                map4 = map3.get(origin_country);
                                if (map4.containsKey(brand)) {
                                    set = map4.get(brand);
                                    if (!set.contains(description)) {
                                        set.add(description);
                                    }
                                } else {
                                    set = new HashSet<>();
                                    set.add(description);
                                    map4.put(brand, set);
                                }
                            } else {
                                map4 = new HashMap<>();
                                set = new HashSet<>();
                                set.add(description);
                                map4.put(brand, set);
                                map3.put(origin_country, map4);
                            }
                        } else {
                            map3 = new HashMap<>();
                            map4 = new HashMap<>();
                            set = new HashSet<>();
                            set.add(description);
                            map4.put(brand, set);
                            map3.put(origin_country, map4);
                            map2.put(g_name, map3);
                        }
                    } else {
                        map2 = new HashMap<>();
                        map3 = new HashMap<>();
                        map4 = new HashMap<>();
                        set = new HashSet<>();
                        set.add(description);
                        map4.put(brand, set);
                        map3.put(origin_country, map4);
                        map2.put(g_name, map3);
                        map1.put(code_ts, map2);
                    }
                }
                read_result = buffered_reader.readLine();
            }
            buffered_reader.close();
            file_reader.close();

            FileWriter file_writer = new FileWriter(display_file);
            BufferedWriter buffered_writer = new BufferedWriter(file_writer);

            FileWriter file_writer2 = new FileWriter(status_file);
            BufferedWriter buffered_writer2 = new BufferedWriter(file_writer2);

            List<String> keyList = map1.keySet().stream().collect(Collectors.toList());
            Collections.sort(keyList);

            Integer map1_size = map1.size();
            buffered_writer2.append("num of code_ts in total: ");
            buffered_writer2.append(map1_size.toString());
            buffered_writer2.append("\n");

            for (String key1 : keyList) {
                map2 = map1.get(key1);
                Set<String> keys2 = map2.keySet();
                Integer Goods_Count = 0;
/*
                Integer map2_size = map2.size();
                buffered_writer2.append("--num of g_name in ");
                buffered_writer2.append(key1);
                buffered_writer2.append(": ");
                buffered_writer2.append(map2_size.toString());
                buffered_writer2.append("\n");
*/
                for (String key2 : keys2) {
                    map3 = map2.get(key2);
                    Set<String> keys3 = map3.keySet();
/*
                    Integer map3_size = map3.size();
                    buffered_writer2.append("  --num of origin country in ");
                    buffered_writer2.append(key2);
                    buffered_writer2.append(": ");
                    buffered_writer2.append(map3_size.toString());
                    buffered_writer2.append("\n");
*/
                    for (String key3 : keys3) {
                        map4 = map3.get(key3);
                        Set<String> keys4 = map4.keySet();
                        Goods_Count += keys4.size();
/*
                        Integer map4_size = map4.size();
                        buffered_writer2.append("    --num of brand in ");
                        buffered_writer2.append(key3);
                        buffered_writer2.append(": ");
                        buffered_writer2.append(map4_size.toString());
                        buffered_writer2.append("\n");
*/
                        for (String key4 : keys4) {
                            set = map4.get(key4);
                            for (String _description : set) {
                                buffered_writer.append(key1);
                                buffered_writer.append("\t");
                                buffered_writer.append(key2);
                                buffered_writer.append("\t");
                                buffered_writer.append(key3);
                                buffered_writer.append("\t");
                                buffered_writer.append(key4);
                                buffered_writer.append("\t");
                                buffered_writer.append(_description);
                                buffered_writer.append("\n");
                                buffered_writer.flush();
                            }
                        }
                    }
                }

                buffered_writer2.append("-- count of code_ts ");
                buffered_writer2.append(key1);
                buffered_writer2.append(": ");
                buffered_writer2.append(Goods_Count.toString());
                buffered_writer2.append("\n");
            }
            buffered_writer2.close();
            file_writer2.close();
            buffered_writer.close();
            file_writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
