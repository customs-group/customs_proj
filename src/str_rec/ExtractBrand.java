package str_rec;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.OutputStreamWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Vector;

import util.DB_manager;

public class ExtractBrand {
	private static Vector<String[]> goods;

	/**
	 * 从数据库读取数据
	 */
	private static void read_data_from_db() {
		String DB_DRIVER = "com.mysql.jdbc.Driver";
		String DB_CONNECTION = "jdbc:mysql://ggcis01rdspublic.mysql.rds.aliyuncs.com:3309/cis_0001";
		String DB_USER = "hzhg";
		String DB_PASSWORD = "1qaz2wsx";
		Connection connection = DB_manager.get_DB_connection(DB_DRIVER, DB_CONNECTION, DB_USER, DB_PASSWORD);
		String query = "select distinct g_name, g_model from entry_list where code_ts = '8708299000';";
		
		try {
			PreparedStatement pstmt = connection.prepareStatement(query);
			ResultSet rs = pstmt.executeQuery();
			// read data
			while(rs.next()) {
				String g_name = rs.getString(1);
				String g_model = rs.getString(2);
				if (g_model == null) {
					g_model = "";
				}
				String[] good = new String[2];
				good[0] = g_name;
				good[1] = g_model;
				goods.add(good);
			}
			// end data preparation
			if (rs != null) {
				rs.close();
			}
			if (pstmt != null) {
				pstmt.close();
			}
			if (connection != null) {
				connection.close();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 从文件读取数据
	 */
	private static void read_data_from_file() {
		String gname_file_name = "/Users/edwardlol/Downloads/ALL_CODE/gname_all";
		String gmodel_file_name = "/Users/edwardlol/Downloads/ALL_CODE/gmodel_all";
		String g_name, g_model;
        try {
        	File gname_file = new File(gname_file_name);
        	BufferedReader gname_reader = new BufferedReader(new FileReader(gname_file));
        	File gmodel_file = new File(gmodel_file_name);
        	BufferedReader gmodel_reader = new BufferedReader(new FileReader(gmodel_file));
        	g_name = gname_reader.readLine();
        	g_model = gmodel_reader.readLine();
        	g_name = gname_reader.readLine();
        	g_model = gmodel_reader.readLine();
            while (g_name != null && g_model != null) {
                String[] good = new String[2];
                if (g_model.equals("NULL")) {
					g_model = "";
				}
				good[0] = g_name;
				good[1] = g_model;
				goods.add(good);
				g_name = gname_reader.readLine();
	        	g_model = gmodel_reader.readLine();
            }
            gname_reader.close();
            gmodel_reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
	
	/**
	 * 读取数据
	 * @param from 读取的源类型
	 */
	private static void read_data(String from) {
		if (from.toLowerCase().equals("db")) {
			read_data_from_db();
		} else if (from.toLowerCase().equals("file")) {
			read_data_from_file();
		} else {
			System.out.println("wrong data source!");
		}
	}
	
	public static void main(String[] args) {
		goods = new Vector<String[]>();
		try {
			read_data("file");
			String filename = "./datasets/goods.txt";
			File file = new File(filename);
			FileOutputStream fos = new FileOutputStream(file);
			OutputStreamWriter writer = new OutputStreamWriter(fos, "UTF-8");
			
			for(String[] _good : goods) {
				String g_name = _good[0];
				String g_model = _good[1];
				writer.append("g_name: " + g_name + "; g_model: " + g_model + "\n");
				Good good = new Good();
				good.set_good_by_gname(g_name);
				good.set_good_by_gmodel(g_model);

				writer.append("品牌: " + good.get_brand() + "\n");
				writer.append("型号: " + good.get_type() + "\n");
				writer.append("其他: " + good.get_discription() + "\n");
			}
			System.out.println("Data record done! see \"" + filename + "\"");
			// end data preparation
			if (writer != null) {
		    	writer.close();
		    }
			if (fos != null) {
				fos.close();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

