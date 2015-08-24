package ml;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Vector;

import util.DB_manager;
import libsvm.svm;
import libsvm.svm_model;
import libsvm.svm_node;
import libsvm.svm_parameter;
import libsvm.svm_print_interface;
import libsvm.svm_problem;

public class Grid_search {
	private static int C_BASE = 2;
	private static int G_BASE = 2;
	private static int C_STEP = 1;
	private static int G_STEP = 1;
	private static int C_START_VALUE = -8;
	private static int C_STOP_VALUE = 8;
	private static int G_START_VALUE = -8;
	private static int G_STOP_VALUE = 8;
	
	/**
	 * train svm model
	 * @param set train set
	 * @param labels train labels
	 * @param param svm_parameter
	 * @return model
	 */
	public static svm_model svm_train(Vector<svm_node[]> set, Vector<Double> labels, svm_parameter param) {
		svm_model model;
		
		/* set svm problem */
		svm_problem problem = new svm_problem();
        problem.l = set.size();
        problem.x = new svm_node[problem.l][];
        problem.y = new double[problem.l];
		for(int i = 0; i < problem.l; i++) {
			problem.x[i] = set.get(i);
			problem.y[i] = labels.get(i);
		}
		
        /* train svm model */
        String error_msg = svm.svm_check_parameter(problem, param);
        if (error_msg == null) {
        	model = svm.svm_train(problem, param);
        } else {
        	System.out.println("svm parameter error:");
        	System.out.println(error_msg);
        	model = null;
        }
        return model;
	}

	/**
	 * valid model accuracy
	 * @param model model to valid
	 * @param set test set
	 * @param labels test labels
	 * @return total hit num in test set
	 */
	public static int svm_valid(svm_model model, Vector<svm_node[]> set, Vector<Double> labels) {
		int hit = 0;
		svm_node[] sample;
		double real_label, predict_label;
		for (int i = 0; i < set.size(); i++) {
			sample = set.get(i);
			real_label = labels.get(i);
			predict_label = svm.svm_predict(model, sample);
			if (Math.abs(predict_label - real_label) < 0.001) {
				hit++;
			}
		}
		return hit;
	}
	
	/**
	 * do cross validation
	 * @param data training data
	 * @param power_of_c
	 * @param power_of_g
	 * @param fold_n the number n of n fold validation
	 * @param param
	 * @return best accuracy under this set of c and g
	 */
	private static double do_cross_validation(Data data, int power_of_c, int power_of_g, int fold_n, svm_parameter param) {
		Vector<svm_node[]> set = data.get_set(Data.data_type.scaled);
		Vector<Double> labels = data.get_labels();
		svm_model model;
		int total_hit = 0;
		int vs_start, vs_end, vs_len; // validate_segment
		double predict_accuracy;
		
		param.C = Math.pow(C_BASE, power_of_c);
		param.gamma = Math.pow(G_BASE, power_of_g);

		for (int i = 0; i <= fold_n; i++) {
			Vector<svm_node[]> train_set = new Vector<>();
			Vector<svm_node[]> valid_set = new Vector<>();
			Vector<Double> train_labels = new Vector<>();
			Vector<Double> valid_labels = new Vector<>();
			
			vs_len = set.size() / fold_n;
			vs_start = i * vs_len;
			vs_end = i == fold_n ? set.size() : (i + 1) * vs_len;
			
			for (int j = 0; j < vs_start; j++) {
				train_set.add(set.get(j));
				train_labels.add(labels.get(j));
			}
			for (int j = vs_start; j < vs_end; j++) {
				valid_set.add(set.get(j));
				valid_labels.add(labels.get(j));
			}
			for (int j = vs_end; j < set.size(); j++) {
				train_set.add(set.get(j));
				train_labels.add(labels.get(j));
			}
	        model = svm_train(train_set, train_labels, param);
	        total_hit += svm_valid(model, valid_set, valid_labels);
		}
		predict_accuracy = 100.0 * total_hit / set.size();
		return predict_accuracy;
	}
	
	private static svm_print_interface svm_print_null = new svm_print_interface() {
		public void print(String s) {}
	};
	
	/**
	 * search the best svm parameter
	 * @param data training data
	 * @return svm_parameter
	 */
	public static svm_parameter update_param(Data data) {
		// no training outputs
		svm_print_interface print_func = svm_print_null;
		svm.svm_set_print_string_function(print_func);
		
		svm_parameter param = new svm_parameter();
		param.svm_type = svm_parameter.C_SVC;
        param.kernel_type = svm_parameter.RBF;
//        param.degree = 9;
//        param.coef0 = 0.0;
        param.eps = 0.01;
        param.shrinking = 0;
        param.probability = 0;
        param.nr_weight = 0;
        param.cache_size = 100;
        
		int best_power_of_c = C_START_VALUE;
		int best_power_of_g = G_START_VALUE;
		double hit_rate;// = 0.0;
		double best_hit_rate = 0.0;
		for (int power_of_c = C_START_VALUE; power_of_c < C_STOP_VALUE; power_of_c += C_STEP) {
			for (int power_of_g = G_START_VALUE; power_of_g < G_STOP_VALUE; power_of_g += G_STEP) {
				hit_rate = do_cross_validation(data, power_of_c, power_of_g, 10, param);
				System.out.printf("poc: " + power_of_c + "; pog: " + power_of_g + "; hit_rate: %.2f%%", hit_rate);
				if (hit_rate < 0.6) { // pruning hehe
					System.out.printf("; best poc: " + best_power_of_c + "; best pog: " + best_power_of_g + "; best hit rate: %.2f%%\n", best_hit_rate);
					continue;
				} else if ((hit_rate > best_hit_rate)
//						|| (hit_rate == best_hit_rate
						|| (Math.abs(hit_rate - best_hit_rate) < 0.00001
//						&& power_of_g == best_power_of_g
						&& power_of_c < best_power_of_c)) {
					best_hit_rate = hit_rate;
					best_power_of_c = power_of_c;
					best_power_of_g = power_of_g;
					System.out.printf("; best poc: " + best_power_of_c + "; best pog: " + best_power_of_g + "; best hit rate: %.2f%%\n", best_hit_rate);
				} else {
					System.out.printf("; best poc: " + best_power_of_c + "; best pog: " + best_power_of_g + "; best hit rate: %.2f%%\n", best_hit_rate);
				}
			}
		}
		param.C = Math.pow(2, best_power_of_c);
		param.gamma = Math.pow(2, best_power_of_g);
		System.out.println("best C: " + param.C + "; best gamma: " + param.gamma + "; accuracy: " + best_hit_rate);
		return param;
	}
	
	public static void main(String[] args) {
		/* query: get sets from database
		 * colom 1: labels
		 * colom 2 - N: features
		 */
		String train_query;
		String limit = "limit 2500";
		
		Vector<String> features = new Vector<>();
		features.add("entry_head.special_flag");
		features.add("entry_head.i_e_flag");
		//features.add("entry_head.decl_port");
		//features.add("entry_head.trade_country");
		//features.add("entry_head.destination_port");
		features.add("UNIX_TIMESTAMP(entry_head.d_date)");
		features.add("entry_head.trade_mode");
		features.add("entry_list.code_ts");
		features.add("entry_list.qty_1");
		features.add("entry_list.usd_price");
		
		/* train query */
		train_query = "select";
		for (int i = 0; i < features.size() - 1; i++) {
			train_query +=  " " + features.get(i) + ",";
		}
		train_query += " " + features.get(features.size() - 1);
		
		train_query += " from entry_head inner join entry_list on entry_head.entry_id = entry_list.entry_id";
		train_query += " where entry_head.d_date < '2010-03-05' and entry_head.special_flag = 1 " + limit;
		train_query += " union (select";
		for (int i = 0; i < features.size() - 1; i++) {
			train_query +=  " " + features.get(i) + ",";
		}
		train_query += " " + features.get(features.size() - 1);
		train_query += " from entry_head inner join entry_list on entry_head.entry_id = entry_list.entry_id";
		train_query += " where entry_head.d_date < '2010-03-05' and entry_head.special_flag = 0 " + limit + ");";

		Connection connection = DB_manager.get_DB_connection();
		
		Data data = new Data();
		try {
			data.read_data(connection, train_query);
			data.scale_data();
			update_param(data);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		DB_manager.return_DB_connection(connection);
	}
}
