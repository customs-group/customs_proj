package str_rec;

import java.util.*;

/**
 * 元组类
 * @author Jason Han
 *
 */
public class Tuple {
	public double threshold;
	public ArrayList<Cluster> clusterSet;
	
	public Tuple(double threshold) {
		this.threshold=threshold;
		this.clusterSet=new ArrayList<Cluster>();
	}

	/**
	 * 设置cluster集，这里是深拷贝而不是浅拷贝
	 * @param clusterSet cluster集
	 */
	public void SetClusterSet(ArrayList<Cluster> clusterSet) {
		for(Cluster c : clusterSet) {
			ArrayList<String> elements=new ArrayList<String>();
			elements.addAll(c.elements);
			Cluster newCluster=new Cluster(elements);
			this.clusterSet.add(newCluster);
		}
	}

	/**
	 * 将两个cluster进行合并
	 * @param cluster1
	 * @param cluster2
	 */
	public void Union(Cluster cluster1,Cluster cluster2) {
		String s1=cluster1.elements.get(0);//cluster1中的某个元素
		String s2=cluster2.elements.get(0);//cluster2中的某个元素

		//寻找s1和s2所在的cluster
		//这里这样做的原因是为了处理“A和B合并后又发现B和C需要合并”的情况
		int s1_index=0,s2_index=0;
		for(Cluster cluster : clusterSet) {
			if(cluster.elements.contains(s1)) {
				s1_index=this.clusterSet.indexOf(cluster);
			}
			if(cluster.elements.contains(s2)) {
				s2_index=this.clusterSet.indexOf(cluster);
			}
		}

		if(s1_index==s2_index) {
			return;
		}

		this.clusterSet.get(s1_index).Union(this.clusterSet.get(s2_index));//合并
		this.clusterSet.remove(s2_index);//移除被合并的Cluster
    }
	
	/**
	 * 重新toString，便于打印
	 * @return 结果
	 */
	public String toString() {
		StringBuilder result=new StringBuilder();
		result.append("< ");
		result.append(this.threshold+", ");
		result.append(this.clusterSet.size()+", ");
		for(Cluster c : this.clusterSet) {
			result.append(c.toString());
		}
		result.append(">");
		return result.toString();
	}
}
