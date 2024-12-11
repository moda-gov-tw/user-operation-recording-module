package app.formatter;

import app.annotation.LogParam;
import app.utils.JSONUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Objects;

/**
 * 參數格式化實作，參數有 {@link app.annotation.LogParam} 註解才會記錄，格式為 JSON
 */
@Component
public class LogParamFormatter implements ParamFormatter {

    @Override
    public FormatResult format(final Method method, final Object[] args) {
        final StringBuilder content = new StringBuilder();
        if (ArrayUtils.isNotEmpty(args)) {
            final Annotation[][] paramAnnotations = method.getParameterAnnotations();
            if (ArrayUtils.isNotEmpty(paramAnnotations)) {
                for (int i = 0; i < paramAnnotations.length; i++) {
                    final Object arg = args[i];
                    final Annotation[] paramAnn = paramAnnotations[i];
                    if(arg == null || ArrayUtils.isEmpty(paramAnn)) {
                        continue;
                    }
                    for (Annotation annotation : paramAnn) {
                        if(annotation.annotationType().equals(LogParam.class)) {
                            if (content.length() > 0) {
                                content.append("$#");
                            }
                            content
                                    .append(arg.getClass().getSimpleName())
                                    .append(" : ")
                                    .append(objectToString(arg));
                        }
                    }
                }
            }
        }
        return new FormatResult(content.toString(), false);
    }

    private String objectToString(final Object arg) {
        final String result = JSONUtils.toJSONString(arg);
        if (result != null) {
            return result;
        }
        return Objects.toString(arg);
    }
}
