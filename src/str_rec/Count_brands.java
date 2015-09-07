package str_rec;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.*;
import java.util.Map.Entry;

public class Count_brands {

    public static void main(String[] args) {
        String in_file;
        String out_file;
        switch (args.length) {
            case 0:
                in_file = "./datasets/original/end";
                out_file = "./datasets/brands_with_counts";
                break;
            case 1:
                in_file = args[0];
                out_file = "./datasets/brands_with_counts";
                break;
            case 2:
                in_file = args[0];
                out_file = args[1];
                break;
            default:
                System.err.println("Error: parameters error!");
                System.err.println("Usage: Preprocess [InputFile] [OutputFile]");
                return;
        }
        try {
            Map<String, Integer> brand_and_count = new HashMap<>();

            FileReader file_reader = new FileReader(in_file);
            BufferedReader buffered_reader = new BufferedReader(file_reader);
            String read_result = buffered_reader.readLine();
            while (read_result != null) {
                String[] result = read_result.split("\t");
                String entry_id = result[0];
                String g_no = result[1];
                String code_ts = result[2];
                String original_country = result[3];
                String g_name = result[4];
                String[] goods_info = result[5].split("@@");
                String brand = goods_info[0];
                String model = goods_info[1];

                if (brand_and_count.containsKey(brand)) {
                    brand_and_count.put(brand, brand_and_count.get(brand) + 1);
                } else {
                    brand_and_count.put(brand, 1);
                }
                read_result = buffered_reader.readLine();
            }

            FileWriter file_writer = new FileWriter(out_file);
            BufferedWriter buffered_writer = new BufferedWriter(file_writer);
            for(Entry<String, Integer> entry : brand_and_count.entrySet()) {
                String key = entry.getKey();
                String val = entry.getValue().toString();
                buffered_writer.append(key);
                buffered_writer.append("\t");
                buffered_writer.append(val);
                buffered_writer.append("\n");
                buffered_writer.flush();
            }

            buffered_writer.close();
            file_writer.close();
            buffered_reader.close();
            file_reader.close();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}