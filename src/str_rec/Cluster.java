package str_rec;

import java.util.ArrayList;

/**
 * Cluster类，表示一个簇
 * @author Jason Han
 *
 */
public class Cluster {
	public ArrayList<String> elements;//Cluster中的元素
	
	public Cluster(ArrayList<String> elements) {
		this.elements=elements;
	}
	
	/**
	 * 和另一个Cluster合并
	 * @param anotherCluster 另一个
	 */
	public void Union(Cluster anotherCluster) {
		this.elements.addAll(anotherCluster.elements);
	}

	/**
	 * 重写toString()，便于打印
	 */
	public String toString() {
		StringBuilder result=new StringBuilder();
		result.append("{");
		for(int i=0;i<this.elements.size();i++) {
			result.append(this.elements.get(i));
			if(i!=this.elements.size()-1) {
				result.append(",");
			}
		}
		result.append("}");
		return result.toString();
	}
}