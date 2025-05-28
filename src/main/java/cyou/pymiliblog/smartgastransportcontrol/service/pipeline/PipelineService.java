package cyou.pymiliblog.smartgastransportcontrol.service.pipeline;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import cyou.pymiliblog.smartgastransportcontrol.common.ApiResponse;
import cyou.pymiliblog.smartgastransportcontrol.entity.pipeline.PipelineEntity;
import cyou.pymiliblog.smartgastransportcontrol.mapper.pipeline.PipelineMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PipelineService {

    private static final Logger log = LoggerFactory.getLogger(PipelineService.class);
    private final PipelineMapper pipelineMapper;

    public PipelineService(PipelineMapper pipelineMapper) {
        this.pipelineMapper = pipelineMapper;
    }

    public ResponseEntity<ApiResponse<Object>> list() {
        List<PipelineEntity> entities;
        try {
            entities = pipelineMapper.selectList(new QueryWrapper<>());
        } catch (Exception e) {
            log.info("获取失败！数据库获取数据异常：{}", e.toString());
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.internalError("获取失败！数据库错误！"));
        }

        if (entities.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.badRequest("获取失败！"));
        }
        return ResponseEntity.ok(ApiResponse.success("获取成功！", entities));
    }
}
