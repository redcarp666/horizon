package org.redcarp.horizon.core.excel;

import java.lang.annotation.*;

/**
 * @author redcarp
 * @date 2024/5/22
 */
@Documented
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ExcelSelected {

	String[] source() default {};

	Class<? extends ExcelDynamicSelect>[] sourceClass() default {};

	int firstRow() default 1;

	int lastRow() default 0x10000;
}
