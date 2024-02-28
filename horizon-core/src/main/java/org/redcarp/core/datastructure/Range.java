package org.redcarp.core.datastructure;


import cn.hutool.core.lang.Pair;
import org.redcarp.core.util.AssertionUtils;
import org.redcarp.core.util.ObjectUtils;

import static org.redcarp.core.util.AssertionUtils.shouldBeTrue;
import static org.redcarp.core.util.ObjectUtils.areEqual;
import static org.redcarp.core.util.ObjectUtils.compare;

/**
 * @author redcarp
 * @date 2024/2/5
 */
public class Range<T extends Comparable<T>> {
	@SuppressWarnings({"unchecked", "rawtypes"})
	static final Range emptyInstance = new Range(null, null);

	protected final T min;
	protected final T max;

	protected Range(T min, T max) {
		AssertionUtils.shouldBeTrue(ObjectUtils.compare(min, max) <= 0, IllegalArgumentException::new);
		this.min = min;
		this.max = max;
	}

	@SuppressWarnings("unchecked")
	public static <T extends Comparable<T>> Range<T> empty() {
		return emptyInstance;
	}

	public static <T extends Comparable<T>> Range<T> of(T min, T max) {
		return new Range<>(min, max);
	}

	public String asString(final String format) {
		return String.format(format, min, max);
	}

	public boolean coincide(Range<T> other) {
		if (other == null) {
			return false;
		} else if (this.equals(other)) {
			return true;
		} else {
			return ObjectUtils.compare(min, other.min) == 0 && ObjectUtils.compare(max, other.max) == 0;
		}
	}

	public boolean contains(Object o) {
		return ObjectUtils.areEqual(o, min) || ObjectUtils.areEqual(o, max);
	}

	public boolean cover(Range<T> other) {
		return lae(min, other.min) && gae(max, other.max);
	}

	public boolean cover(T value) {
		return lae(min, value) && gae(max, value);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if ((obj == null) || (getClass() != obj.getClass())) {
			return false;
		}
		Range other = (Range) obj;
		if (max == null) {
			if (other.max != null) {
				return false;
			}
		} else if (!max.equals(other.max)) {
			return false;
		}
		if (min == null) {
			return other.min == null;
		} else {
			return min.equals(other.min);
		}
	}

	public T getMax() {
		return max;
	}

	public T getMin() {
		return min;
	}

	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (max == null ? 0 : max.hashCode());
		return prime * result + (min == null ? 0 : min.hashCode());
	}

	public boolean intersect(Range<T> other) {
		return gae(min, other.min) && lae(min, other.max) || lae(min, other.min) && gae(max, other.max) || gae(max,
		                                                                                                       other.min) && lae(
				max,
				other.max);
	}

	public boolean isEmpty() {
		return min == null && max == null;
	}

	public T max() {
		return max;
	}

	public T min() {
		return min;
	}

	public Object[] toArray() {
		return new Object[]{min, max};
	}

	public Pair<T, T> toPair() {
		return Pair.of(min, max);
	}

	public String toString() {
		return asString("[%s,%s]");
	}

	public Range<T> withMax(T max) {
		return of(min, max);
	}

	public Range<T> withMin(T min) {
		return of(min, max);
	}

	private boolean gae(T d1, T d2) {
		return ObjectUtils.compare(d1, d2) >= 0;
	}

	private boolean lae(T d1, T d2) {
		return ObjectUtils.compare(d1, d2) <= 0;
	}

}
