package cyou.pymiliblog.smartgastransportcontrol.interceptor;

import com.alibaba.fastjson2.JSONObject;
import cyou.pymiliblog.smartgastransportcontrol.common.ApiResponse;
import cyou.pymiliblog.smartgastransportcontrol.service.user.UserService;
import cyou.pymiliblog.smartgastransportcontrol.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;

@Component
public class AuthenticationInterceptor implements HandlerInterceptor {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    /**
     * <h3>拦截器预处理</h3>
     * @param request {@link HttpServletRequest}
     * @param response {@link HttpServletResponse}
     * @param handler {@link Object}
     * @return boolean
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                             Object handler) throws IOException {
        // log.info("The authorization pre-handling.");
        log.info("身份验证拦截器预处理。");

        // 输出请求的数据日志
        String remoteAddr = request.getRemoteAddr();
        String requestURL = request.getRequestURL().toString();
        log.info("remote address: {}, request URL: {}", remoteAddr, requestURL);

        // 如果未登录返回值
        ApiResponse<String> notLogin = ApiResponse.badRequest("NOT_LOGIN");
        boolean isNotLogin = false;

        // 获取身份验证的头
        String authentication = request.getHeader("Authorization");
        log.info("request header authorization: {}", authentication);
        if (authentication == null) {
            log.warn("Authorization 参数不存在！");
            isNotLogin = true;
        } else if (!authentication.startsWith("Bearer ")) {
            log.warn("Authorization 未添加Bearer!");
            isNotLogin = true;
        }
        try {
            assert authentication != null;
            authentication = authentication.replace("Bearer ", "");
            // 验证身份
            if (JwtUtil.parseToken(authentication) == null) {
                log.warn("Authorization failed!");
                isNotLogin = true;
            }
        } catch (Exception e) {
            log.info("解析jwt错误！{}", e.toString());
            isNotLogin = true;
        }

        // 用户未登录！
        if (isNotLogin) {
            response.getWriter()
                    .write(JSONObject.toJSONString(notLogin));
            return false;
        }

        // 放行
        return true;
    }

    /**
     * <h3>目标资源方法运行后</h3>
     * @param request {@link HttpServletRequest}
     * @param response {@link HttpServletResponse}
     * @param handler  {@link Object}
     * @param modelAndView {@link ModelAndView}
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) {
        log.info("After the interceptor completes authentication.");
        log.info("身份验证完成。");
    }

    /**
     * <h3>身份验证拦截器最后</h3>
     * @param request {@link HttpServletRequest}
     * @param response {@link HttpServletResponse}
     * @param handler {@link Object}
     * @param ex {@link Exception}
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
                                Exception ex) {
        log.info("Authentication interceptor Over.");
        log.info("身份验证拦截器结束");
    }

}
