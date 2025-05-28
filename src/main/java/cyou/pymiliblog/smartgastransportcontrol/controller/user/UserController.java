package cyou.pymiliblog.smartgastransportcontrol.controller.user;

import cyou.pymiliblog.smartgastransportcontrol.common.ApiResponse;
import cyou.pymiliblog.smartgastransportcontrol.entity.user.RequestUserEntity;
import cyou.pymiliblog.smartgastransportcontrol.entity.user.UserEntity;
import cyou.pymiliblog.smartgastransportcontrol.service.user.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<ApiResponse<T>> register() {
        return ResponseEntity.ok(ApiResponse.success("未开发"));
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
