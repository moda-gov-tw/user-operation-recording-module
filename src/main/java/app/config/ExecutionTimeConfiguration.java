package app.config;

import app.interceptor.ExecutionInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ExecutionTimeConfiguration implements WebMvcConfigurer {

    @Override
    public void addInterceptors(final InterceptorRegistry registry) {
        final ExecutionInterceptor executionInterceptor = new ExecutionInterceptor();
        registry.addInterceptor(executionInterceptor);
    }
}
