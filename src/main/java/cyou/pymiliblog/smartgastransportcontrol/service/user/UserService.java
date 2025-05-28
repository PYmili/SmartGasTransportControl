package cyou.pymiliblog.smartgastransportcontrol.service.user;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import cyou.pymiliblog.smartgastransportcontrol.common.ApiResponse;
import cyou.pymiliblog.smartgastransportcontrol.entity.user.RequestUserEntity;
import cyou.pymiliblog.smartgastransportcontrol.entity.user.UserEntity;
import cyou.pymiliblog.smartgastransportcontrol.mapper.user.UserMapper;
import cyou.pymiliblog.smartgastransportcontrol.util.JwtUtil;
import cyou.pymiliblog.smartgastransportcontrol.util.PasswordUtil;
import io.jsonwebtoken.Claims;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserService<T> {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);
    // User Mapper
    private final UserMapper userMapper;

    public UserService(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public ResponseEntity<ApiResponse<String>> login(RequestUserEntity userEntity) {
        LambdaQueryWrapper<UserEntity> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(UserEntity::getUsername, userEntity.getUsername());
        UserEntity queryEntity = userMapper.selectOne(lambdaQueryWrapper);
        // 用户不存在
        if (queryEntity == null) {
            log.warn("用户 {} 不存在", userEntity.getUsername());
            return ResponseEntity.badRequest()
                    .body(ApiResponse.badRequest("用户不存在！"));
        }
        // 密码验证
        String passwordHash = queryEntity.getPasswordHash();
        String password = userEntity.getPassword();
        if (!PasswordUtil.verifyPassword(password, passwordHash)) {
            log.warn("用户 {} 密码错误", userEntity.getUsername());
            return ResponseEntity.badRequest()
                    .body(ApiResponse.badRequest("用户或密码错误！"));
        }
        // jwt生成
        Map<String, Object> map = new HashMap<>();
        map.put("username", queryEntity.getUsername());
        map.put("id", queryEntity.getId());
        String jwt = JwtUtil.generateToken("login", map);
        return ResponseEntity.ok(ApiResponse.success("登录成功！", jwt));
    }

    public ResponseEntity<ApiResponse<String>> verify(String jwt) {
        // jwt是否过期
        if (JwtUtil.isTokenExpired(jwt)) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.badRequest("过期令牌！"));
        }

        // 提取id和用户名
        Integer id = -1;
        String username = "";
        try {
            // 解析jwt
            Claims claims = JwtUtil.parseToken(jwt);
            if (claims == null || claims.isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(ApiResponse.badRequest("验证失败！"));
            }
            id = (Integer) claims.get("id");
            username = (String) claims.get("username");
        } catch (Exception ignored) {
            log.error("JWT解析错误！{}", ignored.toString());
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.internalError("解析错误！"));
        }
        if (id == -1 || username.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.badRequest("验证失败！JWT错误"));
        }

        // 查找用户
        QueryWrapper<UserEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.ge("id", id);
        queryWrapper.ge("username", username);
        UserEntity entity = userMapper.selectOne(queryWrapper);
        if (entity == null || entity.getUsername() == null) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.badRequest("验证失败！用户不存在"));
        }
        return ResponseEntity.ok(ApiResponse.success("验证通过！"));
    }
}
