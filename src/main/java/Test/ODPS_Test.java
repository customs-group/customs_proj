package Test;

import util.ODPSHelper;

/**
 * Created by edwardlol on 15/9/25.
 */
public class ODPS_Test {

    public static void main(String[] args) {
        ODPSHelper oh = new ODPSHelper();
        String result = oh.executeNonQuery("select * from entry_head limit 10;");
        System.out.println(result);
    }
}
