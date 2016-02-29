package g_lib;

import util.ODPSHelper;

/**
 * Created by edwardlol on 15/12/17.
 */
public class ODPS_Test {

    public static void main(String[] args) {
        String sql_query = "select * from thm_conta_ei limit 10;";
        ODPSHelper odpsHelper = new ODPSHelper();

        String result = odpsHelper.executeNonQuery(sql_query);
        System.out.println(result);
    }
}
