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
import java.util.List;
import java.util.Map;

@Service
public class UserService<T> {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);
    // User Mapper
    private final UserMapper userMapper;

    public UserService(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public ResponseEntity<ApiResponse<Object>> list() {
        return ResponseEntity.ok(ApiResponse.success(
                "获取成功！",
                userMapper.selectList(new QueryWrapper<>())
        ));
    }

    public ResponseEntity<ApiResponse<String>> register(UserEntity entity) {
        // 检查用户名是否已存在
        LambdaQueryWrapper<UserEntity> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(UserEntity::getUsername, entity.getUsername());
        UserEntity existingUser = userMapper.selectOne(lambdaQueryWrapper);
        if (existingUser != null) {
            log.warn("用户 {} 已存在", entity.getUsername());
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(ApiResponse.badRequest("用户名已存在！"));
        }

        // 将用户信息保存到数据库中
        try {
            int result = userMapper.insert(entity);
            if (result > 0) {
                return ResponseEntity.ok(ApiResponse.success("注册成功！"));
            } else {
                log.error("用户注册失败");
                return ResponseEntity.internalServerError()
                        .body(ApiResponse.internalError("注册失败！"));
            }
        } catch (Exception e) {
            log.error("注册过程中发生错误：{}", e.toString());
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.internalError("注册失败！"));
        }
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
        // 用户是否可用
        if (!queryEntity.getAction()) {
            log.info("用户已封禁！username: {}", userEntity.getUsername());
            return ResponseEntity.badRequest()
                    .body(ApiResponse.badRequest("用户已封禁！"));
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

    public ResponseEntity<ApiResponse<String>> update(UserEntity entity) {
        // 检查用户是否存在
        LambdaQueryWrapper<UserEntity> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(UserEntity::getId, entity.getId());
        UserEntity existingUser = userMapper.selectOne(lambdaQueryWrapper);
        if (existingUser == null) {
            log.warn("[update] 用户 ID {} 不存在", entity.getId());
            return ResponseEntity.badRequest()
                    .body(ApiResponse.badRequest("用户不存在！"));
        }

        // 对密码加盐
        entity.setPasswordHash(PasswordUtil
                .hashPassword(entity.getPasswordHash(), PasswordUtil.SALT));

        // 更新用户信息
        try {
            int result = userMapper.updateById(entity);
            if (result > 0) {
                return ResponseEntity.ok(ApiResponse.success("用户信息更新成功！"));
            } else {
                log.error("用户信息更新失败");
                return ResponseEntity.internalServerError()
                        .body(ApiResponse.internalError("用户信息更新失败！"));
            }
        } catch (Exception e) {
            log.error("更新用户信息过程中发生错误：{}", e.toString());
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.internalError("用户信息更新失败！"));
        }
    }

    public ResponseEntity<ApiResponse<String>> delete(Integer id) {
        // 检查用户是否存在
        LambdaQueryWrapper<UserEntity> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(UserEntity::getId, id);
        UserEntity existingUser = userMapper.selectOne(lambdaQueryWrapper);
        if (existingUser == null) {
            log.warn("[delete] 用户 ID {} 不存在", id);
            return ResponseEntity.badRequest()
                    .body(ApiResponse.badRequest("用户不存在！"));
        }

        // 删除用户
        try {
            int result = userMapper.deleteById(id);
            if (result > 0) {
                return ResponseEntity.ok(ApiResponse.success("用户删除成功！"));
            } else {
                log.error("用户删除失败");
                return ResponseEntity.internalServerError()
                        .body(ApiResponse.internalError("用户删除失败！"));
            }
        } catch (Exception e) {
            log.error("删除用户过程中发生错误：{}", e.toString());
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.internalError("用户删除失败！"));
        }
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
        List<UserEntity> entity = userMapper.selectList(queryWrapper);
        if (entity == null || entity.getFirst().getUsername() == null) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.badRequest("验证失败！用户不存在"));
        }
        // 判断用户是否封禁
        if (!entity.getFirst().getAction()) {
            log.info("[verify] 用户已封禁！username: {}", entity.getFirst().getUsername());
            return ResponseEntity.badRequest()
                    .body(ApiResponse.badRequest("用户已封禁！"));
        }
        return ResponseEntity.ok(ApiResponse.success("验证通过！"));
    }
}
