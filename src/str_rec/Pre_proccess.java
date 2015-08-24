package str_rec;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.*;
import java.util.Map.Entry;

public class Pre_proccess {

    public static void main(String[] args) {

        if (args.length != 2) {
            System.err.println("Error:: Missing parameters!");
            System.err.println("PreProcess: InputFile OutputFile");
            return;
        }
        try {
            String in_file = args[0];
            String out_file = args[1];
            Map<String, Integer> brand_and_count = new HashMap<>();

            FileReader file_reader = new FileReader(in_file);
            BufferedReader buffered_reader = new BufferedReader(file_reader);

            String read_result = buffered_reader.readLine();
            while (read_result != null) {
                if (!read_result.equals("@@")) {
                    String[] result = read_result.split("@@");
                    String key = result[0];
                    if (brand_and_count.containsKey(key)) {
                        brand_and_count.put(key, brand_and_count.get(key) + 1);
                    } else {
                        brand_and_count.put(key, 1);
                    }
                } else {
                    System.out.println(read_result);
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