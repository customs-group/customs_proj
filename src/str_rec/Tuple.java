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
	 * @param cluster_list 上一次聚类的结果
	 * 因为需要从上一次聚类结果生成新的Tuple, 需要将cluster_list深拷贝一遍
	 */
	public Tuple(ArrayList<Cluster> cluster_list) {
		this.cluster_list = new ArrayList<Cluster>();
		for(Cluster cluster : cluster_list) {
			ArrayList<String> brands = new ArrayList<String>();
			ArrayList<Integer> brand_count = new ArrayList<Integer>();
			brands.addAll(cluster.get_brands());
			brand_count.addAll(cluster.get_brand_count());
			Cluster new_cluster = new Cluster(brands, brand_count);
			this.cluster_list.add(new_cluster);
		}
	}

	/**
	 * 将两个cluster进行合并
	 * @param cluster1_index 合并的cluster的索引
	 * @param cluster2_index 被合并的cluster的索引
	 */
	public void Union(int cluster1_index, int cluster2_index) {
		if(cluster1_index == cluster2_index) {
			System.out.println("same cluster, doing nothing");
			return;
		}
		this.cluster_list.get(cluster1_index).Union(this.cluster_list.get(cluster2_index));
		// 移除被合并的Cluster
		this.cluster_list.remove(cluster2_index);
    }
	
	/**
	 * 将结果记录到文件
	 * @param clusters_file_name 输出文件名
	 */
	public void record(String clusters_file_name) {
		try {
			FileWriter file_writer = new FileWriter(clusters_file_name);
			BufferedWriter buffered_writer = new BufferedWriter(file_writer);
			buffered_writer.append("total cluster count: " + this.cluster_list.size() + "\n");
	        for(int i = 0; i < this.cluster_list.size(); i++) {
	        	Cluster cluster = this.cluster_list.get(i);
	        	buffered_writer.append(cluster.toString() + "\n");
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
