package str_rec;

/**
 * Created by edwardlol on 15/9/6.
 */
public class Pair {
    private String entry_id;
    private String g_no;

    /**
     * 构造Pair类对象
     * @param entry_id entry_id
     * @param g_no g_no
     */
    public Pair(String entry_id, String g_no) {
        this.entry_id = entry_id;
        this.g_no = g_no;
    }

    /**
     * 将对象转变为字符串形式
     * @return [entry_id:g_no]
     */
    public String toString() {
        return "<" + this.entry_id + ":" + this.g_no + ">";
    }

    public String get_entry_id() {
        return this.entry_id;
    }
    public String get_g_no() {
        return this.g_no;
    }
}
