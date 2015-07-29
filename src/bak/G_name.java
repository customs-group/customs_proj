package bak;

import java.util.ArrayList;
import java.util.List;

public class G_name {
	private List<String> content_list;
	private String content_string;
	private int sec_num;
	private static String _filter = "[()（）\\[\\]［］\\{\\}｛｝，,。.、\\\\  \'\"‘’“”]";
	
	public G_name(String string) {
		sec_num = 0;
		this.content_string = "";
		this.content_list = new ArrayList<String>();
		String[] stringSec = string.split(_filter);
		for (String i : stringSec) {
			if (!i.equals("")) {
				content_list.add(i);
				content_string += i;
				sec_num++;
			}
		}
	}
	
	public String getContentString() {
		return this.content_string;
	}
	
	public int getSec_num() {
		return this.sec_num;
	}
	
	public List<String> getContentList() {
		return this.content_list;
	}
	
	public static String getFilter() {
		return _filter;
	}
	
	public static void setFilter(String filter) {
		_filter = filter;
	}
	
	public void printContent() {
		for (String i : this.content_list) {
			System.out.println("hehe: " + i);
		}
	}
	
}
