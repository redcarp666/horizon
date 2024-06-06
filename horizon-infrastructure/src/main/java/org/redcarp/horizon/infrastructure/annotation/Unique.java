package org.redcarp.horizon.infrastructure.annotation;

import java.lang.annotation.*;


@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Unique {


	String column() default "";


	String code() default "";
}
