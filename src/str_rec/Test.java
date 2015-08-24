package str_rec;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

public class Test {
	private static final String brands_file_name = "/Users/LU/Downloads/ALL_CODE/tar";
	private static final String matrix_file_name = "/Users/LU/Downloads/ALL_CODE/matrix";
	private static final String clusters_file_name = "/Users/LU/Downloads/ALL_CODE/clusters";
	private static final int scaling = 3;
	
	private static HierarchicalCluster hierarchical_cluster;
	
	/**
	 * 算法初始化
	 * @throws Exception
	 */
	private static void init(String in) throws Exception {
        ArrayList<String> total_read_brands = new ArrayList<>(); // 读取到的所有品牌
        ArrayList<Integer> total_read_counts = new ArrayList<>(); // 读取到的所有品牌的对应出现次数
        
		ArrayList<String> initial_brands = new ArrayList<>(); // 提取一部分品牌用于算法测试
		ArrayList<Integer> initial_counts = new ArrayList<>(); // 提取出的品牌对应的出现次数
        
		LevensteinDistance ld = new LevensteinDistance();
		
    	FileReader file_reader = new FileReader(in);
    	BufferedReader buffered_reader = new BufferedReader(file_reader);
    	String brand_with_count = buffered_reader.readLine();
        while (brand_with_count != null) {
        	String[] result = brand_with_count.split("\\t");
        	total_read_brands.add(result[0]);
        	total_read_counts.add(Integer.parseInt(result[1]));
        	brand_with_count = buffered_reader.readLine();
        }
        initial_brands.addAll(total_read_brands.subList(0, total_read_brands.size() / scaling));
        initial_counts.addAll(total_read_counts.subList(0, total_read_counts.size() / scaling));
        buffered_reader.close();
        file_reader.close();
        
        float[][] distance_matrix = new float[initial_brands.size()][initial_brands.size()];
        for (int i = 0; i < initial_brands.size(); i++) {
			for (int j = i + 1; j < initial_brands.size(); j ++) {
				distance_matrix[i][j] = ld.getDistance(initial_brands.get(i), initial_brands.get(j));
			}
		}
        
        // 将上三角矩阵复制为完整矩阵
        for (int j = 0; j < initial_brands.size(); j++) {
        	for (int i = j + 1; i < initial_brands.size(); i++) {
        		distance_matrix[i][j] = distance_matrix[j][i];
        	}
        }
        hierarchical_cluster = new HierarchicalCluster(distance_matrix, initial_brands, initial_counts);
        System.out.println("initialized!");
	}

	public static void main(String[] args) {

		if (args.length != 3) {
			System.err.println("Error:: Missing parameters!");
			System.err.println("Test: brands_file_name cluster_file_name matrix_file_name");
			return;
		}
		try {
			init(args[0]);
			hierarchical_cluster.do_clustering();
			hierarchical_cluster.record_result(args[1],args[2]);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
