package app.formatter;

import java.lang.reflect.Method;

/**
 * 參數格式化介面
 */
public interface ParamFormatter {

    FormatResult format(Method method, Object[] args);

}
