package str_rec;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

/**
 * 每次合并完cluster的中间结果
 */
public class Tuple {
	private ArrayList<Cluster> cluster_list;
	
	/**
	 * 初始化Tuple
	 * @param previous_cluster_list 上一次聚类的结果
	 * 因为需要从上一次聚类结果生成新的Tuple, 需要将cluster_list深拷贝一遍
	 */
	public Tuple(ArrayList<Cluster> previous_cluster_list) {
		this.cluster_list = new ArrayList<>();
		for (Cluster cluster : previous_cluster_list) {
			LinkedHashMap<String, HashSet<Pair>> brand_to_id = new LinkedHashMap<>();
			for (Map.Entry<String, HashSet<Pair>> entry : cluster.get_brand_to_id().entrySet()) {
				HashSet<Pair> id_set = new HashSet<>();
				for (Pair pair : entry.getValue()) {
					Pair _pair = new Pair(pair.get_entry_id(), pair.get_g_no());
					id_set.add(_pair);
				}
				brand_to_id.put(entry.getKey(), id_set);
			}
			Cluster new_cluster = new Cluster(brand_to_id);
			this.cluster_list.add(new_cluster);
		}
	}

	/**
	 * 将两个cluster进行合并
	 * @param cluster1_index 合并的cluster的索引
	 * @param cluster2_index 被合并的cluster的索引
	 */
	public void Union(int cluster1_index, int cluster2_index) {
		if (cluster1_index == cluster2_index) {
			System.out.println("same cluster, doing nothing");
		} else {
			this.cluster_list.get(cluster1_index).Union(this.cluster_list.get(cluster2_index));
			this.cluster_list.remove(cluster2_index); // 移除被合并的Cluster
		}
    }
	
	/**
	 * 将结果记录到文件
	 * @param clusters_file 输出文件名
	 */
	public void record(String clusters_file) {
		try {
			FileWriter file_writer = new FileWriter(clusters_file);
			BufferedWriter buffered_writer = new BufferedWriter(file_writer);
			buffered_writer.append("total cluster count: ");
			buffered_writer.append(Integer.toString(this.cluster_list.size()));
			buffered_writer.append("\n");
			for(Cluster cluster : cluster_list) {
				buffered_writer.append(cluster.toString());
				buffered_writer.append("\n");
				buffered_writer.flush();
			}
	        buffered_writer.close();
	        file_writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/** get/set methods */
	public ArrayList<Cluster> get_cluster_list() {
		return this.cluster_list;
	}
}
