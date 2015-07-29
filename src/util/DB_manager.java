package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DB_manager {
	
	public static Connection get_DB_connection(String DB_DRIVER, String DB_CONNECTION, String DB_USER, String DB_PASSWORD) {
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
