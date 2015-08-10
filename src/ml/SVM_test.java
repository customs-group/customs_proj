package ml;

import java.io.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

import util.DB_manager;
import libsvm.*;

public class SVM_test {
	/**
	 * train svm model
	 * @param data: data used to train the model
	 * @return model
	 */
	public static svm_model svm_train(Data data) throws IOException {
		String model_file_name = "./datasets/model";
		svm_model model;
		
		/* set svm problem */
		svm_problem problem = new svm_problem();
        problem.l = data.get_sample_num();
        problem.x = new svm_node[problem.l][];
		for(int i = 0; i < problem.l; i++) {
			problem.x[i] = data.get_set("scaled").get(i);
		}
		problem.y = new double[problem.l];
		for(int i = 0; i < problem.l; i++) {
			problem.y[i] = data.get_labels().get(i);
		}
		
        /* set svm parameter */
        svm_parameter param = new svm_parameter();
        param.svm_type = svm_parameter.C_SVC;
        param.kernel_type = svm_parameter.RBF;
        param.C = 0.015625; // for C_SVC, EPSILON_SVR and NU_SVR, default 1.0 / data.get_feature_num()
        param.gamma = 0.0625;
        param.eps = 0.01;
        param.cache_size = 100;
        
        /* train svm model */
        String error_msg = svm.svm_check_parameter(problem, param);
        if (error_msg == null) {
        	model = svm.svm_train(problem, param);
        	svm.svm_save_model(model_file_name, model);
        } else {
        	System.out.println(error_msg);
        	model = null;
        }
        return model;
	}
	
	/**
	 * predicte the labels of test data
	 * @param model: model trained by svm_train
	 * @param data: data used to test the model
	 */
	public static void svm_test(svm_model model, Data data) {
		int hit = 0;
		double hit_rate = 0.0;
		Vector<svm_node[]> set = data.get_set("scaled");
		Vector<Double> labels = data.get_labels();
		svm_node[] sample;
		double real_label, predict_label;
		
		try {
			/* preparation for the log file */
			Date now = new Date();
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_hh:mm:ss");
			String suffix = dateFormat.format(now);
			File file = new File("./datasets/result_" + suffix + ".log");
			FileOutputStream fos = new FileOutputStream(file);
			OutputStreamWriter writer = new OutputStreamWriter(fos, "UTF-8");
			
			for (int i = 0; i < data.get_sample_num(); i++) {
				sample = set.get(i);
				real_label = labels.get(i);
				predict_label = svm.svm_predict(model, sample);
				writer.append("predict label: " + predict_label + "; real label: " + real_label + " ");
				for (int j = 0; j < data.get_feature_num(); j ++) {
					writer.append(sample[j].index + ":" + sample[j].value + " ");
				}
				writer.append("\n");
				if (Math.abs(predict_label - real_label) < 0.001) {
					hit++;
				}
			}
			hit_rate = 100.0 * hit / data.get_sample_num();
			System.out.println("SVM accuracy: " + String.format("%.2f", hit_rate) + "%");
			
			writer.close();
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		/* query: get sets from database
		 * colom 1: labels
		 * colom 2 - N: features
		 */
		String train_query, test_query;
		String train_limit = "limit 2500";
		String test_limit = "limit 2500";
		
		Vector<String> features = new Vector<String>();
		features.add("entry_head.special_flag");
		features.add("entry_head.i_e_flag");
		features.add("entry_head.decl_port");
		features.add("entry_head.trade_country");
		features.add("entry_head.destination_port");
		features.add("UNIX_TIMESTAMP(entry_head.d_date)");
		features.add("entry_head.trade_mode");
		features.add("entry_list.code_ts");
		features.add("entry_list.qty_1");
		features.add("entry_list.usd_price");
		
		train_query = "select";
		for (int i = 0; i < features.size() - 1; i++) {
			train_query +=  " " + features.get(i) + ",";
		}
		train_query += " " + features.get(features.size() - 1);
		
		train_query += " from entry_head inner join entry_list on entry_head.entry_id = entry_list.entry_id";
		train_query += " where entry_head.d_date < '2010-03-05' and entry_head.special_flag = 1 " + train_limit;
		train_query += " union (select";
		for (int i = 0; i < features.size() - 1; i++) {
			train_query +=  " " + features.get(i) + ",";
		}
		train_query += " " + features.get(features.size() - 1);
		
		train_query += " from entry_head inner join entry_list on entry_head.entry_id = entry_list.entry_id";
		train_query += " where entry_head.d_date < '2010-03-05' and entry_head.special_flag = 0 " + train_limit + ");";
		
		test_query = "select";
		for (int i = 0; i < features.size() - 1; i++) {
			test_query +=  " " + features.get(i) + ",";
		}
		test_query += " " + features.get(features.size() - 1);
		
		test_query += " from entry_head inner join entry_list on entry_head.entry_id = entry_list.entry_id";
		test_query += " where '2010-03-05' <= entry_head.d_date and entry_head.special_flag = 1 " + test_limit;
		test_query += " union (select";
		for (int i = 0; i < features.size() - 1; i++) {
			test_query +=  " " + features.get(i) + ",";
		}
		test_query += " " + features.get(features.size() - 1);
		test_query += " from entry_head inner join entry_list on entry_head.entry_id = entry_list.entry_id";
		test_query += " where '2010-03-05' <= entry_head.d_date and entry_head.special_flag = 0 " + test_limit + ");";
		

		Connection connection = DB_manager.get_DB_connection();
		Data train_data = new Data();
		Data test_data = new Data();
		try {
			/* read data */
			train_data.read_data(connection, train_query);
			test_data.read_data(connection, test_query);
			/* scale data */
			test_data.scale_data_from(train_data.scale_data());
			/* record train data */
			train_data.record_data("train", "original");
			train_data.record_data("train", "scaled");
			/* record test data */
			test_data.record_data("test", "original");
			test_data.record_data("test", "scaled");
		} catch (Exception e) {
			e.printStackTrace();
		}
		DB_manager.return_DB_connection(connection);
		svm_model model;
		try {
			model = svm_train(train_data);
			svm_test(model, train_data);
			svm_test(model, test_data);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}