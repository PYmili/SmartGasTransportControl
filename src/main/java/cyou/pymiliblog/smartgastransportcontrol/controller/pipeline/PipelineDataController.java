package cyou.pymiliblog.smartgastransportcontrol.controller.pipeline;

import cyou.pymiliblog.smartgastransportcontrol.common.ApiResponse;
import cyou.pymiliblog.smartgastransportcontrol.service.pipeline.PipelineDataService;
import cyou.pymiliblog.smartgastransportcontrol.service.pipeline.PipelineService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/pipeline_data")
public class PipelineDataController {

    private static final Logger log = LoggerFactory.getLogger(PipelineDataController.class);

    private final PipelineDataService pipelineDataService;

    public PipelineDataController(PipelineDataService pipelineDataService) {
        this.pipelineDataService = pipelineDataService;
    }

    @RequestMapping(value = "/get", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<Object>> get(@RequestBody Map<String, Object> request) {
        log.info("获取管线数据, request: {}", request);
        if (request == null || request.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.badRequest("请求参数残缺！"));
        }

        LocalDateTime dateTime = null;
        if (request.containsKey("dateTime")) {
            dateTime = LocalDateTime.parse(
                    (String) request.get("dateTime"),
                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            );
        }

        // 根据管道名获取
        if (request.containsKey("pipelineName")) {
            return pipelineDataService.getByPipelineName(
                    (String) request.get("pipelineName"),
                    dateTime
            );
        }

        // 根据管道名列表获取
        if (request.containsKey("pipelineNames")) {
            Object o = request.get("pipelineNames");
            if (o instanceof List<?> list) {
                if (list.stream().allMatch(item -> item instanceof String)) {
                    @SuppressWarnings("unchecked")
                    List<String> pipelineNames = (List<String>) list;
                    return pipelineDataService.getByPipelineNames(pipelineNames, dateTime);
                } else {
                    return ResponseEntity.badRequest()
                            .body(ApiResponse.badRequest("参数错误！'pipelineNames' 必须是字符串列表。"));
                }
            }
            return ResponseEntity.badRequest()
                    .body(ApiResponse.badRequest("参数错误！"));
        }

        return ResponseEntity.badRequest()
                .body(ApiResponse.badRequest("获取失败！参数残缺！"));
    }
}
