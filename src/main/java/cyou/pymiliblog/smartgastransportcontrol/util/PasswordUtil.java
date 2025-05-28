package cyou.pymiliblog.smartgastransportcontrol.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class PasswordUtil {

    private static final String ALGORITHM = "SHA-256"; // 使用 SHA-256 算法
    public static final String SALT = "wbZpRw4qblSs4Rp9wod3BGenxFYPgwacC4i+AiyJVNQ=";
    private static final Logger log = LoggerFactory.getLogger(PasswordUtil.class);

    /**
     * <h3>对密码进行哈希加盐处理</h3>
     * @param rawPassword 原始密码
     * @param salt        指定的盐值
     * @return 加盐后的哈希密码
     */
    public static String hashPassword(String rawPassword, String salt) {
        try {
            // 将盐值和密码拼接
            String saltedPassword = salt != null ? rawPassword + salt : rawPassword;

            // 对拼接后的字符串进行哈希
            MessageDigest digest = MessageDigest.getInstance(ALGORITHM);
            byte[] hashedPassword = digest.digest(saltedPassword.getBytes());

            // 将字节数组转换为十六进制字符串
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashedPassword) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }

            // 返回十六进制字符串
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Unsupported hash algorithm: " + ALGORITHM, e);
        }
    }

    public static String hashPassword(String rawPassword) {
        return hashPassword(rawPassword, null);
    }

    /**
     * <h3>验证原始密码是否与哈希密码匹配</h3>
     * @param rawPassword 原始密码
     * @param hashedPassword 加盐后的哈希密码
     * @return 如果原始密码与哈希密码匹配，则返回 true，否则返回 false
     */
    public static boolean verifyPassword(String rawPassword, String hashedPassword) {
        // 对密码加盐hash
        rawPassword = hashPassword(rawPassword, PasswordUtil.SALT);
        log.info("{} equals {}", rawPassword, hashedPassword);
        // 比较哈希值
        return rawPassword.equals(hashedPassword);
    }
}