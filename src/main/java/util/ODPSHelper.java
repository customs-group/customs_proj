package util;

import com.aliyun.odps.Instance;
import com.aliyun.odps.Odps;
import com.aliyun.odps.account.Account;
import com.aliyun.odps.account.AliyunAccount;
import com.aliyun.odps.task.SQLTask;

/**
 *
 * Created by edwardlol on 15/9/25.
 */
public class ODPSHelper {
    private String accessId = "E69BNv61WlncZ1z4";//"WSv9D4bFBn0cfKDM";
    private String accessKey = "R9kOEH9XjOxDyrdrJjJABJziCRBmJM";//"LQ1FKGJcLaUY7vB44gd0M6UgCSjgF0";
    private String odpsUrl = "http://service.odps.aliyun-inc.com/api";
    private String project = "cia";
    Odps odps = null;

    public ODPSHelper() {
        Account account = new AliyunAccount(accessId, accessKey);
        odps = new Odps(account);
        odps.setEndpoint(odpsUrl);
        odps.setDefaultProject(project);
    }

    public String executeNonQuery(String sql) {
        String r = "";
        try {
            Instance instance = SQLTask.run(odps,sql);
            String id = instance.getId();
            instance.waitForSuccess();
        } catch (Exception e) {
            r = e.getMessage();
        }
        return r;
    }
}
