package cyou.pymiliblog.smartgastransportcontrol.service.pipeline;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import cyou.pymiliblog.smartgastransportcontrol.common.ApiResponse;
import cyou.pymiliblog.smartgastransportcontrol.entity.pipeline.PipelineDataEntity;
import cyou.pymiliblog.smartgastransportcontrol.mapper.pipeline.PipelineDataMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class PipelineDataService {
    private static final Logger log = LoggerFactory.getLogger(PipelineDataService.class);

    private final PipelineDataMapper pipelineDataMapper;

    public PipelineDataService(PipelineDataMapper pipelineDataMapper) {
        this.pipelineDataMapper = pipelineDataMapper;
    }

    public ResponseEntity<ApiResponse<Object>> getByPipelineName(String pipelineName) {
        log.info("通过管线名称获取数据，pipelineName: {}", pipelineName);
        QueryWrapper<PipelineDataEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("pipeline_name", pipelineName);
        List<PipelineDataEntity> entities;
        try {
            entities = pipelineDataMapper.selectList(queryWrapper);
        } catch (Exception e) {
            log.error("数据库查询时错误：{}", e.toString());
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.internalError("数据库错误！"));
        }
        if (entities.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.badRequest("获取失败！"));
        }
        return ResponseEntity.ok(
                ApiResponse.success("获取成功！", entities));
    }

    public ResponseEntity<ApiResponse<Object>> getByPipelineNameAndDatetime(
            String pipelineName, LocalDateTime dateTime) {
        log.info("通过管线名称和时间获取数据，pipeline name: {}, datetime: {}", pipelineName, dateTime);
        QueryWrapper<PipelineDataEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("pipeline_name", pipelineName)
                .eq("datetime", dateTime);

        List<PipelineDataEntity> entities;
        try {
            entities = pipelineDataMapper.selectList(queryWrapper);
        } catch (Exception e) {
            log.error("通过管线名称和时间获取数据，数据库错误！, {}", e.toString());
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.internalError("获取失败!服务器错误！"));
        }
        if (entities.isEmpty()) {
            log.warn("获取的管线数据为空！pipeline name: {}, " +
                    "date time: {}", pipelineName ,dateTime);
            return ResponseEntity.badRequest()
                    .body(ApiResponse.badRequest("获取错误！"));
        }
        return ResponseEntity.ok(ApiResponse.success("获取成功！", entities));
    }

    public ResponseEntity<ApiResponse<Object>> getLatestByPipelineName(String pipelineName) {
        log.info("通过管道名获取最新数据，pipeline name: {}", pipelineName);
        QueryWrapper<PipelineDataEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("pipeline_name", pipelineName);
        queryWrapper.orderByDesc("datetime");

        List<PipelineDataEntity> entities;
        try {
            entities = pipelineDataMapper.selectList(queryWrapper);
        } catch (Exception e) {
            log.error("数据库错误！pipeline name: ｛｝," +
                    " error: {}", pipelineName, e);
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.internalError("获取最新数据失败！数据库错误！"));
        }

        if (entities.isEmpty()) {
            log.warn("获取的管线数据为空！pipeline name: {}", pipelineName);
            return ResponseEntity.badRequest()
                    .body(ApiResponse.badRequest("获取失败！"));
        }

        return ResponseEntity.ok(ApiResponse.success("获取成功！", entities.getFirst()));
    }
    public ResponseEntity<ApiResponse<Object>> getLatestByPipelineNames(List<String> pipelineNames) {
        log.info("通过管道名列表获取最新数据，pipeline name list: {}", pipelineNames.size());

        List<PipelineDataEntity> entities = new ArrayList<>();
        for (String pipelineName : pipelineNames) {
            QueryWrapper<PipelineDataEntity> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("pipeline_name", pipelineName);
            queryWrapper.orderByDesc("datetime");

            try {
                entities.add(pipelineDataMapper.selectList(queryWrapper).getFirst());
            } catch (Exception e) {
                log.error("数据库错误！pipeline name: ｛｝," +
                        " error: {}", pipelineName, e);
                return ResponseEntity.internalServerError()
                        .body(ApiResponse.internalError("获取最新数据失败！数据库错误！"));
            }
        }

        if (entities.isEmpty()) {
            log.warn("获取的管线数据为空！pipeline name list: {}", pipelineNames);
            return ResponseEntity.badRequest()
                    .body(ApiResponse.badRequest("获取失败！"));
        }

        return ResponseEntity.ok(ApiResponse.success("获取成功！", entities));
    }

}
