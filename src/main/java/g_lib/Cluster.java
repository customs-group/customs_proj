package g_lib;

import java.util.*;

/**
 * Cluster类，表示一个聚类
 */
public class Cluster {
	public enum record_type {with_id, without_id}
	private Boolean flag;
	private String label;
	private HashMap<String, HashSet<String>> k_ids;

	/**
	 * 初始化cluster
	 * @param key 初始化时一个类只有一个key
	 * @param id_set 该key下的id_set
	 */
	public Cluster(String key, HashSet<String> id_set) {
		this.k_ids = new HashMap<>();
		this.k_ids.put(key, id_set);
		this.auto_set_label();
		this.flag = false;
	}

	/**
	 * 通过map初始化cluster
	 * @param _k_ids 创建好的[key:id_set]
	 */
	public Cluster(HashMap<String, HashSet<String>> _k_ids) {
		this.k_ids = _k_ids;
	}

	/**
	 * 向cluster下添加key
	 * @param key 需添加的品牌
	 * @param id_set 该品牌下的id_set
	 */
	public void add_key(String key, HashSet<String> id_set) {
		if (this.k_ids.containsKey(key)) {
			this.k_ids.get(key).addAll(id_set);
		} else {
			this.k_ids.put(key, id_set);
		}
	}

	/**
	 * 从cluster中删除key
	 * @param key 被删除的key
     */
	public void remove_key(String key) {
		if (this.k_ids.containsKey(key)) {
			this.k_ids.remove(key);
		} else {
			System.out.println("no key: " + key);
		}
	}

	/**
	 * 和另一个Cluster合并
	 * @param another_cluster 被合并的cluster
	 */
	public void union(Cluster another_cluster) {
		another_cluster.k_ids.forEach(this::add_key);
	}

	/**
	 * 重写toString()，便于打印
	 */
	public String toString(record_type type) {
		StringBuilder result = new StringBuilder();
		result.append("{label: ");
		result.append(this.label);
		result.append("; ");

		Iterator<Map.Entry<String, HashSet<String>>> iterator = this.k_ids.entrySet().iterator();
		while (iterator.hasNext()) {
			Map.Entry<String, HashSet<String>> entry = iterator.next();
			result.append("<");
			result.append(entry.getKey());
			if (type == record_type.with_id) {
				result.append(": ");
				result.append(entry.getValue().toString());
			}
			result.append(">");
			if (iterator.hasNext()) {
				result.append(", ");
			}
		}
		result.append("}");
		return result.toString();
	}

	/**
	 * 根据出现次数最多的key自动设置label
	 */
	public void auto_set_label() {
		String max_key = "";
		int max_count = 0;
		for (Map.Entry<String, HashSet<String>> entry : this.k_ids.entrySet()) {
			int count = entry.getValue().size();
			if (count > max_count) {
				max_key = entry.getKey();
				max_count = count;
			}
		}
		this.label = max_key;
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
	public int get_total_count() {
		int result = 0;
		for (Map.Entry<String, HashSet<String>> entry : this.k_ids.entrySet()) {
			result += entry.getValue().size();
		}
		return result;
	}
	public HashMap<String, HashSet<String>> get_k_ids() {
		return this.k_ids;
	}
	public Set<String> get_keys() {
		return this.k_ids.keySet();
	}
}