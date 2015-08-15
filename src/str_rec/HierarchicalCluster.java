package str_rec;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * 层次聚类
 */
public class HierarchicalCluster {
	private ArrayList<String> original_brands; // 初始所有品牌名
	private ArrayList<Tuple> tuple_list; // 保存每次cluster结果集
	private float[][] origion_distance_matrix; // 初始相似度矩阵
	private float[][] distance_matrix; // 相似度矩阵计算结果
	private static final float threshold = 0.6f;
	
	/**
	 * 算法初始化
	 * @param distanceMatrix 初始距离矩阵
	 * @param original_brands 初始品牌列表
	 * @param brand_count 初始品牌列表所对应品牌数量
	 */
	public HierarchicalCluster(float[][] distance_matrix, ArrayList<String> original_brands, ArrayList<Integer> brand_count) {
		// error check
		if(distance_matrix.length <= 0
				|| distance_matrix.length != distance_matrix[0].length
				|| original_brands.size() != distance_matrix.length) {
			System.out.println("数据有误");
			return;
		}

		this.original_brands = original_brands;
		this.origion_distance_matrix = distance_matrix;
		this.distance_matrix = distance_matrix;

		// init tuple list
		this.tuple_list = new ArrayList<Tuple>();
		ArrayList<Cluster> cluster_list = new ArrayList<Cluster>();
		for(int i = 0; i < original_brands.size(); i++) {
			ArrayList<String> _brands = new ArrayList<String>();
			ArrayList<Integer> _brand_count = new ArrayList<Integer>();
			_brands.add(original_brands.get(i));
			_brand_count.add(brand_count.get(i));
			cluster_list.add(new Cluster(_brands, _brand_count));
		}
		Tuple first_tuple = new Tuple(cluster_list);
		this.tuple_list.add(first_tuple);
	}

	/**
	 * 进行聚类
	 */
	public void do_clustering() {
		while(true) {
			float max_similarity = Float.MIN_VALUE;
			int max_i = 0, max_j = 0;
			// 遍历矩阵右上角寻找相似度最大值, j > i
			for(int i = 0; i < this.distance_matrix.length; i++) {
				for(int j = i + 1; j < this.distance_matrix.length; j++) {
					if(this.distance_matrix[i][j] > max_similarity) {
						max_similarity = this.distance_matrix[i][j];
						max_i = i;
						max_j = j;
					}
				}
			}
			if (max_similarity < threshold) {
				System.out.println("clustering done! max similarity: " + max_similarity + ", matrix size: " + this.distance_matrix.length);
				break;
			}
			// 旧的cluster集
			ArrayList<Cluster> pre_cluster_list = this.tuple_list.get(this.tuple_list.size() - 1).get_cluster_list();
			// 新的tuple
			Tuple tuple = new Tuple(pre_cluster_list);
			tuple.Union(max_i, max_j);
			// 添加tuple
			this.tuple_list.add(tuple);
			// 更新距离矩阵
			this.update_matrix(this.distance_matrix, max_i, max_j);
		}
	}
	
	/**
	 * 重新计算距离矩阵
	 * @param distanceMatrix 需要更新的距离矩阵
	 * @param index_i 需要合并的第一个cluster
	 * @param index_j 需要合并的第二个cluster
	 */
	private void update_matrix(float[][] distance_matrix, int index_i, int index_j) {
		// 遍历更新距离矩阵右上角
		int cluster_num_i = this.tuple_list.get(this.tuple_list.size() - 2).get_cluster_list().get(index_i).get_total_count();
		int cluster_num_j = this.tuple_list.get(this.tuple_list.size() - 2).get_cluster_list().get(index_j).get_total_count();
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
		this.distance_matrix = _distance_matrix;
	}
	
	/**
	 * 纪录cluster结果和距离矩阵
	 * @throws IOException 
	 */
	public void record_result(String clusters_file_name, String matrix_file_name) throws IOException {
		// record clusters
		this.tuple_list.get(this.tuple_list.size() - 1).record(clusters_file_name);
		// record distance matrix
		FileWriter file_writer = new FileWriter(matrix_file_name);
        BufferedWriter buffered_writer = new BufferedWriter(file_writer);
        float[][] distance_matrix = this.get_distance_matrix();
        for (int i = 0; i < distance_matrix.length; i++) {
			for (int j = 0; j < distance_matrix.length; j ++) {
				buffered_writer.append(distance_matrix[i][j] + "");
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
	
	/** get/set mothods */
	public float[][] get_origion_distance_matrix() {
		return this.origion_distance_matrix;
	}
	public float[][] get_distance_matrix() {
		return this.distance_matrix;
	}
	public ArrayList<String> get_original_brands() {
		return this.original_brands;
	}
}