package cyou.pymiliblog.smartgastransportcontrol.config;

import cyou.pymiliblog.smartgastransportcontrol.interceptor.AuthenticationInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * <h3>web mvc configurer</h3>
 * @author PYmili
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    // 身份验证拦截器
    private final AuthenticationInterceptor authenticationInterceptor;

    WebConfig(AuthenticationInterceptor authenticationInterceptor) {
        this.authenticationInterceptor = authenticationInterceptor;
    }

    /**
     * <h3>通过WebMvcConfigurer添加拦截器。</h3>
     * @param registry {@link InterceptorRegistry}
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authenticationInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/user/login")
                .excludePathPatterns("/user/verify");
    }
}
