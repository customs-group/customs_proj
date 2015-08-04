package str_rec;

import java.util.ArrayList;

/**
 * 层次聚类Class
 * @author Jason Han
 *
 */
public class HierarchicalCluster {
	//3种算法的枚举
	public static enum Type{SINGLE_LINK,COMPLETE_LINK,AVERAGE_LINK}

	private ArrayList<String>  elementNames;//元素名
	private double[][] origionDistanceMatrix;//距离矩阵
	ArrayList<Tuple> tupleList;//保存每个阈值对应的结果集

	/**
	 * 算法初始化
	 * @param distanceMatrix 距离矩阵
	 * @param elementNames 元素集
	 */
	public HierarchicalCluster(double[][] distanceMatrix,ArrayList<String> elementNames) {
		if(distanceMatrix.length<=0||distanceMatrix.length!=distanceMatrix[0].length||elementNames.size()!=distanceMatrix.length) {
			System.out.println("数据有误");
			return;
		}

		this.origionDistanceMatrix=distanceMatrix;
		this.elementNames=elementNames;

		this.tupleList=new ArrayList<Tuple>();

		//初始化距离阈值为0的情况
		ArrayList<Cluster> clusterSet=new ArrayList<Cluster>();//初始簇
		for(int i=0;i<elementNames.size();i++) {
			ArrayList<String> al=new ArrayList<String>();
			al.add(elementNames.get(i));
			clusterSet.add(new Cluster(al));
		}
		Tuple firstTuple=new Tuple(0);
		firstTuple.clusterSet=clusterSet;
		this.tupleList.add(firstTuple);
	}

	/**
	 * 进行聚类
	 * @param type 聚类的方式
	 */
	public void DoClustering(Type type) {
		//初始的cluster数量和距离矩阵
		int clusterCount=this.elementNames.size();
		double[][] distanceMatrix=this.origionDistanceMatrix;

		while(clusterCount>1) {
			//获取threshold
			double threshold=this.FindThreshold(distanceMatrix,clusterCount);

			Tuple tuple=new Tuple(threshold);//新的tuple
			ArrayList<Cluster> oldClusterSet=this.tupleList.get(this.tupleList.size()-1).clusterSet;//旧的Cluster集
			tuple.SetClusterSet(oldClusterSet);

			//遍历距离矩阵的右上角
			for(int i=0;i<clusterCount;i++) {
				for(int p=i+1;p<clusterCount;p++) {
					//如果两个cluster的距离小于threshold，则合并
					if(distanceMatrix[i][p]<=threshold) {
						tuple.Union(oldClusterSet.get(i),oldClusterSet.get(p));
					}
				}
			}

			this.tupleList.add(tuple);//添加tuple

			clusterCount=tuple.clusterSet.size();//更新簇数量
			distanceMatrix=new double[clusterCount][clusterCount];
			this.UpdateMatrix(distanceMatrix,tuple, type);//更新距离矩阵
		}
	}

	/**
	 * 重新计算距离矩阵
	 * @param distanceMatrix 需要更新的距离矩阵
	 * @param tuple 目前的tuple
	 * @param type 算法方式（Single Link等等）
	 */
	private void UpdateMatrix(double[][] distanceMatrix,Tuple tuple,Type type) {
		int clusterCount=tuple.clusterSet.size();//获取cluster的数量
		//遍历距离矩阵右上角
		for(int i=0;i<clusterCount;i++) {
			for(int j=i+1;j<clusterCount;j++) {
				distanceMatrix[i][j]=this.GetDistance(tuple.clusterSet.get(i), tuple.clusterSet.get(j), type);
			}
		}
	}
	
	/**
	 * 寻找最小距离作为距离阈值
	 * @param matrix 距离矩阵
	 * @return 最小阈值
	 */
	private double FindThreshold(double[][] matrix,int clusterCount) {
		double minDistance=Double.MAX_VALUE;
		for(int i=0;i<clusterCount;i++) {
			for(int j=i+1;j<clusterCount;j++) {
				if(matrix[i][j]<minDistance) {
					minDistance=matrix[i][j];
				}
			}
		}
		return minDistance;
	}

	/**
	 * 计算两个cluster之间的距离
	 * @param cluster1
	 * @param cluster2
	 * @param type 计算方式
	 * @return 距离
	 */
	private double GetDistance(Cluster cluster1,Cluster cluster2,Type type) {
		switch(type) {
			case SINGLE_LINK:
				return this.SLDistance(cluster1, cluster2);
			case COMPLETE_LINK:
				return this.CLDistance(cluster1, cluster2);
			case AVERAGE_LINK:
				return this.ALDistance(cluster1, cluster2);
		}
		return -1;
	}

	/**
	 * 以Single Link方式计算Cluster之间的距离
	 * @param cluster1
	 * @param cluster2
	 * @return 距离
	 */
	private double SLDistance(Cluster cluster1,Cluster cluster2) {
		double distance=Double.MAX_VALUE;
		for(String element1 : cluster1.elements) {
			int e1_index=this.elementNames.indexOf(element1);
			for(String element2 : cluster2.elements) {
				int e2_index=this.elementNames.indexOf(element2);
				double tempDistance=e1_index<e2_index?this.origionDistanceMatrix[e1_index][e2_index]
						:this.origionDistanceMatrix[e2_index][e1_index];
				if(tempDistance<distance) {
					distance=tempDistance;
				}
			}
		}
		return distance;
	}

	/**
	 * 以Complete Link方式计算cluster之间的距离
	 * @param cluster1
	 * @param cluster2
	 * @return 距离
	 */
	private double CLDistance(Cluster cluster1,Cluster cluster2) {
		double distance=-1;
		for(String element1 : cluster1.elements) {
			int e1_index=this.elementNames.indexOf(element1);
			for(String element2 : cluster2.elements) {
				int e2_index=this.elementNames.indexOf(element2);
				double tempDistance=e1_index<e2_index?this.origionDistanceMatrix[e1_index][e2_index]
						:this.origionDistanceMatrix[e2_index][e1_index];
				if(tempDistance>distance) {
					distance=tempDistance;
				}
			}
		}
		return distance;
	}

	/**
	 * 以Average Link方式计算cluster之间的距离
	 * @param cluster1
	 * @param cluster2
	 * @return 距离
	 */
	private double ALDistance(Cluster cluster1,Cluster cluster2) {
		double sum=0;

		for(String element1 : cluster1.elements) {
			int e1_index=this.elementNames.indexOf(element1);
			for(String element2 : cluster2.elements) {
				int e2_index=this.elementNames.indexOf(element2);
				double tempDistance=e1_index<e2_index?this.origionDistanceMatrix[e1_index][e2_index]
						:this.origionDistanceMatrix[e2_index][e1_index];
				sum+=tempDistance;
			}
		}

		double count=cluster1.elements.size()*cluster2.elements.size();
		return sum/count;
	}

	/**
	 * 打印结果
	 */
	public void PrintResult() {
		for(Tuple tuple : this.tupleList) {
			System.out.println(tuple.toString());
		}
	}
}