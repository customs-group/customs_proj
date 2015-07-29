package ml;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Vector;

import libsvm.*;

public class Test {

	/* get DB connection */
	private static Connection getDBConnection(String DB_DRIVER, String DB_CONNECTION, String DB_USER, String DB_PASSWORD) {
		Connection connection = null;
		try {
			Class.forName(DB_DRIVER);
		} catch (ClassNotFoundException e) {
			System.out.println(e.getMessage());
		}
		try {
			connection = DriverManager.getConnection(DB_CONNECTION, DB_USER, DB_PASSWORD);
			return connection;
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return connection;
	}
	
	public static void main(String[] args) {
		String DB_DRIVER = "com.mysql.jdbc.Driver";
		String DB_CONNECTION = "jdbc:mysql://ggcis01rdspublic.mysql.rds.aliyuncs.com:3309/cis_0001";
		String DB_USER = "hzhg";
		String DB_PASSWORD = "1qaz2wsx";
		
		String train_query;
		String limit = "limit 2500";
		
		Vector<String> features = new Vector<String>();
		features.add("entry_head.special_flag");
		features.add("entry_head.i_e_flag");
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

		Connection connection = getDBConnection(DB_DRIVER, DB_CONNECTION, DB_USER, DB_PASSWORD);
		
		Data data = new Data();
		try {
			data.read_data(connection, train_query);
			data.scale_data();
			Vector<svm_node[]> set;
			svm_node[] sample;
			set = data.get_set("SCALED");
			for (int i = 0; i < set.size(); i ++) {
				System.out.print("sample " + i + ":\t");
				sample = set.get(i);
				for (int j = 0; j < data.get_feature_num(); j ++) {
					System.out.print(sample[j].value + "\t");
				}
				System.out.println();
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		if (connection != null) {
			try {
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
