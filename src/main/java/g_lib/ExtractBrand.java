package g_lib;

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
	public enum read_type {db, file}
	private static Vector<String[]> goods;

	/**
	 * 从数据库读取数据
	 */
	private static void read_data_from_db() {
		Connection connection = DB_manager.get_DB_connection();
		String query = "select distinct g_name, g_model from entry_list where code_ts = '8708299000';";
		
		try {
			PreparedStatement pstmt = connection.prepareStatement(query);
			ResultSet rs = pstmt.executeQuery();
			// read data
			while(rs.next()) {
				String[] good = new String[2];
				good[0] = rs.getString(1);
				good[1] = rs.getString(2);
				if (good[1] == null) {
					good[1] = "";
				}
				goods.add(good);
			}
			// end data preparation
			rs.close();
			pstmt.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		DB_manager.return_DB_connection(connection);
	}
	
	/**
	 * 从文件读取数据
	 */
	private static void read_data_from_file() {
		String source_file = "./datasets/original/all";
        try {
			FileReader file_reader = new FileReader(source_file);
	    	BufferedReader buffered_reader = new BufferedReader(file_reader);

	    	String _read_result = buffered_reader.readLine();
            while (_read_result != null) {
            	String[] read_result;// = new String[5];
            	String[] good = new String[2];
            	System.out.println(_read_result);
            	read_result = _read_result.split("\\t");
            	good[0] = read_result[2];
            	good[1] = read_result[3];
            	if (good[1].equals("NULL")) {
					good[1] = "";
				}
            	goods.add(good);
            	_read_result = buffered_reader.readLine();
            }
            buffered_reader.close();
            file_reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
	
	/**
	 * 读取数据
	 * @param type 读取的源类型
	 */
	private static void read_data(read_type type) {
		switch (type) {
			case db:
				read_data_from_db();
				break;
			case file:
				read_data_from_file();
				break;
			default:
				System.out.println("wrong data source!");
				break;
		}
	}
	
	public static void main(String[] args) {
		goods = new Vector<>();
		try {
			read_data(read_type.file);
			String filename = "./datasets/goods.txt";
			File file = new File(filename);
			FileOutputStream fos = new FileOutputStream(file);
			OutputStreamWriter writer = new OutputStreamWriter(fos, "UTF-8");
			
			for(String[] _good : goods) {
				String g_name = _good[0];
				String g_model = _good[1];

				writer.append("g_name: ");
				writer.append(g_name);
				writer.append("; g_model: ");
				writer.append(g_model);
				writer.append("\n");

				Good good = new Good();
				good.set_good(g_name + g_model);

				writer.append("品牌: ");
				writer.append(good.get_brand());
				writer.append("\n");

				writer.append("型号: ");
				writer.append(good.get_type());
				writer.append("\n");

				writer.append("其他: ");
				writer.append(good.get_discription());
				writer.append("\n");
			}
			System.out.println("SVMData record done! see \"" + filename + "\"");
			// end data preparation
			writer.close();
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

