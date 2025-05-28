package cyou.pymiliblog.smartgastransportcontrol;

import com.alibaba.fastjson2.JSON;
import cyou.pymiliblog.smartgastransportcontrol.service.api.OkHttpService;
import cyou.pymiliblog.smartgastransportcontrol.service.pspace.PSpaceService;
import cyou.pymiliblog.smartgastransportcontrol.util.JwtUtil;
import cyou.pymiliblog.smartgastransportcontrol.util.PasswordUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import okhttp3.OkHttpClient;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.security.Key;
import java.time.LocalDate;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class SmartGasTransportControlApplicationTests {

    @Test
    void generateJwtKey() {
        // 生成一个随机的 HMAC 密钥
        Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

        // 将密钥转换为 Base64 编码的字符串
        String encodedKey = Base64.getEncoder().encodeToString(key.getEncoded());

        // 打印密钥
        System.out.println("Generated JWT Key: " + encodedKey);

        // 验证密钥是否符合要求
        assertNotNull(encodedKey, "Encoded key should not be null");
        assertFalse(encodedKey.isEmpty(), "Encoded key should not be empty");

        // 验证密钥长度（HMAC SHA-256 的密钥长度为 256 位，即 32 字节）
        assertEquals(32, key.getEncoded().length, "Key should be 256 bits (32 bytes)");
    }

    @Test
    void generateJwt() {
        Map<String, Object> map = new HashMap<>();
        map.put("username", "admin");
        map.put("id", 1);
        String jwt = JwtUtil.generateToken("admin", map);
        System.out.println(jwt);
        Claims claims = JwtUtil.parseToken(jwt);
        for (String s : claims.keySet()) {
            System.out.println(s + ": " + claims.get(s));
        }
    }

    @Test
    void TestPasswordUtil() {
        String admin = PasswordUtil.hashPassword("admin");
        String adminSalt = PasswordUtil.hashPassword(admin, PasswordUtil.SALT);
        System.out.println(admin);
        System.out.println(adminSalt);
        System.out.println(PasswordUtil.verifyPassword(admin, adminSalt));
    }

    // @Test
    // void TestOkHttp() throws IOException {
    //     OkHttpClient okHttpClient = new OkHttpClient();
    //     boolean b = new PSpaceService(new OkHttpService(okHttpClient)).setUserInfo();
    //     System.out.println(b);
    //     Map<String, String> map = new HashMap<>();
    //     map.put("appId", "SNQKZHZNZHZX");
    //     map.put("licenseCode", "tyWzcIfq");
    //     map.put("thirdAppId", "SNQKZHZNZHZX");
    //     map.put("start", "2025-05-20 16:13:00");
    //     map.put("end", "2025-05-21 16:13:21");
    //     map.put("tags", "\\M230484PI00005010");
    //     map.put("method", "h_");
    //     String response = new OkHttpService(okHttpClient)
    //             .postForm("http://10.89.6.35:11210/webapi/PSpaceQuery.ashx",
    //                     map);
    //     System.out.println(JSON.parse(response));
    // }

    @Test
    void pSpaceServiceTest() throws IOException {
        String response = new PSpaceService().PSpaceQueryByRange(
                LocalDate.of(2025, 5, 10),
                LocalDate.of(2025, 5, 21)
        );
        JSON.parseArray(response).forEach(item -> {
            System.out.println(JSON.parse((String) item));
        });
    }

}
