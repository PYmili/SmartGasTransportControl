package cyou.pymiliblog.smartgastransportcontrol.service.pspace;

import com.alibaba.fastjson2.JSON;
import cyou.pymiliblog.smartgastransportcontrol.service.api.OkHttpService;
import okhttp3.OkHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class PSpaceService {
    private static final Logger log = LoggerFactory.getLogger(PSpaceService.class);
    private final String API_HOST = "http://10.89.6.35:11210/webapi/";
    private final OkHttpClient okHttpClient = new OkHttpClient();
    private final OkHttpService okHttpService = new OkHttpService(okHttpClient);

    public boolean setUserInfo() throws IOException {
        String url = API_HOST + "setUserInfo";
        Map<String, String> map = new HashMap<>();
        map.put("pspaceUsername", "psMonitor_SN");
        map.put("pspacePassword", "SNPS@20220213!");
        map.put("thirdAppId", "SNQKZHZNZHZX");
        map.put("licenseCode", "tyWzcIfq");
        String response = okHttpService.postForm(url, map);
        if (response == null) {
            log.error("未初始化成功！");
            return false;
        }
        return true;
    }

    public String PSpaceQueryByRange(LocalDate start, LocalDate end) throws IOException {
        boolean setUserInfoBool = this.setUserInfo();
        if (!setUserInfoBool) {
            return null;
        }
        Map<String, String> map = new HashMap<>();
        map.put("appId", "SNQKZHZNZHZX");
        map.put("licenseCode", "tyWzcIfq");
        map.put("thirdAppId", "SNQKZHZNZHZX");
        map.put("start", "2025-05-20 16:13:00");
        map.put("end", "2025-05-21 16:13:21");
        map.put("tags", "\\M230484PI00005010");
        map.put("method", "h_");
        String response = new OkHttpService(this.okHttpClient)
                .postForm("http://10.89.6.35:11210/webapi/PSpaceQuery.ashx",
                        map);
        if (response == null || response.isEmpty()) {
            log.warn("查询失败！");
            return null;
        }
        return response;
    }
}
