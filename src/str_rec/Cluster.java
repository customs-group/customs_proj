package str_rec;

import java.util.ArrayList;

/**
 * Cluster类，表示一个簇
 * @author Jason Han
 *
 */
public class Cluster {
	private ArrayList<String> elements; // Cluster中的元素
	private int element_num;
	
	public Cluster(ArrayList<String> elements) {
		this.elements = elements;
		this.element_num = this.elements.size();
	}
	
	/**
	 * 和另一个Cluster合并
	 * @param anotherCluster 另一个
	 */
	public void Union(Cluster another_cluster) {
		this.elements.addAll(another_cluster.elements);
		this.element_num = this.elements.size();
		another_cluster = null;
	}

	/**
	 * 重写toString()，便于打印
	 */
	public String toString() {
		StringBuilder result = new StringBuilder();
		result.append("{");
		for(int i = 0; i < this.elements.size(); i++) {
			result.append(this.elements.get(i));
			if(i != this.elements.size() - 1) {
				result.append(", ");
			}
		}
		result.append("}");
		return result.toString();
	}
	
	public ArrayList<String> get_elements() {
		return this.elements;
	}
	public int get_ele_num() {
		return this.element_num;
	}
}