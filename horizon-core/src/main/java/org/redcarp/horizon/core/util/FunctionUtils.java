package org.redcarp.horizon.core.util;

import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.function.*;

import static org.redcarp.horizon.core.util.AssertionUtils.shouldNotNull;


public class FunctionUtils {

	@SuppressWarnings("rawtypes")
	public static final Consumer EMPTY_CONSUMER = o -> {
	};

	@SuppressWarnings("rawtypes")
	public static final BiConsumer EMPTY_BICONSUMER = (a, b) -> {
	};

	@SuppressWarnings("rawtypes")
	public static final Supplier EMPTY_SUPPLIER = () -> null;

	@SuppressWarnings("rawtypes")
	public static final Function EMPTY_FUNCTION = p -> null;

	@SuppressWarnings("rawtypes")
	public static final Predicate EMPTY_PREDICATE_TRUE = p -> true;

	@SuppressWarnings("rawtypes")
	public static final Predicate EMPTY_PREDICATE_FALSE = p -> false;

	@SuppressWarnings("rawtypes")
	public static final BiPredicate EMPTY_BIPREDICATE_TRUE = (a, b) -> true;

	@SuppressWarnings("rawtypes")
	public static final BiPredicate EMPTY_BIPREDICATE_FALSE = (a, b) -> false;

	public static <T> Callable<T> asCallable(Runnable runnable) {
		return () -> {
			AssertionUtils.shouldNotNull(runnable).run();
			return null;
		};
	}

	public static <T> Callable<T> asCallable(Supplier<T> supplier) {
		return supplier::get;
	}

	public static <T> Supplier<T> asSupplier(Callable<T> callable) {
		return () -> {
			try {
				return callable.call();
			} catch (Exception e) {
				throw ThrowableUtils.asUncheckedException(e);
			}
		};
	}

	public static <T> Supplier<T> asSupplier(Runnable runnable) {
		return () -> {
			AssertionUtils.shouldNotNull(runnable).run();
			return null;
		};
	}

	public static <T, U> BiPredicate<T, U> defaultBiPredicate(BiPredicate<T, U> predicate, boolean always) {
		return predicate != null ? predicate : emptyBiPredicate(always);
	}

	public static <T> Predicate<T> defaultPredicate(Predicate<T> predicate, boolean always) {
		return predicate != null ? predicate : emptyPredicate(always);
	}

	@SuppressWarnings("unchecked")
	public static <A, B> BiConsumer<A, B> emptyBiConsumer() {
		return EMPTY_BICONSUMER;
	}

	@SuppressWarnings("unchecked")
	public static <T, U> BiPredicate<T, U> emptyBiPredicate(boolean bool) {
		return bool ? EMPTY_BIPREDICATE_TRUE : EMPTY_BIPREDICATE_FALSE;
	}

	@SuppressWarnings("unchecked")
	public static <T> Consumer<T> emptyConsumer() {
		return EMPTY_CONSUMER;
	}

	@SuppressWarnings("unchecked")
	public static <P, R> Function<P, R> emptyFunction() {
		return EMPTY_FUNCTION;
	}

	@SuppressWarnings("unchecked")
	public static <T> Predicate<T> emptyPredicate(boolean bool) {
		return bool ? EMPTY_PREDICATE_TRUE : EMPTY_PREDICATE_FALSE;
	}

	@SuppressWarnings("unchecked")
	public static <T> Supplier<T> emptySupplier() {
		return EMPTY_SUPPLIER;
	}

	public static <T> Optional<T> optional(T obj) {
		return Optional.ofNullable(obj);
	}

	public static <T> T trySupplied(Supplier<T> supplier) {
		T supplied = null;
		if (supplier != null) {
			try {
				supplied = supplier.get();
			} catch (Exception e) {
				// Noop! just try...
			}
		}
		return supplied;
	}

}