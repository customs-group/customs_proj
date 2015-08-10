package str_rec;

import java.util.ArrayList;

/**
 * Cluster类，表示一个聚类
 */
public class Cluster {
	private ArrayList<String> brands; // Cluster中的元素
	private ArrayList<Integer> brand_count; // 各个元素的数量
	private int total_count; // 所有元素总数量
	
	/**
	 * 初始化cluster
	 * @param brands cluster内包含的品牌
	 * @param brand_count cluster内所包含的品牌对应出现的次数
	 */
	public Cluster(ArrayList<String> brands, ArrayList<Integer> brand_count) {
		this.brands = brands;
		this.brand_count = brand_count;
		this.total_count = 0;
		for (Integer i : brand_count) {
			this.total_count += i;
		}
	}
	
	/**
	 * 和另一个Cluster合并
	 * @param another_cluster 被合并的cluster
	 */
	public void Union(Cluster another_cluster) {
		this.brands.addAll(another_cluster.brands);
		this.brand_count.addAll(another_cluster.brand_count);
		this.total_count += another_cluster.total_count;
		another_cluster = null;
	}

	/**
	 * 重写toString()，便于打印
	 */
	public String toString() {
		StringBuilder result = new StringBuilder();
		result.append("{");
		for(int i = 0; i < this.brands.size(); i++) {
			result.append(this.brands.get(i) + ": " + this.brand_count.get(i));
			if(i != this.brands.size() - 1) {
				result.append(", ");
			}
		}
		result.append("}");
		return result.toString();
	}
	
	/** set/get methods */
	public ArrayList<String> get_brands() {
		return this.brands;
	}
	public ArrayList<Integer> get_brand_count() {
		return this.brand_count;
	}
	public int get_total_count() {
		return this.total_count;
	}
	
}