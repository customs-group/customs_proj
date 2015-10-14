package ml;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import libsvm.svm_node;

public class Data {
	public enum data_type {original, scaled};
	
	private Vector<Double> labels;
	private Vector<svm_node[]> original_set;
	private Vector<svm_node[]> scaled_set;
	private int sample_num;
	private int feature_num;
	private double scale_upper_bound;
	private double scale_lower_bound;
	
	/**
	 * initialization
	 */
	public Data() {
		this.labels = new Vector<>();
		this.original_set = new Vector<>();
		this.scaled_set = new Vector<>();
		this.sample_num = 0;
		this.feature_num = 0;
		this.scale_upper_bound = 1;
		this.scale_lower_bound = -1;
	}
	
	/**
	 * transfer String to Double, in case some features are "null", "I", "E" or ""
	 * @param string feature in string
	 * @return feature in double
	 */
	private static Double stod(String string) {
		Double result = 2.0; // should handle this error: "cannnot convert string to Double"
		if (string == null || string.equals("") || string.equals("null") || string.equals("I")) {
			result = 0.0;
		} else if (string.equals("E")) {
			result = 1.0;
		} else {
			try {
				result = Double.parseDouble(string);
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
	/**
	 * make the labels 1 or -1
	 * @param label original label
	 * @return standarlized label
	 */
	private static Double mklabel(Double label) {
		Double _label;
		if (label == 0) {
			_label = -1.0;
		} else {
			_label = 1.0;
		}
		return _label;
	}
	
	/**
	 * read data from database
	 * @param connection
	 * @param query
	 * @throws SQLException
	 */
	public void read_data(Connection connection, String query) throws SQLException {
		PreparedStatement pstmt = connection.prepareStatement(query);
		ResultSet rs = pstmt.executeQuery();
		
		/* set feature num */
		this.feature_num = rs.getMetaData().getColumnCount() - 1;
		/* read data */
		while(rs.next()) {
			svm_node[] sample = new svm_node[this.feature_num];
			for (int i = 0; i < this.feature_num; i++) {
				sample[i] = new svm_node();
				/* set index */
				sample[i].index = i + 1;
				/* set value */
				if (rs.getObject(i + 2).getClass().getName().equals("java.lang.String")) {
					sample[i].value = stod(rs.getString(i + 2));
				} else if ((rs.getObject(i + 2).getClass().getName().equals("java.lang.Long"))
						|| (rs.getObject(i + 2).getClass().getName().equals("java.math.BigDecimal"))) {
					sample[i].value = rs.getDouble(i + 2);
				} else {
					// to be continued
				}
				
			}
			original_set.add(sample);
		    labels.add(mklabel(stod(rs.getString(1))));	// special_flag
		}
		
		/* set sample num */
        this.sample_num = original_set.size();
	    
	    /* end data preparation */
        System.out.println("Data preparation done! " + this.get_sample_num() + " samples in total");
		rs.close();
		pstmt.close();
	}

	/**
	 * record data to file
	 * @param filename file name to store data
	 * @param type type of data to be recorded, original or scaled
	 * @throws IOException
	 */
	public void record_data(String filename, data_type type) throws IOException {		
		String _filename;
		Vector<svm_node[]> _set;
		/* set file name for record */
		switch (type) {
			case original:
				_filename = "./datasets/data." + filename + "_original";
				_set = original_set;
				break;
			case scaled:
				_filename = "./datasets/data." + filename + "_scaled";
				_set = scaled_set;
				break;
			default:
				System.out.println("wrong data type, record failed");
				return;
		}
		File file = new File(_filename);
		FileOutputStream fos = new FileOutputStream(file);
		OutputStreamWriter writer = new OutputStreamWriter(fos, "UTF-8");
		
	    svm_node[] sample;
	    for (int i = 0; i < this.sample_num; i++) {
			writer.append(Double.toString(labels.get(i)));
			writer.append(" ");
	    	sample = _set.get(i);
	    	for (int j = 0; j < this.feature_num; j++) {
				writer.append(Integer.toString(sample[j].index));
				writer.append(":");
				writer.append(Double.toString(sample[j].value));
				writer.append(" ");
	    	}
	    	writer.append("\n");
	    }
	    System.out.println("Data record done! see " + _filename);
		writer.close();
		fos.close();
	}
	
	
	/**
	 * scale training data
	 * @return scaling parameter in double[][]
	 * @double[0][0]: scale_upper_bound
	 * @double[0][1]: scale_lower_bound
	 * @double[i][0]: feature_max[i - 1], 1 <= i <= this.feature_num
	 * @double[i][1]: feature_min[i - 1], 1 <= i <= this.feature_num
	 */
	public double[][] scale_data () {
		/* scale labels, not using for now
		double y_max = -Double.MAX_VALUE;
		double y_min = Double.MAX_VALUE;
		*/
		svm_node[] original_sample;
		svm_node[] scaled_sample;
		
		/* step 0: initiate scale param */
		double[][] scale_param = new double[this.feature_num + 1][2];
		scale_param[0][0] = this.scale_upper_bound;
		scale_param[0][1] = this.scale_lower_bound;
		
		/* step 1: initiate feature bound */
		double[] feature_max = new double[this.feature_num];
		double[] feature_min = new double[this.feature_num];
		for(int i = 0; i < this.feature_num; i++) {
			feature_max[i] = -Double.MAX_VALUE;
			feature_min[i] = Double.MAX_VALUE;
		}
		
		/* step 2: find out min/max value */
		for (int i = 0; i < this.sample_num; i++) {
			/* scale labels, not using for now
			y_max = Math.max(y_max, labels[i]);
			y_min = Math.min(y_min, labels[i]);
			*/
			original_sample = original_set.get(i);
			for (int j = 0; j < this.feature_num; j++) {
				feature_max[j] = Math.max(feature_max[j], original_sample[j].value);
				feature_min[j] = Math.min(feature_min[j], original_sample[j].value);
				scale_param[j + 1][0] = feature_max[j];
				scale_param[j + 1][1] = feature_min[j];
			}
		}

		/* pass 3: scale */
		for (int i = 0; i < sample_num; i++) {
			original_sample = original_set.get(i);
			scaled_sample = new svm_node[this.feature_num];
			for (int j = 0; j < feature_num; j++) {
				scaled_sample[j] = new svm_node();
				scaled_sample[j].index = original_sample[j].index;
				if (original_sample[j].value == feature_min[j]) {
					scaled_sample[j].value = this.scale_lower_bound;
				} else if (original_sample[j].value == feature_max[j]) {
					scaled_sample[j].value = this.scale_upper_bound;
				} else {
					scaled_sample[j].value = this.scale_lower_bound
							+ ((original_sample[j].value - feature_min[j])
							 / (feature_max[j] - feature_min[j])
							 * (this.scale_upper_bound - this.scale_lower_bound));
				}
			}
			scaled_set.add(scaled_sample);
		}
		return scale_param;
	}
	
	/**
	 * scale testing data
	 * @param scale_param
	 * see: scale_data@param
	 */
	public void scale_data_from (double[][] scale_param) {
		/* scale labels, not using for now
		double y_max = -Double.MAX_VALUE;
		double y_min = Double.MAX_VALUE;
		*/
		svm_node[] sample;
		svm_node[] scaled_sample;
		
		/* step 1: initiate feature bound */
		this.scale_upper_bound = scale_param[0][0];
		this.scale_lower_bound = scale_param[0][1];
		
		double[] feature_max = new double[this.feature_num];
		double[] feature_min = new double[this.feature_num];
		for(int i = 0; i < this.feature_num; i++) {
			feature_max[i] = scale_param[i + 1][0];
			feature_min[i] = scale_param[i + 1][1];
		}
		
		/* pass 3: scale */
		for (int i = 0; i < sample_num; i++) {
			sample = original_set.get(i);
			scaled_sample = new svm_node[this.feature_num];
			for (int j = 0; j < feature_num; j++) {
				scaled_sample[j] = new svm_node();
				scaled_sample[j].index = sample[j].index;
				if (sample[j].value == feature_min[j]) {
					scaled_sample[j].value = this.scale_lower_bound;
				} else if (sample[j].value == feature_max[j]) {
					scaled_sample[j].value = this.scale_upper_bound;
				} else {
					scaled_sample[j].value = this.scale_lower_bound
							+ ((sample[j].value - feature_min[j])
							 / (feature_max[j] - feature_min[j])
							 * (this.scale_upper_bound - this.scale_lower_bound));
				}
			}
			scaled_set.add(scaled_sample);
		}
	}
	
	/** get/set mothods */
	public Vector<svm_node[]> get_set(data_type type) {
		switch (type) {
			case original:
				return this.original_set;
			case scaled:
				return this.scaled_set;
			default:
				System.out.println("wrong data type, original set returned");
				return this.original_set;
		}
	}
	public Vector<Double> get_labels() {
		return this.labels;
	}
	public int get_sample_num() {
		return this.sample_num;
	}
	public int get_feature_num() {
		return this.feature_num;
	}
	public void set_scale_upper_bound(double scale_upper_bound) {
		this.scale_upper_bound = scale_upper_bound;
	}
	public void set_scale_lower_bound(double scale_lower_bound) {
		this.scale_lower_bound = scale_lower_bound;
	}
}