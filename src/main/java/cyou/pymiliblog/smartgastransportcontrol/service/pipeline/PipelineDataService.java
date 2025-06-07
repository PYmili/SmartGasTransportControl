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

    private final QueryWrapper<PipelineDataEntity> queryWrapper = new QueryWrapper<>();

    public PipelineDataService(PipelineDataMapper pipelineDataMapper) {
        this.pipelineDataMapper = pipelineDataMapper;
    }

    private boolean verifyValueByDateTime(LocalDateTime dateTime) {
        log.info("查看是否有根据{}的数据。", dateTime);
        List<PipelineDataEntity> entities;

        QueryWrapper<PipelineDataEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("dateTime", dateTime);
        try {
            entities = pipelineDataMapper.selectList(queryWrapper);
        } catch (Exception e) {
            log.error("查看时，获取数据失败！{}", e.toString());
            return false;
        }
        return !entities.isEmpty();
    }

    public ResponseEntity<ApiResponse<Object>> getByPipelineName(String pipelineName, LocalDateTime dateTime) {
        log.info("通过管线名称获取数据，pipelineName: {}", pipelineName);

        queryWrapperInit(dateTime, pipelineName);
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

    public ResponseEntity<ApiResponse<Object>> getByPipelineNames(List<String> pipelineNames, LocalDateTime dateTime) {
        log.info("通过管道名列表获取最新数据，pipeline name list: {}", pipelineNames.size());

        List<PipelineDataEntity> entities = new ArrayList<>();
        for (String pipelineName : pipelineNames) {

            queryWrapperInit(dateTime, pipelineName);

            try {
                PipelineDataEntity one = pipelineDataMapper.selectOne(queryWrapper);
                if (one != null)
                    entities.add(one);
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

    private void queryWrapperInit(LocalDateTime dateTime, String pipelineName) {
        queryWrapper.clear();
        queryWrapper.eq("pipeline_name", pipelineName);

        LocalDateTime ldt = dateTime;
        if (!verifyValueByDateTime(dateTime)) {
            QueryWrapper<PipelineDataEntity> qw = new QueryWrapper<>();
            qw.orderByDesc("dateTime");
            ldt = pipelineDataMapper.selectList(qw)
                    .getFirst()
                    .getDateTime();
        }
        queryWrapper.eq("dateTime", ldt);
    }

}
