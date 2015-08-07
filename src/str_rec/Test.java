package str_rec;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;

public class Test {
	private static final String brands_file_name = "./datasets/initial_brands";
	private static final String matrix_file_name = "./datasets/matrix";
	private static final int scaling = 100;
	
	private static ArrayList<String> strings;
	private static float[][] distance_matrix;
	
	private static void init() throws Exception {
		strings = new ArrayList<String>();
		
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
        
        distance_matrix = new float[strings.size()][strings.size()];
        for (int i = 0; i < strings.size(); i++) {
			for (int j = i + 1; j < strings.size(); j ++) {
				distance_matrix[i][j] = ld.getDistance(strings.get(i), strings.get(j));
			}
		}
        
        //debug
        for (int j = 0; j < strings.size(); j++) {
        	for (int i = j + 1; i < strings.size(); i++) {
        		distance_matrix[i][j] = distance_matrix[j][i];
        	}
        }
        
        System.out.println("initialized!");
	}
	
	private static void write_result() throws Exception{
		FileWriter matrix_file_writer = new FileWriter(matrix_file_name);
        BufferedWriter matrix_buffered_writer = new BufferedWriter(matrix_file_writer);
        for (int i = 0; i < distance_matrix.length; i++) {
			for (int j = 0; j < distance_matrix.length; j ++) {
				matrix_buffered_writer.append(distance_matrix[i][j] + "");
				if (j != strings.size() - 1) {
					matrix_buffered_writer.append(", ");
				}
			}
			matrix_buffered_writer.append(";\n");
			matrix_buffered_writer.flush();
		}
        matrix_buffered_writer.close();
        matrix_file_writer.close();
	}
	public static void main(String[] args) {
		try {
			init();
			HierarchicalCluster hc = new HierarchicalCluster(distance_matrix, strings);
			hc.do_clustering();
			hc.PrintResult();
			write_result();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
