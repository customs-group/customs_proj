package str_rec;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

/**
 * 聚类中间结果类
 */
public class Tuple {
	private static final String clusters_file_name = "./datasets/clusters";
	private ArrayList<Cluster> cluster_list;
	
	public Tuple(ArrayList<Cluster> cluster_list) {
		this.cluster_list = new ArrayList<Cluster>();
		for(Cluster c : cluster_list) {
			ArrayList<String> elements = new ArrayList<String>();
			elements.addAll(c.get_elements());
			Cluster newCluster = new Cluster(elements);
			this.cluster_list.add(newCluster);
		}
	}

	/**
	 * 将两个cluster进行合并
	 * @param cluster1_index
	 * @param cluster2_index
	 */
	public void Union(int cluster1_index, int cluster2_index) {
		if(cluster1_index == cluster2_index) {
			System.out.println("same cluster, doing nothing");
			return;
		}
		// 合并
		this.cluster_list.get(cluster1_index).Union(this.cluster_list.get(cluster2_index));
		// 移除被合并的Cluster
		this.cluster_list.remove(cluster2_index);
    }
	
	public void record() {
		FileWriter clusters_file_writer;
		try {
			clusters_file_writer = new FileWriter(clusters_file_name);
			BufferedWriter clusters_buffered_writer = new BufferedWriter(clusters_file_writer);
	        clusters_buffered_writer.append("total cluster number: " + this.cluster_list.size() + "\n");
	        for(int i = 0; i < this.cluster_list.size(); i++) {
	        	Cluster c = this.cluster_list.get(i);
	        	clusters_buffered_writer.append(c.toString() + "\n");
	        	clusters_buffered_writer.flush();
			}
	        clusters_buffered_writer.close();
	        clusters_file_writer.close();
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
