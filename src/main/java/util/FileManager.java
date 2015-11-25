package util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 *
 * Created by edwardlol on 15/10/15.
 */
public class FileManager {

    public static String end_file = "datasets/original/end";
    public static String conta_file = "datasets/original/thm_conta_ei";

    public static String hir_cluster_result = "datasets/clusters";
    public static String km_cluster_result = "datasets/kmclusters";

    public static String lib_display = "datasets/goods_lib";
    public static String lib_status = "datasets/status";

    public static String days_static = "datasets/days";
    public static String conta_static = "datasets/containers";

    public enum key_column {brand, description}

    /**
     * read data from "datasets/original/end"
     * @param end_file file name
     * @param column the column to be the key
     * @return HashMap[key : idset]
     */
    public static HashMap<String, HashSet<String>> read_end(String end_file, key_column column) {
        HashMap<String, HashSet<String>> map = new HashMap<>();
        HashSet<String> id_set;
        try {
            FileReader fr = new FileReader(end_file);
            BufferedReader br = new BufferedReader(fr);
            String line = br.readLine();
            while (line != null) {
                String[] attrs = line.split("\t");
                if (attrs.length != 6) {
                    System.out.println(line);
                    throw new Exception("end file length error!");
                }
                String id = attrs[0] + ":::" + attrs[1];
                String key;
                switch (column) {
                    case brand:
                        key = attrs[5].split("@@")[0];
                        if (key.equals("无")) {
                            line = br.readLine();
                            continue;
                        }
                        break;
                    case description:
                        key = attrs[5].replaceAll("@@", "");
                        break;
                    default:
                        throw new Exception("end file format error!");
                }
                if (map.containsKey(key)) {
                    id_set = map.get(key);
                } else {
                    id_set = new HashSet<>();
                }
                id_set.add(id);
                map.put(key, id_set);
                line = br.readLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    /**
     * read data from "datasets/original/thm_conta_ei"
     * @param conta_file file name
     * @return days array
     */
    public static int[] read_thm(String conta_file) {
        ArrayList<Integer> days_list = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            FileReader fr = new FileReader(conta_file);
            BufferedReader br = new BufferedReader(fr);
            String line = br.readLine();
            while (line != null) {
                String[] attrs = line.split("@@");
                if (attrs.length != 24) {
                    break;
                }
                if (sdf.parse(attrs[3]).getTime() - sdf.parse(attrs[4]).getTime() <= 0) {
                    int days = Integer.parseInt(attrs[5]);
                    days_list.add(days);
                }
                line = br.readLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        int[] days_array = new int[days_list.size()];
        for (int i = 0; i < days_list.size(); i++) {
            days_array[i] = days_list.get(i);
        }
        return days_array;
    }
}
