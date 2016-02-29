package g_lib;

import util.Dict;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;

/**
 * Created by edwardlol on 16/2/22.
 */
public class W2V_test {
    public static void main(String[] args) {

        String file = "datasets/original/end8703";
        String result_file = "datasets/text8";
        try {
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);

            FileWriter fw = new FileWriter(result_file);
            BufferedWriter bw = new BufferedWriter(fw);

            String line = br.readLine();
            while (line != null) {
                String[] contents = line.split("\t");
                if (contents.length == 6) {
                    Dict.get_normalizer_map().forEach(
                            (String key, String value) ->
                                    contents[5] = contents[5].replaceAll(key, value));
                    Dict.get_symbol_set().forEach(
                            (String symbol) ->
                                    contents[5] = contents[5].replaceAll(symbol, " "));
                    bw.append(contents[5]);
                    bw.append(" ");
                    bw.flush();
                }
                line = br.readLine();
            }
            bw.close();
            fw.close();
            br.close();
            fr.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
