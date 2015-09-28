package g_lib;

import util.LevensteinDistance;

import java.io.*;
import java.util.*;

/**
 * 层次聚类
 */
public class HierarchicalCluster {
	private static ArrayList<Cluster> cluster_list;
	private static float[][] distance_matrix; // 相似度矩阵计算结果
	private static Float threshold = 0.6f; // (0.0 ~ 1.0)
	private static Float scaling = 0.5f; // (0.0 ~ 1.0]
	private static String original_file = "datasets/goods_lib";
	private static String matrix_file = "datasets/matrix";
	private static String clusters_file = "datasets/clusters";

	/**
	 * algorithm initialization
	 * @throws Exception
	 */
	private static void init(String original_file) throws Exception {
		LinkedHashMap<String, HashMap<String, Integer>> brands_map = new LinkedHashMap<>();
		// step1: read hashmap from file
		System.out.println("reading data from file!");
		FileReader file_reader = new FileReader(original_file);
		BufferedReader buffered_reader = new BufferedReader(file_reader);
		String read_result = buffered_reader.readLine();
		while (read_result != null) {
			String[] result = read_result.split("\\t");
			String code_ts = result[0];
			String g_name = result[1];
			String brand = result[3];
			Integer count = 0;
			if (result.length == 5) {
				count = Integer.parseInt(result[4]);
			} else if (result.length == 6) {
				count = Integer.parseInt(result[5]);
			} else {
				System.out.println("data format error! check your original file!");
				System.exit(0);
			}

			String id = code_ts + ":::" + g_name;
			if (brands_map.containsKey(brand)) {
				HashMap<String, Integer> id_map = brands_map.get(brand);
				if (id_map.containsKey(id)) {
					count += id_map.get(id);
				}
				id_map.put(id, count);
			} else {
				HashMap<String, Integer> id_map = new HashMap<>();
				id_map.put(id, count);
				brands_map.put(brand, id_map);
			}
			read_result = buffered_reader.readLine();
		}
		buffered_reader.close();
		file_reader.close();
		System.out.println("file reading done! all data in memory now");
		System.out.println("map size: " + brands_map.size());

		// step2: init tuple list
		System.out.println("initializing tuple");
		cluster_list = new ArrayList<>();
		brands_map.entrySet().stream().forEach(entry -> {
			LinkedHashMap<String, HashMap<String, Integer>> _brand_map = new LinkedHashMap<>();
			_brand_map.put(entry.getKey(), entry.getValue());
			cluster_list.add(new Cluster(_brand_map));
		});
/*
		for (Map.Entry<String, HashMap<String, Integer>> entry : brands_map.entrySet()) {
			LinkedHashMap<String, HashMap<String, Integer>> _brand_map = new LinkedHashMap<>();
			_brand_map.put(entry.getKey(), entry.getValue());
			cluster_list.add(new Cluster(_brand_map));
		}
*/
		Float temp = cluster_list.size() * scaling;
		Integer end_index = temp.intValue();
		cluster_list = new ArrayList<>(cluster_list.subList(0, end_index));

		// step3: init distance matrix
		System.out.println("initializing matrix");
		LevensteinDistance ld = new LevensteinDistance();
		ArrayList<String> brand_list = new ArrayList<>();
		cluster_list.stream().forEach(cluster -> {
			List<String> list = new ArrayList<>(cluster.get_brand_to_id().keySet());
			brand_list.add(list.get(0)); // 初始每个cluster只有一个brand
		});
/*
		for (Cluster cluster : cluster_list) {
			List<String> list = new ArrayList<>(cluster.get_brand_to_id().keySet());
			brand_list.add(list.get(0)); // 初始每个cluster只有一个brand
		}
*/
		distance_matrix = new float[brand_list.size()][brand_list.size()];
		for (int i = 0; i < brand_list.size(); i++) {
			for (int j = i + 1; j < brand_list.size(); j ++) {
				distance_matrix[i][j] = ld.getDistance(brand_list.get(i).toLowerCase(), brand_list.get(j).toLowerCase());
			}
		}
		for (int j = 0; j < brand_list.size(); j++) {
			for (int i = j + 1; i < brand_list.size(); i++) {
				distance_matrix[i][j] = distance_matrix[j][i];
			}
		}
		// end initialization
		System.out.println("initializing done!");
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
				cluster_list.stream().forEach(Cluster::auto_set_label);
				break;
			}
			// step2: update distance matrix
			update_matrix(max_i, max_j);
			// step3: update tuple
			cluster_list.get(max_i).Union(cluster_list.get(max_j));
			cluster_list.remove(max_j); // 移除被合并的Cluster
		}
	}
	
	/**
	 * 重新计算距离矩阵
	 * @param index_i 需要合并的第一个cluster
	 * @param index_j 需要合并的第二个cluster
	 */
	private static void update_matrix(int index_i, int index_j) {
		// 遍历更新距离矩阵右上角
		int cluster_num_i = cluster_list.get(index_i).get_total_count();
		int cluster_num_j = cluster_list.get(index_j).get_total_count();
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


	private static void record_clusters(String clusters_file) throws Exception {
		FileWriter file_writer = new FileWriter(clusters_file);
		BufferedWriter buffered_writer = new BufferedWriter(file_writer);
		buffered_writer.append("total cluster count: ");
		buffered_writer.append(Integer.toString(cluster_list.size()));
		buffered_writer.append("\n");
/*
		cluster_list.stream().forEach(cluster -> {
			try {
				buffered_writer.append(cluster.toString());
				buffered_writer.append("\n");
				buffered_writer.flush();
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
*/
		for (Cluster cluster : cluster_list) {
			buffered_writer.append(cluster.toString());
			buffered_writer.append("\n");
			buffered_writer.flush();
		}
		buffered_writer.close();
		file_writer.close();
	}
	/**
	 * 纪录cluster结果和距离矩阵
	 * @throws IOException
	 */
	public static void record_matrix(String matrix_file) throws IOException {
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

	/**
	 *
	 * @param args [InputFile] [MatrixFile] [clustersFile]
	 */
	private static void check_args(String[] args) {
		switch (args.length) {
			case 0:
				break;
			case 1:
				original_file = args[0];
				break;
			case 2:
				original_file = args[0];
				matrix_file = args[1];
				break;
			case 3:
				original_file = args[0];
				matrix_file = args[1];
				clusters_file = args[2];
				break;
			default:
				System.err.println("Error: parameters error!");
				System.err.println("Usage: HierarchicalCluster [InputFile] [MatrixFile] [clustersFile]");
				System.exit(0);
		}
	}

	public static void set_threshold(Float _threshold) {
		threshold = _threshold;
	}
	public static void set_scaling(Float _scaling) {
		scaling = _scaling;
	}

	public static void main(String[] args) {
		check_args(args);
		try {
			init(original_file);
			do_clustering();
			record_clusters(clusters_file);
			record_matrix(matrix_file);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}