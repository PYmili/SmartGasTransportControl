package cyou.pymiliblog.smartgastransportcontrol.controller.user;

import cyou.pymiliblog.smartgastransportcontrol.common.ApiResponse;
import cyou.pymiliblog.smartgastransportcontrol.entity.user.RequestUserEntity;
import cyou.pymiliblog.smartgastransportcontrol.entity.user.UserEntity;
import cyou.pymiliblog.smartgastransportcontrol.service.user.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController<T> {

    // service
    private final UserService<UserEntity> userService;

    public UserController(UserService<UserEntity> userService) {
        this.userService = userService;
    }

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<String>> register(@RequestBody UserEntity entity) {
        log.info("用户注册, request entity: {}", entity);
        if (entity == null
                || entity.getUsername() == null
                || entity.getPasswordHash() == null) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.badRequest("参数残缺！"));
        }
        return userService.register(entity);
    }

    @PostMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<String>> update(@RequestBody UserEntity entity) {
        log.info("更新用户数据, request entity: {}", entity);
        if (entity == null
                || entity.getUsername() == null
                || entity.getPasswordHash() == null) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.badRequest("参数残缺！"));
        }
        return userService.update(entity);
    }

    @PostMapping(value = "/delete", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<String>> delete(@RequestBody Map<String, Object> request) {
        log.info("删除用户, request: {}", request);
        if (request == null || request.isEmpty() || !request.containsKey("id")) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.badRequest("参数残缺！"));
        }
        return userService.delete((Integer) request.get("id"));
    }

    @RequestMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<Object>> list() {
        return userService.list();
    }

    @PostMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<String>> login(@RequestBody RequestUserEntity userEntity) {
        if (userEntity == null
                || userEntity.getUsername() == null
                || userEntity.getPassword() == null) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.badRequest("请求参数不全！"));
        }
        return userService.login(userEntity);
    }

    @PostMapping(value = "/verify", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<String>> verify(@RequestBody Map<String, String> request) {
        log.info("verify {}", request);
        if (request == null
                || request.get("jwt") == null
                || request.get("jwt").isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.badRequest("验证不通过！"));
        }
        return userService.verify(request.get("jwt"));
    }
}
