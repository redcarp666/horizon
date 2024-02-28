package org.redcarp.common.base.utils;

import org.redcarp.common.base.exception.HorizonBusinessException;
import org.redcarp.core.util.AssertionUtils;

/**
 * 先决条件
 *
 * @author redcarp
 * @date 2024/2/18
 */
public class PreconditionUtils {
	/**
	 * 参数必须为true
	 *
	 * @param expression 表达式
	 * @param messageKey 异常code
	 * @param params     异常参数
	 */
	public static void requireTrue(boolean expression, String messageKey, String... params) {
		AssertionUtils.shouldBeTrue(expression, () -> new HorizonBusinessException(messageKey, params));
	}

	/**
	 * 参数必须为false
	 *
	 * @param expression 表达式
	 * @param messageKey 异常code
	 * @param params     异常参数
	 */
	public static void requireFalse(boolean expression, String messageKey, String... params) {
		AssertionUtils.shouldBeFalse(expression, () -> new HorizonBusinessException(messageKey, params));
	}

	public static void requireNotNull(Object obj, String messageKey, String... params) {
		AssertionUtils.shouldNotNull(obj, () -> new HorizonBusinessException(messageKey, params));
	}

	public static void requireNull(Object obj, String messageKey, String... params) {
		AssertionUtils.shouldBeNull(obj, () -> new HorizonBusinessException(messageKey, params));
	}
}
