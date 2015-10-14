package bak;

import bak.Display_item;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;

/**
 * Created by edwardlol on 15/8/31.
 */
public class Display_list {
    private ArrayList<Display_item> item_list;

    public Display_list() {
        this.item_list = new ArrayList<>();
    }
    public void add(Display_item item) {
        this.item_list.add(item);
    }
    public Boolean contains(Display_item another_item) {
        for (Display_item item : item_list) {
            if (another_item.equals(item)) {
                return true;
            }
        }
        return false;
    }
    public void record_to_file(String file_name) throws Exception {
        FileWriter file_writer = new FileWriter(file_name);
        BufferedWriter buffered_writer = new BufferedWriter(file_writer);

        for(Display_item item : this.item_list) {
            buffered_writer.append(item.getCode_ts());
            buffered_writer.append("\t");
            buffered_writer.append(item.getG_name());
            buffered_writer.append("\t\t");
            buffered_writer.append(item.getOrigin_country());
            buffered_writer.append("\t");
            buffered_writer.append(item.getFinal_brand());
            buffered_writer.append("\n");
            buffered_writer.flush();
        }
        buffered_writer.close();
        file_writer.close();
    }
}
