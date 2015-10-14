package g_lib;

import java.util.*;

/**
 * Cluster类，表示一个聚类
 */
public class Cluster {
	private String label;
	private LinkedHashMap<String, HashMap<String, Integer>> brand_to_id;
	
	/**
	 * 初始化cluster
	 * @param brand_to_id brand ＝ set<code_ts:g_name:count>
	 */
	public Cluster(String brand, HashMap<String, Integer> id_map) {
		this.brand_to_id = new LinkedHashMap<>();
		brand_to_id.put(brand, id_map);
	}
	
	/**
	 * 和另一个Cluster合并
	 * @param another_cluster 被合并的cluster
	 */
	public void union(Cluster another_cluster) {
		another_cluster.brand_to_id.entrySet().stream().forEach(another_b2i_entry -> {
			String another_brand = another_b2i_entry.getKey();
			HashMap<String, Integer> another_id_map = another_b2i_entry.getValue();
			if (this.brand_to_id.containsKey(another_brand)) { // 当前cluster和要合并的cluster含有相同brand
				HashMap<String, Integer> this_id_map = this.brand_to_id.get(another_brand);
				another_id_map.entrySet().stream().forEach(another_id_entry -> {
					String id = another_id_entry.getKey();
					Integer ano_cnt = another_id_entry.getValue();
					if (this_id_map.containsKey(id)) {
						ano_cnt += this_id_map.get(id);
					}
					this_id_map.put(id, ano_cnt);
				});
			} else { // 新的brand
				this.brand_to_id.put(another_brand, another_id_map);
			}
		});
/*
		for (Map.Entry<String, HashMap<String, Integer>> another_b2i_entry : another_cluster.brand_to_id.entrySet()) {
			String another_brand = another_b2i_entry.getKey();
			HashMap<String, Integer> another_id_map = another_b2i_entry.getValue();
			if (this.brand_to_id.containsKey(another_brand)) { // 当前cluster和要合并的cluster含有相同brand
				HashMap<String, Integer> this_id_map = this.brand_to_id.get(another_brand);
				for (Map.Entry<String, Integer> another_id_entry : another_id_map.entrySet()) {
					String id = another_id_entry.getKey();
					Integer ano_cnt = another_id_entry.getValue();
					if (this_id_map.containsKey(id)) {
						Integer this_cnt = this_id_map.get(id);
						this_id_map.put(id, ano_cnt + this_cnt);
					} else {
						this_id_map.put(id, ano_cnt);
					}
				}
			} else { // 新的brand
				this.brand_to_id.put(another_brand, another_id_map);
			}
		}
		*/
	}

	/**
	 * 重写toString()，便于打印
	 */
	public String toString() {
		StringBuilder result = new StringBuilder();
		result.append("{label: ");
		result.append(this.label);
		result.append("; ");

		Iterator<Map.Entry<String, HashMap<String, Integer>>> brand_iter = this.brand_to_id.entrySet().iterator();
		while (brand_iter.hasNext()) {
			Map.Entry<String, HashMap<String, Integer>> brand_entry = brand_iter.next();
			result.append("[");
			result.append(brand_entry.getKey());
			result.append(": ");
			result.append(brand_entry.getValue().toString());
			result.append("]");
			if (brand_iter.hasNext()) {
				result.append(", ");
			}
		}
		result.append("}");
		return result.toString();
	}

	/**
	 * 根据出现次数最多的brand自动设置推荐label
	 */
	public void auto_set_label() {
		String max_brand = "";
		int max_count = 0;
/*
		this.brand_to_id.entrySet().stream().forEach(brand_entry -> {
			HashMap<String, Integer> id_map = brand_entry.getValue();
			int count = id_map.entrySet().stream().mapToInt(HashMap.Entry::getValue).sum();
			if (count > max_count) {
				max_brand = brand_entry.getKey();
				max_count = count;
			}
		});
*/
		for (Map.Entry<String, HashMap<String, Integer>> brand_entry : this.brand_to_id.entrySet()) {
			HashMap<String, Integer> id_map = brand_entry.getValue();

			int count = id_map.entrySet().stream().mapToInt(HashMap.Entry::getValue).sum();
			if (count > max_count) {
				max_brand = brand_entry.getKey();
				max_count = count;
			}
		}
		this.label = max_brand;
	}
	
	/** get/set methods */
	public void set_label (String label) {
		this.label = label;
	}
	public Integer get_total_count () {
		Integer result = 0;
		for (Map.Entry<String, HashMap<String, Integer>> brand_entry : brand_to_id.entrySet()) {
			for (Map.Entry<String, Integer> id_entry : brand_entry.getValue().entrySet()) {
				result += id_entry.getValue();
			}
		}
		return result;
	}
	public LinkedHashMap<String, HashMap<String, Integer>> get_brand_to_id () {
		return this.brand_to_id;
	}
}