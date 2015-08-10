package util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DB_manager {
	private static final String DB_DRIVER = "com.mysql.jdbc.Driver";
	private static final String config_file = "./custom_proj.conf";
	
	private static String DB_CONNECTION = "";
	private static String DB_USER = "";
	private static String DB_PASSWORD = "";
	
	public static Connection get_DB_connection() {
		Connection connection = null;
		// read config file
		try {
			FileReader file_reader = new FileReader(config_file);
	    	BufferedReader buffered_reader = new BufferedReader(file_reader);
	    	String config = buffered_reader.readLine();
	        while (config != null) {
	        	String[] result = config.split(": ");
	        	switch (result[0]) {
	        	case "DB_CONNECTION":
	        		DB_CONNECTION = result[1];
	        		break;
	        	case "DB_USER":
	        		DB_USER = result[1];
	        		break;
	        	case "DB_PASSWORD":
	        		DB_PASSWORD = result[1];
	        		break;
	        	default:
	        		break;
	        	}
	        	config = buffered_reader.readLine();
	        }
	        buffered_reader.close();
	        file_reader.close();
		} catch (Exception e) {
			System.out.println("read config file failed!");
			e.printStackTrace();
		}
		
		// get db connection
		try {
			Class.forName(DB_DRIVER);
			connection = DriverManager.getConnection(DB_CONNECTION, DB_USER, DB_PASSWORD);
			return connection;
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return connection;
	}
	
	public static void return_DB_connection(Connection connection) {
		if (connection != null) {
			try {
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
