package str_rec;

import util.LevensteinDistance;

import java.io.*;
import java.util.*;

/**
 * 层次聚类
 */
public class HierarchicalCluster {
	private static Tuple tuple; // 保存每次cluster结果集
	private static float[][] distance_matrix; // 相似度矩阵计算结果
	private static final Float threshold = 0.6f; // (0.0 ~ 1.0)
	private static final Float scaling = 1.0f; // (0.0 ~ 1.0]
	private static final String original_file_name = "./datasets/original/end_aa";
	private static final String matrix_file_name = "./datasets/brands_with_counts";
	private static final String clusters_file_name = "./datasets/clusters";

	/**
	 * algorithm initialization
	 * @throws Exception
	 */
	private static void init(String original_file) throws Exception {
		LinkedHashMap<String, HashSet<Pair>> brand_to_id = new LinkedHashMap<>();
		LevensteinDistance ld = new LevensteinDistance();

		// step1: read hashmap from file
		FileReader file_reader = new FileReader(original_file);
		BufferedReader buffered_reader = new BufferedReader(file_reader);
		String read_result = buffered_reader.readLine();
		while (read_result != null) {
			String[] result = read_result.split("\\t");
			String[] good_info = result[5].split("@@");
			String brand = good_info[0];
			String entry_id = result[0];
			String g_no = result[1];

			Pair pair = new Pair(entry_id, g_no);
			if (brand_to_id.containsKey(brand)) {
				brand_to_id.get(brand).add(pair);
			} else {
				HashSet<Pair> id_set = new HashSet<>();
				id_set.add(pair);
				brand_to_id.put(brand, id_set);
			}
			read_result = buffered_reader.readLine();
		}
		buffered_reader.close();
		file_reader.close();
		System.out.println("file read done! data in memory now");

		// step2: init tuple list
		ArrayList<Cluster> cluster_list = new ArrayList<>();
		for (Map.Entry<String, HashSet<Pair>> entry : brand_to_id.entrySet()) {
			LinkedHashMap<String, HashSet<Pair>> _brand_to_id = new LinkedHashMap<>();
			_brand_to_id.put(entry.getKey(), entry.getValue());
			cluster_list.add(new Cluster(_brand_to_id));
		}
		Float temp = cluster_list.size() * scaling;
		Integer end_index = temp.intValue();
		cluster_list = new ArrayList<>(cluster_list.subList(0, end_index));
		tuple = new Tuple(cluster_list);

		// step3: init distance matrix
		ArrayList<String> brand_list = new ArrayList<>();
		for (Cluster cluster : tuple.get_cluster_list()) {
			List<String> list = new ArrayList<>(cluster.get_brand_to_id().keySet());
			brand_list.add(list.get(0)); // 初始每个cluster只有一个brand
		}
		distance_matrix = new float[brand_list.size()][brand_list.size()];
		for (int i = 0; i < brand_list.size(); i++) {
			for (int j = i + 1; j < brand_list.size(); j ++) {
				distance_matrix[i][j] = ld.getDistance(brand_list.get(i), brand_list.get(j));
			}
		}
		for (int j = 0; j < brand_list.size(); j++) {
			for (int i = j + 1; i < brand_list.size(); i++) {
				distance_matrix[i][j] = distance_matrix[j][i];
			}
		}
		// end initialization
		System.out.println("initialized!");
	}

	/**
	 * 进行聚类
	 */
	public static void do_clustering() {
		while(true) {
			float max_similarity = Float.MIN_VALUE;
			int max_i = 0, max_j = 0;
			// step1: 遍历矩阵右上角寻找相似度最大值, j > i
			for(int i = 0; i < distance_matrix.length; i++) {
				for(int j = i + 1; j < distance_matrix.length; j++) {
					if(distance_matrix[i][j] > max_similarity) {
						max_similarity = distance_matrix[i][j];
						max_i = i;
						max_j = j;
					}
				}
			}
			if (max_similarity < threshold) {
				System.out.println("clustering done! max similarity: " + max_similarity + ", matrix size: " + distance_matrix.length);
				break;
			}
			// step2: update distance matrix
			update_matrix(max_i, max_j);
			// step3: update tuple
			tuple.Union(max_i, max_j);
		}
	}
	
	/**
	 * 重新计算距离矩阵
	 * @param index_i 需要合并的第一个cluster
	 * @param index_j 需要合并的第二个cluster
	 */
	private static void update_matrix(int index_i, int index_j) {
		// 遍历更新距离矩阵右上角
		int cluster_num_i = tuple.get_cluster_list().get(index_i).get_brands_count();
		int cluster_num_j = tuple.get_cluster_list().get(index_j).get_brands_count();
		float[][] _distance_matrix = new float[distance_matrix.length - 1][distance_matrix.length - 1];
		for (int i = 0; i < _distance_matrix.length; i++) {
			for (int j = 0; j < _distance_matrix.length; j++) {
				if (i < index_i || (index_i < i && i < index_j)) {
					// 0~index_i 以及 index_i~index_j 列之间
					if (j < index_i || (index_i < j && j < index_j)) {
						// 0～index_i 以及 index_i~index_j 行之间, 直接拷贝
						_distance_matrix[i][j] = distance_matrix[i][j];
					} else if (j == index_i) {
						// 第index_i行, 合并cluster, 更新距离
						_distance_matrix[i][j] = (distance_matrix[i][j] * cluster_num_i
								+ distance_matrix[i][index_j] * cluster_num_j) / (cluster_num_i + cluster_num_j);
					} else if (j >= index_j) {
						// index_j行之后, 上移一行
						_distance_matrix[i][j] = distance_matrix[i][j + 1];
					}
				} else if (i == index_i) {
					// 第index_i列
					if (j < index_j) {
						// index_j行之前, 合并cluster, 更新距离
						_distance_matrix[i][j] = (distance_matrix[i][j] * cluster_num_i
								+ distance_matrix[index_j][j] * cluster_num_j) / (cluster_num_i + cluster_num_j);
					} else if (j >= index_j) {
						// index_j行之后, 更新距离并上移一行
						_distance_matrix[i][j] = (distance_matrix[i][j + 1] * cluster_num_i
								+ distance_matrix[index_j][j + 1] * cluster_num_j) / (cluster_num_i + cluster_num_j);
					}
				} else if (i >= index_j) {
					// index_j列及之后, 整体左移一列
					if (j < index_i || (index_i < j && j < index_j)) {
						// 0～index_i 以及 index_i~index_j 行之间, 左移一列
						_distance_matrix[i][j] = distance_matrix[i + 1][j];
					} else if (j == index_i) {
						// index_i行, 更新距离并左移一行
						_distance_matrix[i][j] = (distance_matrix[i + 1][j] * cluster_num_i
								+ distance_matrix[i + 1][index_j] * cluster_num_j) / (cluster_num_i + cluster_num_j);
					} else if (j >= index_j) {
						// index_j列之后, 左移一列, 上移一行
						_distance_matrix[i][j] = distance_matrix[i + 1][j + 1];
					}
				}
			}
		}
		distance_matrix = _distance_matrix;
	}

	/**
	 * 纪录cluster结果和距离矩阵
	 * @throws IOException 
	 */
	public static void record_result(String clusters_file, String matrix_file) throws IOException {
		// record clusters
		tuple.record(clusters_file);
		// record distance matrix
		FileWriter file_writer = new FileWriter(matrix_file);
        BufferedWriter buffered_writer = new BufferedWriter(file_writer);
        for (float[] row : distance_matrix) {
			for (int j = 0; j < distance_matrix.length; j ++) {
				buffered_writer.append(Float.toString(row[j]));
				if (j != distance_matrix.length - 1) {
					buffered_writer.append(", ");
				}
			}
			buffered_writer.append(";\n");
			buffered_writer.flush();
		}
        buffered_writer.close();
        file_writer.close();
	}

	public static void main(String[] args) {
		String original_file;
		String matrix_file;
		String clusters_file;
		switch (args.length) {
			case 0:
				original_file = original_file_name;
				matrix_file = matrix_file_name;
				clusters_file = clusters_file_name;
				break;
			case 1:
				original_file = args[0];
				matrix_file = matrix_file_name;
				clusters_file = clusters_file_name;
				break;
			case 2:
				original_file = args[0];
				matrix_file = args[1];
				clusters_file = clusters_file_name;
				break;
			case 3:
				original_file = args[0];
				matrix_file = args[1];
				clusters_file = args[2];
				break;
			default:
				System.err.println("Error: parameters error!");
				System.err.println("Usage: Preprocess [InputFile] [OutputFile]");
				return;
		}
		try {
			init(original_file);
			do_clustering();
			record_result(clusters_file, matrix_file);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}