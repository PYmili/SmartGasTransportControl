package cyou.pymiliblog.smartgastransportcontrol.service.route;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import cyou.pymiliblog.smartgastransportcontrol.common.ApiResponse;
import cyou.pymiliblog.smartgastransportcontrol.entity.route.RouteEntity;
import cyou.pymiliblog.smartgastransportcontrol.mapper.route.RouteMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RouteService<T> {

    private static final Logger log = LoggerFactory.getLogger(RouteService.class);
    private final RouteMapper routeMapper;

    public RouteService(RouteMapper routeMapper) {
        this.routeMapper = routeMapper;
    }

    public ResponseEntity<ApiResponse<Object>> getByRange(Integer offset, Integer size) {
        // 默认每页10个
        if (size == null || size == 0)
            size = 10;
        log.info("查询页：offset: {}, size: {}", offset, size);
        QueryWrapper<RouteEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.last("limit " + offset + ", " + size);
        List<RouteEntity> entities = routeMapper.selectList(queryWrapper);
        QueryWrapper<RouteEntity> queryWrapperCount = new QueryWrapper<>();
        Long total = routeMapper.selectCount(queryWrapperCount);
        log.info("查询结果：list: {}, total: {}", entities.size(), total);
        return ResponseEntity.ok(
                ApiResponse.success(
                        "成功！",
                        Map.of("list", entities, "total", total)
                )
        );
    }

    public ResponseEntity<ApiResponse<String>> append(RouteEntity entity) {
        try {
            int insert = routeMapper.insert(entity);
            if (insert <= 0) {
                log.warn("新增失败！insert value: {}", insert);
                return ResponseEntity.badRequest()
                        .body(ApiResponse.badRequest("新增失败！"));
            }
        } catch (Exception e) {
            log.error("写入数据库失败！{}", e.toString());
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.internalError("新增失败！服务器错误！"));
        }
        return ResponseEntity.ok(ApiResponse.success("新增成功！"));
    }

    public ResponseEntity<ApiResponse<String>> deleteById(Integer id) {
        try {
            int deletedResult = routeMapper.deleteById(id);
            if (deletedResult <= 0) {
                return ResponseEntity.badRequest()
                        .body(ApiResponse.badRequest("删除数据失败！"));
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.internalError("删除数据失败！服务器错误！"));
        }
        return ResponseEntity.ok(ApiResponse.success("删除成功！"));
    }

    public ResponseEntity<ApiResponse<String>> batchDeleteByIds(List<Integer> ids) {
        try {
            for (Integer id : ids) {
                int deletedResult = routeMapper.deleteById(id);
                if (deletedResult <= 0) {
                    return ResponseEntity.badRequest()
                            .body(ApiResponse.badRequest("删除数据失败！"));
                }
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.internalError("删除数据失败！服务器错误！"));
        }
        return ResponseEntity.ok(ApiResponse.success("删除成功！"));
    }

    public ResponseEntity<ApiResponse<String>> deleteByLongName(String longName) {
        try {
            Map<String, Object> deleteMap = new HashMap<>();
            deleteMap.put("long_name", longName);
            int deletedResult = routeMapper.deleteByMap(deleteMap);
            if (deletedResult <= 0) {
                return ResponseEntity.badRequest()
                        .body(ApiResponse.badRequest("删除数据失败！"));
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.internalError("删除数据失败！服务器错误！"));
        }
        return ResponseEntity.ok(ApiResponse.success("删除成功！"));
    }
}
