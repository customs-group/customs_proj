package str_rec;

import java.util.*;

/**
 * Cluster类，表示一个聚类
 */
public class Cluster {
	private String label;
	private Integer brands_count;
	private LinkedHashMap<String, HashSet<Pair>> brand_to_id;
	
	/**
	 * 初始化cluster
	 * @param brand_to_id brand ＝ set<entry_id:g_no对>
	 */
	public Cluster(LinkedHashMap<String, HashSet<Pair>> brand_to_id) {
		this.brand_to_id = brand_to_id;
		this.brands_count = 0;
		for (HashSet<Pair> id_set : this.brand_to_id.values()) {
			this.brands_count += id_set.size();
		}
	}
	
	/**
	 * 和另一个Cluster合并
	 * @param another_cluster 被合并的cluster
	 */
	public void Union(Cluster another_cluster) {
		for (Map.Entry<String, HashSet<Pair>> entry : another_cluster.brand_to_id.entrySet()) {
			this.brand_to_id.put(entry.getKey(), entry.getValue());
			this.brands_count += entry.getValue().size();
		}
	}

	/**
	 * 重写toString()，便于打印
	 */
	public String toString() {
		StringBuilder result = new StringBuilder();
		result.append("{label: ");
		result.append(this.label);
		result.append("; ");

		Iterator<Map.Entry<String, HashSet<Pair>>> brand_to_id_iter = this.brand_to_id.entrySet().iterator();
		while (brand_to_id_iter.hasNext()) {
			Map.Entry<String, HashSet<Pair>> entry = brand_to_id_iter.next();
			result.append("[");
			result.append(entry.getKey());
			result.append(": ");
			Iterator<Pair> id_set_iter = entry.getValue().iterator();
			while (id_set_iter.hasNext()) {
				result.append(id_set_iter.next().toString());
				if (id_set_iter.hasNext()) {
					result.append(", ");
				}
			}
			result.append("]");
			if (brand_to_id_iter.hasNext()) {
				result.append(", ");
			}
		}
		result.append("}");
		return result.toString();
	}
	
	/** get/set methods */
	public void set_label (String label) {
		this.label = label;
	}
	public Integer get_brands_count () {
		return this.brands_count;
	}
	public LinkedHashMap<String, HashSet<Pair>> get_brand_to_id () {
		return this.brand_to_id;
	}
}