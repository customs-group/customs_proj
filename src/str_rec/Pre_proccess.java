package str_rec;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.*;
import java.util.Map.Entry;

public class Pre_proccess {
	private static final String source_file = "/Users/edwardlol/Downloads/data/out";
	private static final String target_file = "/Users/edwardlol/Downloads/data/tar";
	private static Map<String, Integer> brand_and_count;
	
	public static void main(String[] args) {
		try {
			FileReader file_reader = new FileReader(source_file);
	    	BufferedReader buffered_reader = new BufferedReader(file_reader);
	    	
	    	brand_and_count = new HashMap<String, Integer>();
	    	
	    	String read_result = buffered_reader.readLine();
	    	while (read_result != null) {
	        	String[] result = read_result.split("@@");
	        	String key = result[0];
	        	if (brand_and_count.containsKey(key)) {
	        		brand_and_count.put(key, brand_and_count.get(key) + 1);
	        	} else {
	        		brand_and_count.put(key, 1);
	        	}
	        	read_result = buffered_reader.readLine();
	        }

			FileWriter file_writer = new FileWriter(target_file);
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
