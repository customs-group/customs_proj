package g_lib;

import java.util.*;

/**
 * Cluster类，表示一个聚类
 */
public class Cluster {
	public enum record_type {with_id, without_id}
	private Boolean flag;
	private String label;
	private HashMap<String, HashSet<String>> brand_to_ids;

	/**
	 * 初始化cluster
	 * @param brand 读取的品牌
	 * @param id_set 该品牌下的id_set
	 */
	public Cluster(String brand, HashSet<String> id_set) {
		this.brand_to_ids = new HashMap<>();
		this.brand_to_ids.put(brand, id_set);
		this.auto_set_label();
		this.flag = false;
	}

	/**
	 * 通过map初始化cluster
	 * @param _brand_to_ids 创建好的brand map
	 */
	public Cluster(HashMap<String, HashSet<String>> _brand_to_ids) {
		this.brand_to_ids = _brand_to_ids;
	}

	/**
	 * 向cluster下添加品牌
	 * @param brand 需添加的品牌
	 * @param id_set 该品牌下的id_set
	 */
	public void add_brand(String brand, HashSet<String> id_set) {
		if (this.brand_to_ids.containsKey(brand)) {
			this.brand_to_ids.get(brand).addAll(id_set);
		} else {
			this.brand_to_ids.put(brand, id_set);
		}
	}

	public void add_map(HashMap<String, HashSet<String>> another_brand_to_ids) {
		another_brand_to_ids.forEach(this::add_brand);
	}

	/**
	 * 和另一个Cluster合并
	 * @param another_cluster 被合并的cluster
	 */
	public void union(Cluster another_cluster) {
		another_cluster.brand_to_ids.forEach(this::add_brand);
	}

	/**
	 * 重写toString()，便于打印
	 */
	public String toString(record_type type) {
		StringBuilder result = new StringBuilder();
		result.append("{label: ");
		result.append(this.label);
		result.append("; ");

		Iterator<Map.Entry<String, HashSet<String>>> brand_iter = this.brand_to_ids.entrySet().iterator();
		while (brand_iter.hasNext()) {
			Map.Entry<String, HashSet<String>> brand_entry = brand_iter.next();
			result.append("<");
			result.append(brand_entry.getKey());
			if (type == record_type.with_id) {
				result.append(": ");
				result.append(brand_entry.getValue().toString());
			}
			result.append(">");
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
		for (Map.Entry<String, HashSet<String>> brand_entry : this.brand_to_ids.entrySet()) {
			int count = brand_entry.getValue().size();
			if (count > max_count) {
				max_brand = brand_entry.getKey();
				max_count = count;
			}
		}
		this.label = max_brand;
	}
	
	/** get/set methods */
	public void set_label(String label) {
		this.label = label;
	}
	public String get_label() {
		return this.label;
	}
	public void set_flag(Boolean _flag) {
		this.flag = _flag;
	}
	public Boolean get_flag() {
		return this.flag;
	}
	public Integer get_total_count() {
		Integer result = 0;
		for (Map.Entry<String, HashSet<String>> brand_entry : brand_to_ids.entrySet()) {
			result += brand_entry.getValue().size();
		}
		return result;
	}
	public HashMap<String, HashSet<String>> get_brand_to_ids() {
		return this.brand_to_ids;
	}
	public Set<String> get_brands() {
		return this.brand_to_ids.keySet();
	}

}