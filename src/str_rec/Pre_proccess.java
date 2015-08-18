package str_rec;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.*;
import java.util.Map.Entry;

public class Pre_proccess {
//    private static final String source_file = "/Users/LU/Downloads/ALL_CODE/out";
//    private static final String target_file = "/Users/LU/Downloads/ALL_CODE/tar";
    private static String in_file = "";
    private static String out_file = "";

    private static Map<String, Integer> brand_and_count;

    public static void main(String[] args) {

        if (args.length != 2){
            System.err.println("Input File: " + args[0] + '\t' + "Output File: " + args[1]);
            System.err.println("Error:: Missing parameters!");
            return;
        }
        try {
            in_file = args[0];
            out_file = args[1];

            FileReader file_reader = new FileReader(in_file);
            BufferedReader buffered_reader = new BufferedReader(file_reader);

            brand_and_count = new HashMap<String, Integer>();

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
                    return;
                }
                read_result = buffered_reader.readLine();
            }

            FileWriter file_writer = new FileWriter(out_file);
            BufferedWriter matrix_buffered_writer = new BufferedWriter(file_writer);

            Iterator<Entry<String, Integer>> iter = brand_and_count.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry<String, Integer> entry = iter.next();
                Object key = entry.getKey();
                Object val = entry.getValue();
                matrix_buffered_writer.append(key + "\t" + val + "\n");
                matrix_buffered_writer.flush();
            }

            matrix_buffered_writer.close();
            file_writer.close();
            buffered_reader.close();
            file_reader.close();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
