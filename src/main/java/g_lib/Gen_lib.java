package g_lib;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Gen_lib
 * Created by edwardlol on 15/9/1.
 */
public class Gen_lib {
    private enum record_type {
        with_description,
        without_description
    }
    private static String original_file = "datasets/original/end";
    private static String display_file = "datasets/goods_lib";
    private static String status_file = "datasets/status";

    private static HashMap<String, HashMap<String, HashMap<String, HashMap<String, HashMap<String, HashSet<String>>>>>> code_to_name;
    private static HashMap<String, HashMap<String, HashMap<String, HashMap<String, HashSet<String>>>>> name_to_country;
    private static HashMap<String, HashMap<String, HashMap<String, HashSet<String>>>> country_to_brand;
    private static HashMap<String, HashMap<String, HashSet<String>>> brand_to_desc;
    private static HashMap<String, HashSet<String>> desc_to_id;
    private static HashSet<String> id_set;
    //private static HashMap<String, Integer> desc_to_cnt;

    /**
     *
     * @param file_name original file
     * @throws Exception
     */
    private static void read_from_file(String file_name) throws Exception {
        System.out.print("reading data from file...");
        code_to_name = new HashMap<>();
        FileReader file_reader = new FileReader(file_name);
        BufferedReader buffered_reader = new BufferedReader(file_reader);
        String read_result = buffered_reader.readLine();
        while (read_result != null) {
            String[] _entry = read_result.split("\t");
            if (_entry.length != 6) {
                System.out.println("wrong entry length");
            } else {
                String entry_id = _entry[0];
                String g_no = _entry[1];
                String code_ts = _entry[2];
                String origin_country = _entry[3];
                String g_name = _entry[4];
                // \t后内容
                String[] _goods_info = _entry[5].split("@@");
                String brand = _goods_info[0];
                String model = _goods_info[1];
                String description = _goods_info[2];
                String id = entry_id + "::" + g_no;
                if (_goods_info.length == 4) {
                    description += _goods_info[3];
                }
                if (brand.equals("无")) {
                    read_result = buffered_reader.readLine();
                    continue;
                }
                if (code_to_name.containsKey(code_ts)) {
                    name_to_country = code_to_name.get(code_ts);
                    if (name_to_country.containsKey(g_name)) {
                        country_to_brand = name_to_country.get(g_name);
                        if (country_to_brand.containsKey(origin_country)) {
                            brand_to_desc = country_to_brand.get(origin_country);
                            if (brand_to_desc.containsKey(brand)) {
                                desc_to_id = brand_to_desc.get(brand);
                                if (desc_to_id.containsKey(description)) {
                                    id_set = desc_to_id.get(description);
                                    id_set.add(id);
                                } else {
                                    id_set = new HashSet<>();
                                    id_set.add(id);
                                    desc_to_id.put(description, id_set);
                                }
                            } else {
                                desc_to_id = new HashMap<>();
                                id_set = new HashSet<>();
                                id_set.add(id);
                                desc_to_id.put(description, id_set);
                                brand_to_desc.put(brand, desc_to_id);
                            }
                        } else {
                            brand_to_desc = new HashMap<>();
                            desc_to_id = new HashMap<>();
                            id_set = new HashSet<>();
                            id_set.add(id);
                            desc_to_id.put(description, id_set);
                            brand_to_desc.put(brand, desc_to_id);
                            country_to_brand.put(origin_country, brand_to_desc);
                        }
                    } else {
                        country_to_brand = new HashMap<>();
                        brand_to_desc = new HashMap<>();
                        desc_to_id = new HashMap<>();
                        id_set = new HashSet<>();
                        id_set.add(id);
                        desc_to_id.put(description, id_set);
                        brand_to_desc.put(brand, desc_to_id);
                        country_to_brand.put(origin_country, brand_to_desc);
                        name_to_country.put(g_name, country_to_brand);
                    }
                } else {
                    name_to_country = new HashMap<>();
                    country_to_brand = new HashMap<>();
                    brand_to_desc = new HashMap<>();
                    desc_to_id = new HashMap<>();
                    id_set = new HashSet<>();
                    id_set.add(id);
                    desc_to_id.put(description, id_set);
                    brand_to_desc.put(brand, desc_to_id);
                    country_to_brand.put(origin_country, brand_to_desc);
                    name_to_country.put(g_name, country_to_brand);
                    code_to_name.put(code_ts, name_to_country);
                }
            }
            read_result = buffered_reader.readLine();
        }
        buffered_reader.close();
        file_reader.close();
        System.out.println("data reading done!");
    }

    /**
     *
     * @param file_name status file
     * @throws Exception
     */
    private static void record_status(String file_name) throws Exception{
        System.out.println("recording lib status...");
        FileWriter file_writer = new FileWriter(file_name);
        BufferedWriter buffered_writer = new BufferedWriter(file_writer);

        Integer map1_size = code_to_name.size();
        buffered_writer.append("num of code_ts in total: ");
        buffered_writer.append(map1_size.toString());
        buffered_writer.append("\n");

        List<String> keyList = code_to_name.keySet().stream().collect(Collectors.toList());
        Collections.sort(keyList);
        for (String key1 : keyList) {
            name_to_country = code_to_name.get(key1);
            Set<String> keys2 = name_to_country.keySet();
            Integer Goods_Count = 0;
            for (String key2 : keys2) {
                country_to_brand = name_to_country.get(key2);
                Set<String> keys3 = country_to_brand.keySet();
                for (String key3 : keys3) {
                    brand_to_desc = country_to_brand.get(key3);
                    Set<String> keys4 = brand_to_desc.keySet();
                    for (String key4 : keys4) {
                        desc_to_id = brand_to_desc.get(key4);
                        Set<String> keys5 = desc_to_id.keySet();
                        for (String key5 : keys5) {
                            Goods_Count += desc_to_id.get(key5).size();
                        }
                    }
                }
            }
            buffered_writer.append("-- count of code_ts ");
            buffered_writer.append(key1);
            buffered_writer.append(": ");
            buffered_writer.append(Goods_Count.toString());
            buffered_writer.append("\n");
        }
        buffered_writer.close();
        file_writer.close();
        System.out.println("status recorded!");
    }

    /**
     *
     * @param file_name display file
     * @param type with or without description
     * @throws Exception
     */
    private static void record(String file_name, record_type type) throws Exception {
        System.out.println("recording lib...");
        FileWriter file_writer = new FileWriter(file_name);
        BufferedWriter buffered_writer = new BufferedWriter(file_writer);

        List<String> code_ts_List = code_to_name.keySet().stream().collect(Collectors.toList());
        Collections.sort(code_ts_List);
        for (String code_ts : code_ts_List) {
            name_to_country = code_to_name.get(code_ts);
            Set<String> g_names = name_to_country.keySet();
            for (String g_name : g_names) {
                country_to_brand = name_to_country.get(g_name);
                Set<String> countrys = country_to_brand.keySet();
                for (String origin_country : countrys) {
                    brand_to_desc = country_to_brand.get(origin_country);
                    Set<String> brands = brand_to_desc.keySet();
                    for (String brand : brands) {
                        desc_to_id = brand_to_desc.get(brand);
                        Set<String> descriptions = desc_to_id.keySet();

                        switch (type) {
                            case without_description:
                                Integer count = 0;
                                buffered_writer.append(code_ts);
                                buffered_writer.append("\t");
                                buffered_writer.append(g_name);
                                buffered_writer.append("\t");
                                buffered_writer.append(origin_country);
                                buffered_writer.append("\t");
                                buffered_writer.append(brand);
                                buffered_writer.append("\t");
                                for (String description : descriptions) {
                                    count += desc_to_id.get(description).size();
                                }
                                buffered_writer.append(count.toString());
                                buffered_writer.append("\t");
                                buffered_writer.append("<");

                                Iterator<String> desc_it = descriptions.iterator();
                                while (desc_it.hasNext()) {
                                    id_set = desc_to_id.get(desc_it.next());
                                    Iterator<String> id_it = id_set.iterator();
                                    while (id_it.hasNext()) {
                                        buffered_writer.append(id_it.next());
                                        if (id_it.hasNext()) {
                                            buffered_writer.append(", ");
                                        }
                                    }
                                    if (desc_it.hasNext()) {
                                        buffered_writer.append(", ");
                                    }
                                }
                                /*
                                for (String description : descriptions) {
                                    id_set = desc_to_id.get(description);
                                    for (String id : id_set) {
                                        buffered_writer.append(id);
                                        buffered_writer.append(", ");
                                    }

                                }
                                */
                                buffered_writer.append(">");
                                buffered_writer.append("\n");
                                buffered_writer.flush();
                                break;
                            case with_description:
                                for (String description : descriptions) {
                                    buffered_writer.append(code_ts);
                                    buffered_writer.append("\t");
                                    buffered_writer.append(g_name);
                                    buffered_writer.append("\t");
                                    buffered_writer.append(origin_country);
                                    buffered_writer.append("\t");
                                    buffered_writer.append(brand);
                                    buffered_writer.append("\t");
                                    buffered_writer.append(description);
                                    buffered_writer.append("\t");
                                    id_set = desc_to_id.get(description);
                                    buffered_writer.append("<");

                                    Iterator<String> it = id_set.iterator();
                                    while (it.hasNext()) {
                                        buffered_writer.append(it.next());
                                        if (it.hasNext()) {
                                            buffered_writer.append(", ");
                                        }
                                    }
                                    /*
                                    for (String id : id_set) {
                                        buffered_writer.append(id);
                                        buffered_writer.append(", ");
                                    }
                                    */
                                    buffered_writer.append(">");
                                    buffered_writer.append("\n");
                                    buffered_writer.flush();
                                }
                                break;
                            default:
                                break;
                        }
                    }
                }
            }
        }
        buffered_writer.close();
        file_writer.close();
        System.out.println("lib recorded!");
    }

    /**
     *
     * @param args [InputFile] [DisplayFile] [StatusFile]
     */
    private static void check_args(String[] args) {
        switch (args.length) {
            case 0:
                break;
            case 1:
                original_file = args[0];
                break;
            case 2:
                original_file = args[0];
                display_file = args[1];
                break;
            case 3:
                original_file = args[0];
                display_file = args[1];
                status_file = args[2];
                break;
            default:
                System.err.println("Error: parameters error!");
                System.err.println("Usage: Gen_lib [InputFile] [DisplayFile] [StatusFile]");
                System.exit(0);
        }
    }

    public static void main(String[] args) {
        check_args(args);
        try {
            read_from_file(original_file);
            record_status(status_file);
            record(display_file, record_type.without_description);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
