package str_rec;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by edwardlol on 15/8/27.
 */
public class LinkBack {

    public static void main(String[] args) {
        String cluster_file = "datasets/clusters";
        String original_file = "datasets/original/end";
        String linkback_file = "datasets/linkback";
        String display_file = "datasets/display";

        // step1: 纪录只有一个brand的cluster
        ArrayList<String> single_brand_list = new ArrayList<>();
        try {
            FileReader file_reader;
            BufferedReader buffered_reader;
            FileWriter file_writer;
            BufferedWriter buffered_writer;

            file_reader = new FileReader(cluster_file);
            buffered_reader = new BufferedReader(file_reader);
            String read_result;
            read_result = buffered_reader.readLine(); // 第一行为cluster number
            read_result = buffered_reader.readLine();
            while (read_result != null) {
                read_result  = read_result.replaceAll("[{}]","");
                String[] result = read_result.split(", ");
                if (result.length == 1) {
                    String[] cluster = result[0].split(": ");
                    single_brand_list.add(cluster[0]);
                }
                read_result = buffered_reader.readLine();
            }
            buffered_reader.close();
            file_reader.close();

            // step2: 将字段映射到map
            file_reader = new FileReader(original_file);
            buffered_reader = new BufferedReader(file_reader);
            file_writer = new FileWriter(linkback_file);
            buffered_writer = new BufferedWriter(file_writer);

            HashMap<String, HashMap<String, HashMap<String, HashMap<String, HashSet<String>>>>> map1 = new HashMap<>();
            HashMap<String, HashMap<String, HashMap<String, HashSet<String>>>> map2;
            HashMap<String, HashMap<String, HashSet<String>>> map3;
            HashMap<String, HashSet<String>> map4;
            HashSet<String> set;

            read_result = buffered_reader.readLine();
            while (read_result != null) {
                System.out.println(read_result);
                String[] _entry = read_result.split("\t");
                // \t前内容
                String[] _entry_info = _entry[0].split("@@");
                String entry_id = _entry_info[0];
                String code_ts = _entry_info[1];
                String origin_country = _entry_info[2];
                String g_name = _entry_info[3];
                // \t后内容
                String[] _goods_info = _entry[1].split("@@");
                String brand = _goods_info[0];
                String type = _goods_info[1];
                String description = _goods_info[2];

                if (single_brand_list.contains(brand)) {
                    buffered_writer.append(read_result);
                    buffered_writer.append("\t");
                    buffered_writer.append(brand);
                    buffered_writer.append("\n");
                    buffered_writer.flush();

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
            buffered_writer.close();
            file_writer.close();
            buffered_reader.close();
            file_reader.close();

            file_writer = new FileWriter(display_file);
            buffered_writer = new BufferedWriter(file_writer);

            Set<String> keys1 = map1.keySet();
            for (String key1 : keys1) {
                map2 = map1.get(key1);
                Set<String> keys2 = map2.keySet();
                for (String key2 : keys2) {
                    map3 = map2.get(key2);
                    Set<String> keys3 = map3.keySet();
                    for (String key3 : keys3) {
                        map4 = map3.get(key3);
                        Set<String> keys4 = map4.keySet();
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
            }
            buffered_writer.close();
            file_writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
