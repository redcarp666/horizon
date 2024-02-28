package org.redcarp.core.util;

import org.redcarp.core.exception.HorizonRuntimeException;

import java.util.function.Supplier;

import static org.apache.commons.lang3.StringUtils.isBlank;

public class AssertionUtils {

	private AssertionUtils() {
	}


	public static void shouldBeEquals(Object a, Object b) {
		shouldBeEquals(a, b, "Objects should be equal!");
	}

	public static void shouldBeEquals(Object a, Object b, String messageOrFormat, Object... args) {
		if (!ObjectUtils.areEqual(a, b)) {
			throw new HorizonRuntimeException(messageOrFormat, args);
		}
	}

	public static void shouldBeEquals(Object a, Object b, Supplier<? extends RuntimeException> ex) {
		if (!ObjectUtils.areEqual(a, b)) {
			throw ex.get();
		}
	}

	public static void shouldBeFalse(boolean condition) {
		shouldBeFalse(condition, "Condition or expression should be false!");
	}


	public static void shouldBeFalse(boolean condition, String messageOrFormat, Object... args) {
		if (condition) {
			throw new HorizonRuntimeException(messageOrFormat, args);
		}
	}

	public static void shouldBeFalse(boolean condition, Supplier<? extends RuntimeException> ex) {
		if (condition) {
			throw ex.get();
		}
	}


	public static <T extends Comparable<T>> T shouldBeGreater(T obj, T comparison) {
		return shouldBeGreater(obj, comparison, "Object should not null and must be greater than the comparison");
	}


	public static <T extends Comparable<T>> T shouldBeGreater(T obj, T comparison, String messageOrFormat, Object... args) {
		if (obj != null && comparison != null && obj.compareTo(comparison) > 0) {
			return obj;
		}
		throw new HorizonRuntimeException(messageOrFormat, args);
	}


	public static <T extends Comparable<T>> T shouldBeLess(T obj, T comparison) {
		return shouldBeLess(obj, comparison, "Object should not null and must be less than the comparison");
	}


	public static <T extends Comparable<T>> T shouldBeLess(T obj, T comparison, String messageOrFormat, Object... args) {
		if (obj != null && comparison != null && obj.compareTo(comparison) < 0) {
			return obj;
		}
		throw new HorizonRuntimeException(messageOrFormat, args);
	}

	public static void shouldBeNull(Object obj) {
		shouldBeNull(obj, "Object should be null!");
	}

	public static void shouldBeNull(Object obj, String messageOrFormat, Object... args) {
		if (obj != null) {
			throw new HorizonRuntimeException(messageOrFormat, args);
		}
	}


	public static void shouldBeNull(Object obj, Supplier<? extends RuntimeException> ex) {
		if (obj != null) {
			throw ex.get();
		}
	}


	public static void shouldBeTrue(boolean condition) {
		shouldBeTrue(condition, "Condition or expression should be true!");
	}


	public static void shouldBeTrue(boolean condition, String messageOrFormat, Object... args) {
		if (!condition) {
			throw new HorizonRuntimeException(messageOrFormat, args);
		}
	}


	public static void shouldBeTrue(boolean condition, Supplier<? extends RuntimeException> ex) {
		if (!condition) {
			throw ex.get();
		}
	}

	@SuppressWarnings("unchecked")
	public static <T> T shouldInstanceOf(Object obj, Class<T> clazz) {
		shouldBeTrue(clazz.isInstance(obj), "The object must instanceof %s", clazz);
		return (T) obj;
	}


	@SuppressWarnings("unchecked")
	public static <T> T shouldInstanceOf(Object obj, Class<T> clazz, String messageOrFormat, Object... args) {
		shouldBeTrue(clazz.isInstance(obj), messageOrFormat, args);
		return (T) obj;
	}


	public static void shouldNoneNull(Object... args) {
		for (Object obj : args) {
			shouldNotNull(obj);
		}
	}


	public static <T extends Comparable<T>> T shouldNotBeGreater(T obj, T comparison) {
		return shouldNotBeGreater(obj,
		                          comparison,
		                          "Object should not null and must not greater than the comparad object");
	}


	public static <T extends Comparable<T>> T shouldNotBeGreater(T obj, T comparison, String messageOrFormat, Object... args) {
		if (obj != null && comparison != null && obj.compareTo(comparison) <= 0) {
			return obj;
		}
		throw new HorizonRuntimeException(messageOrFormat, args);
	}

	public static <T extends Comparable<T>> T shouldNotBeLess(T obj, T comparison) {
		return shouldNotBeLess(obj, comparison, "Object should not null and must not less than the comparad object");
	}


	public static <T extends Comparable<T>> T shouldNotBeLess(T obj, T comparison, String messageOrFormat, Object... args) {
		if (obj != null && comparison != null && obj.compareTo(comparison) >= 0) {
			return obj;
		}
		throw new HorizonRuntimeException(messageOrFormat, args);
	}


	public static <T extends CharSequence> T shouldNotBlank(T obj) {
		return shouldNotBlank(obj, "Object should not blank!");
	}


	public static <T extends CharSequence> T shouldNotBlank(T obj, String messageOrFormat, Object... args) {
		if (isBlank(obj)) {
			throw new HorizonRuntimeException(messageOrFormat, args);
		}
		return obj;
	}


	public static <T extends CharSequence> T shouldNotBlank(T obj, Supplier<? extends RuntimeException> ex) {
		if (isBlank(obj)) {
			throw ex.get();
		}
		return obj;
	}


	public static <T> T shouldNotEmpty(T obj) {
		return shouldNotEmpty(obj, "Object should not empty!");
	}


	public static <T> T shouldNotEmpty(T obj, String messageOrFormat, Object... args) {
		if (EmptyUtils.isEmpty(obj)) {
			throw new HorizonRuntimeException(messageOrFormat, args);
		}
		return obj;
	}


	public static <T> T shouldNotEmpty(T obj, Supplier<? extends RuntimeException> ex) {
		if (EmptyUtils.isEmpty(obj)) {
			throw ex.get();
		}
		return obj;
	}

	public static void shouldNotEquals(Object obj, Object other) {
		if (ObjectUtils.areEqual(obj, other)) {
			throw new HorizonRuntimeException("Objects can't equal");
		}
	}


	public static void shouldNotEquals(Object a, Object b, String messageOrFormat, Object... args) {
		if (ObjectUtils.areEqual(a, b)) {
			throw new HorizonRuntimeException(messageOrFormat, args);
		}
	}


	public static void shouldNotEquals(Object a, Object b, Supplier<? extends RuntimeException> ex) {
		if (ObjectUtils.areEqual(a, b)) {
			throw ex.get();
		}
	}


	public static <T> T shouldNotNull(T obj) {
		return shouldNotNull(obj, "Object should not null!");
	}


	public static <T> T shouldNotNull(T obj, String messageOrFormat, Object... args) {
		if (obj == null) {
			throw new HorizonRuntimeException(messageOrFormat, args);
		}
		return obj;
	}


	public static <T> T shouldNotNull(T obj, Supplier<? extends RuntimeException> ex) {
		if (obj == null) {
			throw ex.get();
		}
		return obj;
	}
}
