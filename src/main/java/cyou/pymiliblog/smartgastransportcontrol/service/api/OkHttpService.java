package cyou.pymiliblog.smartgastransportcontrol.service.api;

import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;

@Service
public class OkHttpService {
    private static final Logger log = LoggerFactory.getLogger(OkHttpService.class);
    private final OkHttpClient okHttpClient;

    public OkHttpService(OkHttpClient okHttpClient) {
        this.okHttpClient = okHttpClient;
    }

    public String get(String url) throws IOException {
        log.info("OkHttp get url: {}", url);
        Request request = new Request.Builder()
                .url(url)
                .build();
        try (Response response = okHttpClient.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                return response.body().string();
            } else {
                throw new IOException("Unexpected code " + response);
            }
        }
    }

    public String post(String url, String json) throws IOException {
        log.info("OkHttp post url: {}", url);
        RequestBody body = RequestBody.create(json, MediaType.parse("application/json; charset=utf-8"));
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        try (Response response = okHttpClient.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                return response.body().string();
            } else {
                if (response.body() != null) {
                    return response.body().string();
                } else {
                    return null;
                }
            }
        }
    }

    public String postForm(String url, Map<String, String> formParams) throws IOException {
        // 创建 FormBody
        FormBody.Builder formBuilder = new FormBody.Builder();
        for (Map.Entry<String, String> entry : formParams.entrySet()) {
            formBuilder.add(entry.getKey(), entry.getValue());
        }
        FormBody formBody = formBuilder.build();

        // 创建 Request
        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();

        // 发送请求
        try (Response response = okHttpClient.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                return response.body().string();
            } else {
                if (response.body() != null) {
                    return response.body().string();
                }
                throw new IOException("Unexpected code " + response);
            }
        }
    }
}
