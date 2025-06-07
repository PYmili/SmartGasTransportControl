package cyou.pymiliblog.smartgastransportcontrol.controller.route;

import cyou.pymiliblog.smartgastransportcontrol.common.ApiResponse;
import cyou.pymiliblog.smartgastransportcontrol.controller.user.UserController;
import cyou.pymiliblog.smartgastransportcontrol.entity.route.RouteEntity;
import cyou.pymiliblog.smartgastransportcontrol.service.route.RouteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/route")
public class RouteController<T> {
    // 日志
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    // service
    private final RouteService<RouteEntity> routeService;

    public RouteController(RouteService<RouteEntity> routeService) {
        this.routeService = routeService;
    }

    @PostMapping(value = "/get_by_page", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<Object>> getAirIntakeByRange(
            @RequestBody Map<String, Object> request) {
        log.info("Route get by range request: {}", request);
        if (request == null || !request.containsKey("current")) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.badRequest("请求参数错误！"));
        }
        Integer size = null;
        if (request.containsKey("size")) {
            size = (Integer) request.get("size");
        }
        return routeService.getByRange((Integer) request.get("current"), size);
    }

    @PostMapping(value = "/append", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<String>> append(@RequestBody RouteEntity entity) {
        log.info("线路添加 Router Entity: {}", entity);
        if (entity == null
                || entity.getLongName() == null
                || entity.getDescription() == null) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.badRequest("请求参数残缺！"));
        }
        return routeService.append(entity);
    }

    @PostMapping(value = "/delete", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<String>> delete(@RequestBody Map<String, Object> request) {
        log.info("删除线路, request: {}", request);
        if (request == null || request.isEmpty()) {
            return new ResponseEntity<>(
                    ApiResponse.badRequest("请求参数残缺！"),
                    HttpStatus.NOT_FOUND
            );
        }
        if (request.containsKey("id"))
            return routeService.deleteById((Integer) request.get("id"));

        if (request.containsKey("ids") && request.get("ids") != null) {
            Object RequestIds = request.get("ids");
            if (RequestIds instanceof List<?>) {
                return routeService.batchDeleteByIds((List<Integer>) RequestIds);
            }
            return ResponseEntity.badRequest()
                    .body(ApiResponse.badRequest("请求错误！参数残缺！"));
        }

        if (request.containsKey("longName"))
            return routeService.deleteByLongName((String) request.get("longName"));

        return ResponseEntity.badRequest()
                .body(ApiResponse.badRequest("错误请求，参数残缺！"));
    }
}
