package app.interceptor;

import app.context.RuntimeContext;
import app.context.ThreadLocalData;
import app.utils.IpUtils;
import com.google.common.base.Stopwatch;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
public class ExecutionInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler) throws Exception {
        try {
            final String ipAddr = IpUtils.getIpAddr(request);
            final String requestURI = request.getRequestURI();
            final ThreadLocalData data = ThreadLocalData
                .builder()
                .apiUrl(requestURI)
                .userIp(ipAddr)
                .threadId(Thread.currentThread().getId())
                .stopwatch(Stopwatch.createStarted())
                .build();

            RuntimeContext.set(data);
        } catch (final Exception e) {
            log.warn(e.getMessage(), e);
        }
        return true;
    }

    @Override
    public void afterCompletion(final HttpServletRequest request, final HttpServletResponse response, final Object handler, final Exception ex) throws Exception {
        try {
            RuntimeContext.remove();
        } catch (final Exception e) {
            log.warn(e.getMessage(), e);
        }
    }
}
