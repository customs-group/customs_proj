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

                }
                read_result = buffered_reader.readLine();
            }
            buffered_writer.close();
            file_writer.close();
            buffered_reader.close();
            file_reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
