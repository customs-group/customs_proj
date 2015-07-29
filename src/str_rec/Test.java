package str_rec;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import util.DB_manager;

public class Test {

	public static void main(String[] args) {
		String DB_DRIVER = "com.mysql.jdbc.Driver";
		String DB_CONNECTION = "jdbc:mysql://ggcis01rdspublic.mysql.rds.aliyuncs.com:3309/cis_0001";
		String DB_USER = "hzhg";
		String DB_PASSWORD = "1qaz2wsx";
		Connection connection = DB_manager.get_DB_connection(DB_DRIVER, DB_CONNECTION, DB_USER, DB_PASSWORD);
		
		String query = "select distinct g_name, g_model from entry_list where code_ts = '8708299000';";
		
		try {
			String filename = "./datasets/goods.txt";
			File file = new File(filename);
			FileOutputStream fos = new FileOutputStream(file);
			OutputStreamWriter writer = new OutputStreamWriter(fos, "UTF-8");
			
			PreparedStatement pstmt = connection.prepareStatement(query);
			ResultSet rs = pstmt.executeQuery();
			/* read data */
			while(rs.next()) {
				String g_name = rs.getString(1);
				String g_model = rs.getString(2);
				if (g_model == null) {
					g_model = "";
				}
				writer.append("g_name: " + g_name + "; g_model: " + g_model + "\n");
				Good good = new Good();
				good.set_good(g_name);
				if (good.get_brand().equals("无")) {
					good.set_good(g_model);
				}
				writer.append("品牌: " + good.get_brand() + "\n");
			}
			System.out.println("Data record done! see " + filename);
			 /* end data preparation */
			if (writer != null) {
		    	writer.close();
		    }
			if (fos != null) {
				fos.close();
			}
			if (rs != null) {
				rs.close();
			}
			if (pstmt != null) {
				pstmt.close();
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	    
	   
		
/*		
		String content1 = "品牌honda,hehehe";
		String content2 = ":  honda-雅阁牌hehehe";
		String content3 = "品牌：  本田-arcord,hehehe";
		String content4 = ":  本田-arcord牌hehehe";
		String content5 = ":  本田-arcord无品牌hehehe";
*/

	}
}
