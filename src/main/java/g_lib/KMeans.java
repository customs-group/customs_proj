package g_lib;

import util.FileManager;
import util.LevensteinDistance;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * Created by edwardlol on 15/10/27.
 */
public class KMeans {
    private static ArrayList<Cluster> cluster_list;

    /**
     * 算法初始化
     * @param centers 初始中心点
     */
    public static void init(String[] centers) {
        System.out.println("initializing...");
        cluster_list = new ArrayList<>();
        for (String center : centers) {
            HashSet<String> id_set = new HashSet<>();
            id_set.add("0:::0");
            Cluster cluster = new Cluster(center, id_set);
            cluster_list.add(cluster);
        }
    }
    public static void init(String[][] centers) {
        System.out.println("initializing...");
        cluster_list = new ArrayList<>();
        for (String[] center : centers) {
            HashMap<String, HashSet<String>> map = new HashMap<>();
            for (String center_brand : center) {
                HashSet<String> id_set = new HashSet<>();
                id_set.add("0:::0");
                map.put(center_brand, id_set);
            }
            Cluster cluster = new Cluster(map);
            cluster_list.add(cluster);
        }
        for (int i = 0; i < cluster_list.size(); i++) {
            System.out.print("center " + i + ": ");
            for (String aaa : cluster_list.get(i).get_keys()) {
                System.out.print(aaa);
                if (i < cluster_list.size()) {
                    System.out.print(", ");
                }
            }
            System.out.println();
        }
        System.out.println("initializing done!");
    }

    /**
     * 从end文件中读取cluster
     * @param line end文件的一行
     * @return cluster
     * @throws Exception
     */
    public static Cluster read_cluster_line(String line)throws Exception {
        HashMap<String, HashSet<String>> map = new HashMap<>();
        HashSet<String> id_set = new HashSet<>();
        String label = "";
        Pattern pattern = Pattern.compile("^\\{label: (.*?); <(.*?)>\\}$");
        Matcher matcher = pattern.matcher(line);
        if (matcher.find()) {
            label = matcher.group(1);
            String id_str = matcher.group(2);
            String[] tuples = id_str.split(">, <");
            for (String tuple : tuples) {
                Pattern tuple_pattern = Pattern.compile("^(.*?): \\[(.*?)\\]$");
                Matcher tuple_matcher = tuple_pattern.matcher(tuple);
                if (tuple_matcher.find()) {
                    String brand = tuple_matcher.group(1);
                    String _ids = tuple_matcher.group(2);
                    String[] ids = _ids.split(", ");
                    id_set.addAll(Arrays.asList(ids));
                    map.put(brand, id_set);
                } else {
                    throw new Exception("cluster file format error!");
                }
            }
        }
        Cluster cluster = new Cluster(map);
        cluster.set_label(label);
        return cluster;
    }

    /**
     * 算法主体
     * @throws Exception
     */
    public static void do_clustering() throws Exception {
        FileReader fr = new FileReader(FileManager.end_file);
        BufferedReader br = new BufferedReader(fr);
        LevensteinDistance ld = new LevensteinDistance();
        String line = br.readLine(); // 第一行是sb
        line = br.readLine();
        while (line != null) {
            Cluster cluster = read_cluster_line(line);
            System.out.println("brand: " + cluster.get_label());
            float temp = 0;
            int temp_index = 0;
            for (int i = 0; i < cluster_list.size(); i++) {
                float dist = 0;
                System.out.println("center" + i);
                Cluster center = cluster_list.get(i);
                for (String brand : center.get_keys()) {
                    float _dist = ld.getDistance(cluster.get_label(), brand);
                    dist += _dist;
                    System.out.println("center brand: " + brand + "; dist: " + _dist);
                }
                dist /= center.get_keys().size();
                System.out.println("center dist: " + dist);
                if (dist > temp) {
                    temp = dist;
                    temp_index = i;
                }
            }
            // 归入index类
            cluster_list.get(temp_index).union(cluster);
            System.out.println("label: " + cluster.get_label() + "; max dist: " + temp + "; index: " + temp_index);
            for (int i = 0; i < cluster_list.size(); i++) {
                Cluster center = cluster_list.get(i);
                System.out.print("center " + i + ": ");
                for (String brand : center.get_keys()) {
                    System.out.print(brand + " ");
                }
                System.out.println();
            }
            line = br.readLine();
        }

    }

    /**
     * 纪录cluster结果
     * @param clusters_file cluster文件
     * @throws Exception
     */
    private static void record_clusters(String clusters_file, Cluster.record_type type) throws Exception {
        FileWriter file_writer = new FileWriter(clusters_file);
        BufferedWriter buffered_writer = new BufferedWriter(file_writer);
        buffered_writer.append("total cluster count: ");
        buffered_writer.append(Integer.toString(cluster_list.size()));
        buffered_writer.append("\n");

        for (Cluster cluster : cluster_list) {
            buffered_writer.append(cluster.toString(type));
            buffered_writer.append("\n");
            buffered_writer.flush();
        }
        buffered_writer.close();
        file_writer.close();
    }

    /**
     * 检查输入参数
     * @param args [InputFile] [clustersFile]
     */
    private static void check_args(String[] args) {
        switch (args.length) {
            case 0:
                break;
            case 1:
                FileManager.end_file = args[0];
                break;
            case 2:
                FileManager.end_file = args[0];
                FileManager.km_cluster_result = args[1];
                break;
            default:
                System.err.println("Error: parameters error!");
                System.err.println("Usage: KMeans [InputFile] [clustersFile]");
                System.exit(0);
        }
    }

    public static void main(String[] args) {
        check_args(args);
        String[] centers1 = {
                "本田",
                "honda",
                "丰田",
                "toyota"
        };
        String[][] centers2 = {
                {"本田", "honda"},
                {"丰田", "toyota"}
        };
        init(centers2);
        try {
            do_clustering();
            record_clusters(FileManager.km_cluster_result, Cluster.record_type.without_id);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
