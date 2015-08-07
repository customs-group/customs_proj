package str_rec;

import java.util.ArrayList;

/**
 * 层次聚类Class
 * @author Jason Han
 *
 */
public class HierarchicalCluster {
	private ArrayList<String> original_brands; // 初始所有品牌名
	private float[][] origion_distance_matrix; // 初始相似度矩阵
	private float[][] distance_matrix; // 相似度矩阵
	private ArrayList<Tuple> tuple_list; // 保存每次cluster结果集
	private static final float threshold = 0.6f;
	
	/**
	 * 算法初始化
	 * @param distanceMatrix 距离矩阵
	 * @param elementNames 元素集
	 */
	public HierarchicalCluster(float[][] distance_matrix, ArrayList<String> original_brands) {
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

		// deep copy
		this.tuple_list = new ArrayList<Tuple>();
		ArrayList<Cluster> cluster_set = new ArrayList<Cluster>();
		for(int i = 0; i < original_brands.size(); i++) {
			ArrayList<String> brands = new ArrayList<String>();
			brands.add(original_brands.get(i));
			cluster_set.add(new Cluster(brands));
		}
		Tuple first_tuple = new Tuple(cluster_set);
		this.tuple_list.add(first_tuple);
	}

	/**
	 * 进行聚类
	 * @param type 聚类的方式
	 */
	public void do_clustering() {
		while(true) {
			float max_similarity = Float.MIN_VALUE;
			int max_i = 0, max_j = 0;
			// 遍历矩阵右上角, j > i
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
			//tuple.Union(pre_cluster_set.get(max_i), pre_cluster_set.get(max_j));
			tuple.Union(max_i, max_j);
			// 添加tuple
			this.tuple_list.add(tuple);

			this.update_matrix(this.distance_matrix, max_i, max_j);//更新距离矩阵
		}
	}

	
	/**
	 * 重新计算距离矩阵
	 * @param distanceMatrix 需要更新的距离矩阵
	 * @param tuple 目前的tuple
	 * @param type 算法方式（Single Link等等）
	 */
	private void update_matrix(float[][] distance_matrix, int index_i, int index_j) {
		// 遍历更新距离矩阵右上角
		int cluster_num_i = this.tuple_list.get(this.tuple_list.size() - 2).get_cluster_list().get(index_i).get_ele_num();
		int cluster_num_j = this.tuple_list.get(this.tuple_list.size() - 2).get_cluster_list().get(index_j).get_ele_num();
		float[][] _distance_matrix = new float[distance_matrix.length - 1][distance_matrix.length - 1];
		for (int i = 0; i < _distance_matrix.length; i++) {
			for (int j = 0; j < _distance_matrix.length; j++) {
				if (i < index_i) {
					if (j < index_i) {
						_distance_matrix[i][j] = distance_matrix[i][j];
					} else if (j == index_i) {
						_distance_matrix[i][j] = (distance_matrix[i][j] * cluster_num_i
								+ distance_matrix[i][index_j] * cluster_num_j) / (cluster_num_i + cluster_num_j);
					} else if (index_i < j && j < index_j) {
						_distance_matrix[i][j] = distance_matrix[i][j];
					} else if (j >= index_j) {
						_distance_matrix[i][j] = distance_matrix[i][j + 1];
					}
				} else if (i == index_i) {
					if (j < index_j) {
						_distance_matrix[i][j] = (distance_matrix[i][j] * cluster_num_i
								+ distance_matrix[index_j][j] * cluster_num_j) / (cluster_num_i + cluster_num_j);
					} else if (j >= index_j) {
						_distance_matrix[i][j] = (distance_matrix[i][j + 1] * cluster_num_i
								+ distance_matrix[index_j][j + 1] * cluster_num_j) / (cluster_num_i + cluster_num_j);
					}
				} else if (index_i < i && i < index_j) {
					if (j < index_i) {
						_distance_matrix[i][j] = distance_matrix[i][j];
					} else if (j == index_i) {
						_distance_matrix[i][j] = (distance_matrix[i][j] * cluster_num_i
								+ distance_matrix[i][index_j] * cluster_num_j) / (cluster_num_i + cluster_num_j);
					} else if (index_i < j && j < index_j) {
						_distance_matrix[i][j] = distance_matrix[i][j];
					} else if (j >= index_j) {
						_distance_matrix[i][j] = distance_matrix[i][j + 1];
					}
				} else if (i >= index_j) {
					if (j < index_i) {
						_distance_matrix[i][j] = distance_matrix[i + 1][j];
					} else if (j == index_i) {
						_distance_matrix[i][j] = (distance_matrix[i + 1][j] * cluster_num_i
								+ distance_matrix[i + 1][index_j] * cluster_num_j) / (cluster_num_i + cluster_num_j);
					} else if (index_i < j && j < index_j) {
						_distance_matrix[i][j] = distance_matrix[i][j + 1];
					} else if (j >= index_j) {
						_distance_matrix[i][j] = distance_matrix[i + 1][j + 1];
					}
				}
			}
		}
		this.distance_matrix = _distance_matrix;
		
	}
	
	/**
	 * 打印结果
	 */
	public void PrintResult() {
		System.out.println(this.tuple_list.get(this.tuple_list.size() - 1).toString());
		this.tuple_list.get(this.tuple_list.size() - 1).record();
	}
	
	public float[][] get_origion_distance_matrix() {
		return this.origion_distance_matrix;
	}
	public ArrayList<String> get_original_brands() {
		return this.original_brands;
	}
}