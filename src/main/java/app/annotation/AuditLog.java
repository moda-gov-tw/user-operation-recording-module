package app.annotation;

import app.enums.MethodType;

import java.lang.annotation.*;

@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AuditLog {

    MethodType methodType();

    String paramFormatter() default "";

}
