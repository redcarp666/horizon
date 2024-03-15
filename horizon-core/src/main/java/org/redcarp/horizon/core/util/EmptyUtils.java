package org.redcarp.horizon.core.util;

import cn.hutool.core.lang.Tuple;

import java.lang.reflect.Array;
import java.util.*;

public class EmptyUtils {

	private EmptyUtils() {
	}

	public static boolean isEmpty(final CharSequence object) {
		return object == null || object.length() == 0;
	}


	public static boolean isEmpty(final Collection<?> object) {
		return object == null || object.isEmpty();
	}


	public static boolean isEmpty(final Enumeration<?> object) {
		return object == null || !object.hasMoreElements();
	}


	public static boolean isEmpty(final Iterable<?> object) {
		return object == null || !object.iterator().hasNext();
	}


	public static boolean isEmpty(final Iterator<?> object) {
		return object == null || !object.hasNext();
	}


	public static boolean isEmpty(final Map<?, ?> object) {
		return object == null || object.isEmpty();
	}

	public static boolean isEmpty(final Object object) {
		if (object == null) {
			return true;
		} else if (object instanceof Collection<?>) {
			return isEmpty((Collection<?>) object);
		} else if (object instanceof Map<?, ?>) {
			return isEmpty((Map<?, ?>) object);
		} else if (object instanceof Object[]) {
			return isEmpty((Object[]) object);
		} else if (object instanceof CharSequence) {
			return isEmpty((CharSequence) object);
		} else if (object instanceof Iterator<?>) {
			return isEmpty((Iterator<?>) object);
		} else if (object instanceof Iterable<?>) {
			return isEmpty((Iterable<?>) object);
		} else if (object instanceof Enumeration<?>) {
			return isEmpty((Enumeration<?>) object);
		} else {
			if (object.getClass().isArray()) {
				return Array.getLength(object) == 0;
			}
			throw new IllegalArgumentException("Unsupported object type: " + object.getClass().getName());
		}
	}


	public static boolean isEmpty(final Object[] object) {
		return object == null || object.length == 0;
	}


	/**
	 * <pre>
	 * Empties.isEmpty(null)      = true
	 * Empties.isEmpty("")        = true
	 * Empties.isEmpty(" ")       = false
	 * Empties.isEmpty("abc")     = false
	 * Empties.isEmpty("  abc  ") = false
	 * </pre>
	 */
	public static boolean isEmpty(final String object) {
		return object == null || object.isEmpty();
	}

	/**
	 * <pre>
	 * Empties.isNotEmpty(null)      = false
	 * Empties.isNotEmpty("")        = false
	 * Empties.isNotEmpty(" ")       = true
	 * Empties.isNotEmpty("abc")     = true
	 * Empties.isNotEmpty("  abc  ") = true
	 * </pre>
	 *
	 * @param object the char sequence (string) object to check
	 * @return isNotEmpty
	 */
	public static boolean isNotEmpty(final CharSequence object) {
		return !isEmpty(object);
	}

	public static boolean isNotEmpty(final Collection<?> object) {
		return !isEmpty(object);
	}


	public static boolean isNotEmpty(final Enumeration<?> object) {
		return !isEmpty(object);
	}


	public static boolean isNotEmpty(final Iterable<?> object) {
		return !isEmpty(object);
	}


	public static boolean isNotEmpty(final Iterator<?> object) {
		return !isEmpty(object);
	}


	public static boolean isNotEmpty(final Map<?, ?> object) {
		return !isEmpty(object);
	}

	public static boolean isNotEmpty(final Object object) {
		return !isEmpty(object);
	}


	public static boolean isNotEmpty(final Object[] object) {
		return !isEmpty(object);
	}

	public static boolean isNotEmpty(final Optional<?> object) {
		return !isEmpty(object);
	}


	public static boolean isNotEmpty(final Tuple object) {
		return !isEmpty(object);
	}

	public static int sizeOf(final boolean[] object) {
		return object == null ? 0 : object.length;
	}


	public static int sizeOf(final byte[] object) {
		return object == null ? 0 : object.length;
	}


	public static int sizeOf(final char[] object) {
		return object == null ? 0 : object.length;
	}


	public static int sizeOf(final CharSequence object) {
		return isEmpty(object) ? 0 : object.length();
	}


	public static int sizeOf(final Collection<?> object) {
		return isEmpty(object) ? 0 : object.size();
	}

	public static int sizeOf(final double[] object) {
		return object == null ? 0 : object.length;
	}


	public static int sizeOf(final Enumeration<?> enums) {
		int size = 0;
		while (enums.hasMoreElements()) {
			size++;
			enums.nextElement();
		}
		return size;
	}


	public static int sizeOf(final float[] object) {
		return object == null ? 0 : object.length;
	}


	public static int sizeOf(final int[] object) {
		return object == null ? 0 : object.length;
	}


	public static int sizeOf(final long[] object) {
		return object == null ? 0 : object.length;
	}


	public static int sizeOf(final Map<?, ?> object) {
		return isEmpty(object) ? 0 : object.size();
	}


	public static int sizeOf(final Object[] object) {
		return isEmpty(object) ? 0 : object.length;
	}


	public static int sizeOf(final short[] object) {
		return object == null ? 0 : object.length;
	}
}
