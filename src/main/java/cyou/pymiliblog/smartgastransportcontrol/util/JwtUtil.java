package cyou.pymiliblog.smartgastransportcontrol.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;

public class JwtUtil {
    // JWT secret key
    private static final String SECRET_KEY =
            "1a5b6f1724410e683ac5f4879982eebb003e7eda501fa4f374683d98ba95fa98";
    // JWT 有效时间（毫秒）
    public static final long JWT_TTL = 60 * 60 * 1000L;

    /**
     * <h3>生成 JWT</h3>
     * @param subject 用户信息（如用户名）
     * @param ttlMillis 有效时间（毫秒），如果不传则使用默认值
     * @return JWT 令牌
     */
    public static String generateToken(String subject, Long ttlMillis, Map<String, Object> claims) {
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        long expMillis = nowMillis + (ttlMillis != null ? ttlMillis : JWT_TTL);
        Date exp = new Date(expMillis);

        SecretKey secretKey = Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
        return Jwts.builder()
                .subject(subject) // 设置主题
                .claims(claims) // 设置声明
                .issuedAt(now) // 设置签发时间
                .expiration(exp) // 设置过期时间
                .signWith(secretKey) // 设置签名算法
                .compact(); // 构建 JWT
    }

    public static String generateToken(String subject, Map<String, Object> claims) {
        return generateToken(subject, JWT_TTL, claims);
    }

    /**
     * <h3>解析 JWT</h3>
     * @param token JWT 令牌
     * @return Claims 对象（包含 JWT 的载荷信息）
     */
    public static Claims parseToken(String token) {
        try {
            SecretKey secretKey = Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
            return Jwts.parser()
                    .verifyWith(secretKey) // 设置签名密钥
                    .build()
                    .parseSignedClaims(token) // 解析 JWT
                    .getPayload(); // 获取载荷
        } catch (ExpiredJwtException e) {
            throw new RuntimeException("过期的 JWT 令牌", e);
        } catch (SignatureException e) {
            throw new RuntimeException("JWT 令牌签名无效", e);
        } catch (MalformedJwtException e) {
            throw new RuntimeException("JWT 令牌无效", e);
        } catch (Exception e) {
            throw new RuntimeException("处理程序的 JWT 令牌压缩无效", e);
        }
    }

    /**
     * <h3>验证 JWT 是否过期</h3>
     * @param token JWT 令牌
     * @return true 表示已过期，false 表示未过期
     */
    public static boolean isTokenExpired(String token) {
        try {
            // 尝试解析令牌
            Claims claims = parseToken(token);
            // 检查过期时间
            return claims.getExpiration().before(new Date());
        } catch (RuntimeException e) {
            // 处理特定过期异常
            if (e.getCause() instanceof ExpiredJwtException) {
                return true;
            }
            // 其他异常重新抛出
            throw e;
        }
    }
}