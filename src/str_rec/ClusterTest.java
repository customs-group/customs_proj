package str_rec;

import java.io.*;
import java.util.*;

class Max {
	int max_i;
	int max_j;
	float max_similarity;
	Cluster cluster_i;
	Cluster cluster_j;
	
	public Max() {
		max_i = 0;
		max_j = 0;
		max_similarity = 0.0f;
	}
}

public class ClusterTest {
	private static final String brands_file_name = "./datasets/initial_brands";
	private static final String matrix_file_name = "./datasets/matrix";
	private static final String clusters_file_name = "./datasets/clusters";
	private static final int scaling = 100;
	private static final float threshold = 0.6f;
	
	private static ArrayList<String> strings;
	private static ArrayList<Cluster> cluster_list;
	
	private static void init() throws Exception {
		strings = new ArrayList<String>();
		cluster_list = new ArrayList<Cluster>();
		
        ArrayList<String> _strings = new ArrayList<String>();
		LevensteinDistance ld = new LevensteinDistance();
		
    	FileReader file_reader = new FileReader(brands_file_name);
    	BufferedReader buffered_reader = new BufferedReader(file_reader);
    	String brand = buffered_reader.readLine();
        while (brand != null) {
        	_strings.add(brand);
        	brand = buffered_reader.readLine();
        }
        strings.addAll(_strings.subList(0, _strings.size() / scaling));
        buffered_reader.close();
        file_reader.close();
        
        FileWriter matrix_file_writer = new FileWriter(matrix_file_name);
        BufferedWriter matrix_buffered_writer = new BufferedWriter(matrix_file_writer);
        for (int i = 0; i < strings.size(); i++) {
			for (int j = i + 1; j < strings.size(); j ++) {
				Float distance = ld.getDistance(strings.get(i), strings.get(j));
				matrix_buffered_writer.append(distance.toString());
				if (j != strings.size() - 1) {
					matrix_buffered_writer.append(", ");
				}
			}
			matrix_buffered_writer.append(";\n");
			matrix_buffered_writer.flush();
		}
        matrix_buffered_writer.close();
        matrix_file_writer.close();
		
        FileWriter clusters_file_writer = new FileWriter(clusters_file_name);
        BufferedWriter clusters_buffered_writer = new BufferedWriter(clusters_file_writer);
        for (int i = 0; i < strings.size(); i++) {
			clusters_buffered_writer.append(strings.get(i) + "\n");
			clusters_buffered_writer.flush();
			ArrayList<String> brands = new ArrayList<String>();
			brands.add(strings.get(i));
			Cluster cluster = new Cluster(brands);
			cluster_list.add(cluster);
		}
        clusters_buffered_writer.close();
        clusters_file_writer.close();
		
		System.out.println("initialized!");
	}

	private static Max matrix_search() throws Exception {
		Max max = new Max();
		int i = 0;
		FileReader file_reader = new FileReader(matrix_file_name);
    	BufferedReader buffered_reader = new BufferedReader(file_reader);
    	String similarities = buffered_reader.readLine();
        while (similarities != null) {
        	String[] temps = similarities.split(", ");
        	for (int j = 0; j < temps.length; j ++) {
        		float tempf = Float.parseFloat(temps[j]);
        		if (tempf > max.max_similarity) {
        			max.max_i = i;
        			max.max_j = i + j;
        			max.max_similarity = tempf;
        		}
        	}
        	i++;
        	similarities = buffered_reader.readLine();
        }
        buffered_reader.close();
        file_reader.close();
        
        max.cluster_i = cluster_list.get(max.max_i);
        max.cluster_j = cluster_list.get(max.max_j);
		return max;
	}
	
	private static void update_matrix(Max max) throws Exception {
		int i = 0;
		FileReader file_reader = new FileReader(matrix_file_name);
    	BufferedReader buffered_reader = new BufferedReader(file_reader);
    	String similarities = buffered_reader.readLine();
        while (similarities != null) {
        	String[] temp_string = similarities.split(", ");
        	float[] temp_float = new float[temp_string.length];
        	for (int j = 0; j < temp_string.length; j ++) {
        		temp_float[j] = Float.parseFloat(temp_string[j]);        		
        	}
        	temp_float[max.max_i] = (temp_float[max.max_i] * max.cluster_i.get_ele_num() + temp_float[max.max_j - i] * max.cluster_j.get_ele_num())
        			/ (max.cluster_i.get_ele_num() + max.cluster_j.get_ele_num());
        	for (int j = max.max_j - i; j < temp_float.length - 1; j++) {
        		temp_float[j] = temp_float[j + 1];
        	}
        	temp_float[temp_float.length - 1] = 0;
        	i++;
        	similarities = buffered_reader.readLine();
        }
        buffered_reader.close();
        file_reader.close();
	}
	
	private static void update_clusters(Max max) {
		
	}
	
	private static void do_cluster() throws Exception {
		while (true) {
			Max max = matrix_search();
			if (max.max_similarity < threshold) {
				break;				
			}
			update_matrix(max);
			update_clusters(max);
		}
	}
	
	
	public static void main(String[] args) {
		
        try {
        	init();
        	do_cluster();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
	}
}
