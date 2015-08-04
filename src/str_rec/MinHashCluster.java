package str_rec;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.mino.util.FileUtil;

import com.sun.net.httpserver.Filter;
import com.sun.net.httpserver.HttpExchange;


/**
 * 通过minhash方法进行聚类
 * @author 丁杰
 */
public class MinHashCluster {
	static HashMap<String, Double> map = null;
	/**
	 * 任意两篇专利文献之间距离
	 */
	private static String simHashPath = "E:\\research\\experiment_2\\tfidf_filter\\H01M\\simhash.dat";
	static {
		map = loadSimHash(simHashPath);
	}
	
	public static void main(String[] args) {
		/**
		 * 聚类文件绝对目录
		 */
		String clusterFilePath = "E:\\research\\experiment_2\\tfidf_filter\\H01M\\";
		/**
		 * 孤立点目录
		 */
		String isolatePoin = "E:\\research\\experiment_2\\tfidf_filter\\H01M\\IsolatedPoint\\";
		double initThread = 0.80;
		//文件内容为空的剪切到孤立点目录下
//		cutFileNotExist(clusterFilePath, isolatePoin); 
		//文本到其它文本的最小值大于阈值的拷贝到孤立点目录
		preIsolatePoin(simHashPath, clusterFilePath, isolatePoin, initThread);
		
		
		
		double disThreshold = 0.6;//阈值
		MinHashCluster mh = new MinHashCluster();
		String [] fileFilters = {"txt"};
		int clusterNum = 20;
		
		//如果一篇专利文献到其它专利文献的最近的距离大于某一个阈值则将该专利文献视为孤立点
		
		List<DataPoint> iniPoint = mh.getDatePoinFromDir(clusterFilePath, fileFilters);
//		System.out.println(iniPoint.size());
		mh.initialCluster(iniPoint);
		List<Cluster> clusters = mh.startAnalysis(iniPoint, clusterNum, disThreshold);
		String clusterPath = clusterFilePath + File.separator + "fileClustet.dat";
		writeClusterToFile(clusterPath, clusters);
	}

	/**
	 * 剪切
	 * @param simHashPath
	 * @param cutToPath
	 * @param threshold
	 */
	@SuppressWarnings("unused")
	private static void preIsolatePoin(String simHashPath, String cutFromPath, String cutToPath, double threshold) {
		HashMap<String, Double> simMap = FileUtil.getDoubMapFromFile(simHashPath);  //加载simhash文件
		HashMap<String, Double> minSimMap = new HashMap<String, Double>();
		Iterator<String> it = simMap.keySet().iterator();
		while(it.hasNext()) {
			String key = it.next().trim();
			String[] keys = key.split("#");
			double value = simMap.get(key);
			double distance = 1.0 - value;
//			System.out.println(distance);
			////////////////////////////////////////////////
			if(minSimMap.containsKey(keys[0])) {//如果包含
				if(minSimMap.get(keys[0]) > distance) {//如果原有的距离大于distance
					minSimMap.put(keys[0], distance);
				}
			} else {//不包含
//				System.out.println(keys[0] +  " " + distance);
				minSimMap.put(keys[0], distance);
			}
			////////////////////////////////////////////////
			if(minSimMap.containsKey(keys[1])) {//如果包含
				if(minSimMap.get(keys[1]) > distance) {
					minSimMap.put(keys[1], distance);
				}
			} else {//不包含
				minSimMap.put(keys[1], distance);
			}
		   ////////////////////////////////////////////////
		}
		it = minSimMap.keySet().iterator();
		int total =  0;
		while(it.hasNext()) {
			String fileName = it.next();
			double value = minSimMap.get(fileName);
			if(value >= threshold) {
				total ++;
//				System.out.println(value);
				FileUtil.copyFile(cutFromPath + fileName, cutToPath + fileName);
				new File(cutFromPath + fileName).delete();
//				System.out.println(fileName);//如果文本到专利文献的 最小距离大于阈值  则删除该文件
			}
		}
		System.out.println("孤立点 ： " + total);
	}
	
	/**
	 * 剪切文件内容为空的文件到 isolatePoin
	 * @param clusterFilePath
	 * @param isolatePoin
	 */
	@SuppressWarnings("unused")
	private static void cutFileNotExist(String clusterFilePath,
			String isolatePoin) {
		// TODO Auto-generated method stub
		File file = new File(clusterFilePath);
		File [] files = file.listFiles(new FileFilter() {
			@Override
			public boolean accept(File pathname) {
				// TODO Auto-generated method stub
				if(pathname.getName().endsWith("txt")) {
					return true;
				}
				return false;
			}});
		
		for(File f : files) {
			if(f.length() == 0) {
				System.out.println(f);
				FileUtil.copyFile(f.getAbsolutePath(), isolatePoin + File.separator + f.getName());
				f.delete();
			}
		}//end_file
	}

	/**
	 * 加载两篇专利文献之间的距离
	 * @param simHashPath2
	 * @return
	 */
	private static HashMap<String, Double> loadSimHash(String simHashPath) {
		// TODO Auto-generated method stub
		return FileUtil.getDoubMapFromFile(simHashPath);
	}

	/**
	 * 将聚类结果写入到文件
	 * @param clusterFilePath
	 * @param clusters
	 */
	private static void writeClusterToFile(String clusterFilePath,
			List<Cluster> clusters) {
		FileWriter fw = null;
		BufferedWriter bw = null;
		try {
			 fw = new FileWriter(clusterFilePath);
			 bw = new BufferedWriter(fw);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for (Cluster cl : clusters) {
			try {
				bw.write(cl.getClusterName());
				bw.newLine();
				bw.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
			List<DataPoint> tempDps = cl.getDataPoints();
			
			for (DataPoint tempdp : tempDps) {
				try {
					bw.write(tempdp.getDataPointName().substring(tempdp.getDataPointName().lastIndexOf("\\") + 1));
					bw.write("#");
					bw.newLine();
					bw.flush();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
//				System.out.print(tempdp.getDataPointName()+"#");
			}//end_for
			try {
				bw.newLine();
				bw.flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
//			System.out.println();
		}//end_for
	}
	
	/**
	 * @param dataPoints 数据点
	 * @param ClusterNum 需要聚类的个数
	 * @return
	 */
	public List<Cluster> startAnalysis(List<DataPoint> dataPoints,
			int ClusterNum, double threshold) {
		//最聚类结果
		List<Cluster> finalClusters = new ArrayList<Cluster>();
		//初始聚类结果
		List<Cluster> originalClusters = initialCluster(dataPoints);
		finalClusters = originalClusters;
		while (finalClusters.size() > ClusterNum) {
			double min = Double.MAX_VALUE;
			int mergeIndexA = 0;
			int mergeIndexB = 0;
			for (int i = 0; i < finalClusters.size(); i++) {
				for (int j = 0; j < finalClusters.size(); j++) {
					if (i != j) {
						Cluster clusterA = finalClusters.get(i);//获取聚类1
						Cluster clusterB = finalClusters.get(j);//获取聚类2
						List<DataPoint> dataPointsA = clusterA.getDataPoints();
						List<DataPoint> dataPointsB = clusterB.getDataPoints();
						
						//以下两个for循环来确定应聚那两个类
						for (int m = 0; m < dataPointsA.size(); m++) {
							for (int n = 0; n < dataPointsB.size(); n++) {//划分类的标准:两类中最近的两篇文档作为类的距离
								 //计算两个聚类专利文献的距离
//								System.out.println("outer:" + (m + 1) + " / " + dataPointsA.size()+ ",inner:"+ (n + 1) + " / " +dataPointsB.size());
								double tempWeight = getDistance(dataPointsA.get(m).getDataPointName(), dataPointsB.get(n).getDataPointName());
								if(0 <= tempWeight && tempWeight <= 1) {//如果在0-1之间,将相似度转换为距离
									tempWeight = 1 - tempWeight;
								}
								if (tempWeight < min) {
									min = tempWeight;
									mergeIndexA = i;//保存最小距离的专利文献所在的簇
									mergeIndexB = j;//保存最小距离的专利文献所在的簇
								}//end_if
							}//end_for
						}//end_for
					}//end_if
				} // end for j
			}// end for i
			// 合并cluster[mergeIndexA]和cluster[mergeIndexB]
			if(min > threshold) {
				System.out.println("距离超过指定的阈值，聚类结束！");
				break;
			}
			finalClusters = mergeCluster(finalClusters, mergeIndexA, mergeIndexB);
		}// end while
		return finalClusters;
	}

	/**
	 * 获取两篇专利文献之间的相似度
	 * @param dataPointName
	 * @param dataPointName2
	 * @return
	 */
	private double getDistance(String dataPointName1, String dataPointName2) {
		// TODO Auto-generated method stub
		String path1 = dataPointName1.substring(dataPointName1.lastIndexOf("\\")+1);
		String path2 = dataPointName2.substring(dataPointName2.lastIndexOf("\\")+1);
//		System.out.println(path1);
		String key = path1 + "#" + path2;
		if(map.containsKey(path1 + "#" + path2)) {
			double value = map.get(key);
//			System.out.println(key + "$" + value);
			return value;
		}
		return 0;
	}

	/**
	 * 合并两个类
	 * @param clusters
	 * @param mergeIndexA
	 * @param mergeIndexB
	 * @return
	 */
	private List<Cluster> mergeCluster(List<Cluster> clusters, int mergeIndexA,
			int mergeIndexB) {
		if (mergeIndexA != mergeIndexB) {
			// 将cluster[mergeIndexB]中的DataPoint加入到 cluster[mergeIndexA]
			Cluster clusterA = clusters.get(mergeIndexA);
			Cluster clusterB = clusters.get(mergeIndexB);
			System.out.println("合并 " + clusterA.getClusterName()+"["+clusterA.getDataPoints().size()+"] + " + clusterB.getClusterName() + "["+clusterB.getDataPoints().size()+"]");
			List<DataPoint> dpA = clusterA.getDataPoints();
			List<DataPoint> dpB = clusterB.getDataPoints();
			for (DataPoint dp : dpB) {
				DataPoint tempDp = new DataPoint();
				tempDp.setDataPointName(dp.getDataPointName());
				tempDp.setCluster(clusterA);
				dpA.add(tempDp);
			}
			clusterA.setDataPoints(dpA);
			// List<Cluster> clusters中移除cluster[mergeIndexB]
			clusters.remove(mergeIndexB);
		}
		return clusters;
	}

	/**
	 * 返回目录下符合后缀集合的样本集合
	 * @param dir
	 * @param fileSufix
	 * @return
	 */
	@SuppressWarnings("unused")
	private List<DataPoint> getDatePoinFromDir(String dir, final String [] fileSufix) {
		List<DataPoint> list = new ArrayList<DataPoint>();
		if(dir == null || "".equals(dir)) {
			System.out.println("输入路径为空!");
			return list;
		}
		File file = new File(dir);
		String[] files = file.list(new FilenameFilter(){
			@Override
			public boolean accept(File dir, String name) {
				// TODO Auto-generated method stub
				for(String str : fileSufix) {
					if(name.endsWith(str)) {
						return true;
					}
				}
				return false;
			}});
		for(String f : files) {
			DataPoint dp = new DataPoint();
			dp.setDataPointName(dir + f);
			list.add(dp);
		}
		return list;
	}
	// 初始化类簇
	/**
	 * 每个节点属于一个类
	 * @param dataPoints
	 * @return
	 */
	private List<Cluster> initialCluster(List<DataPoint> dataPoints) {
		List<Cluster> originalClusters = new ArrayList<Cluster>();
		for (int i = 0; i < dataPoints.size(); i++) {
			DataPoint tempDataPoint = dataPoints.get(i);
			List<DataPoint> tempDataPoints = new ArrayList<DataPoint>();
			tempDataPoints.add(tempDataPoint);
			Cluster tempCluster = new Cluster();
			tempCluster.setClusterName("Cluster " + String.valueOf(i));
			tempCluster.setDataPoints(tempDataPoints);
			tempDataPoint.setCluster(tempCluster);
			originalClusters.add(tempCluster);
		}
		return originalClusters;
	}
	
}
/**
 * 聚类存放结果
 * @author DingJie
 */
class Cluster {
	private List<DataPoint> dataPoints = new ArrayList<DataPoint>(); // 类簇中的样本点
	private String clusterName;

	public List<DataPoint> getDataPoints() {
		return dataPoints;
	}

	public void setDataPoints(List<DataPoint> dataPoints) {
		this.dataPoints = dataPoints;
	}

	public String getClusterName() {
		return clusterName;
	}

	public void setClusterName(String clusterName) {
		this.clusterName = clusterName;
	}
}

/**
 * 聚类节点类型
 * @author 丁杰
 */
class DataPoint {
	String dataPointName; // 样本点名
	Cluster cluster;      // 样本点所属类簇

	public DataPoint() {
	}

	public Cluster getCluster() {
		return cluster;
	}

	public void setCluster(Cluster cluster) {
		this.cluster = cluster;
	}

	public String getDataPointName() {
		return dataPointName;
	}

	public void setDataPointName(String dataPointName) {
		this.dataPointName = dataPointName;
	}
}