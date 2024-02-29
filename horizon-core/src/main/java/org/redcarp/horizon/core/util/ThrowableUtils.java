package org.redcarp.horizon.core.util;


import org.redcarp.horizon.core.exception.HorizonRuntimeException;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;


public class ThrowableUtils {

	private ThrowableUtils() {
	}

	public static <S> Attempt<S> attempt(Supplier<S> supplier) {
		return new Attempt<>(supplier);
	}

	public static Stream<Throwable> causes(final Throwable throwable) {
		return StreamUtils.streamOf(() -> new Iterator<Throwable>() {
			Throwable cause = throwable;

			@Override
			public boolean hasNext() {
				return cause != null && cause.getCause() != null && cause != cause.getCause();
			}

			@Override
			public Throwable next() {
				if (cause == null || (cause = cause.getCause()) == null) {
					throw new NoSuchElementException();
				}
				return cause;
			}
		});
	}

	public static void rethrow(Throwable t) {
		if (t instanceof RuntimeException) {
			throw (RuntimeException) t;
		}
		if (t instanceof Error) {
			throw (Error) t;
		}
		throw new HorizonRuntimeException(t);
	}

	public static RuntimeException asUncheckedException(Exception e) {
		return e instanceof RuntimeException ? (RuntimeException) e : new HorizonRuntimeException(e);
	}

	public static Throwable rootCause(final Throwable throwable) {
		return throwable != null && throwable.getCause() != null && throwable != throwable.getCause() ? rootCause(
				throwable.getCause()) : throwable;
	}

	public static String stackTraceAsString(Throwable throwable) {
		StringWriter stringWriter = new StringWriter();
		throwable.printStackTrace(new PrintWriter(stringWriter));
		return stringWriter.toString();
	}

	public static Stream<Throwable> suppresses(final Throwable throwable) {
		return throwable == null || EmptyUtils.isEmpty(throwable.getSuppressed()) ? Stream.empty() : StreamUtils.streamOf(
				throwable.getSuppressed());
	}

	public static class Attempt<S> {

		final Supplier<S> supplier;
		final LinkedList<Case<S>> cases = new LinkedList<>();

		private Attempt(Supplier<S> supplier) {
			this.supplier = AssertionUtils.shouldNotNull(supplier);
		}

		public S attempt() {
			try {
				return supplier.get();
			} catch (Exception e) {
				causes(e).forEach(ce -> {
					for (Case<S> cas : cases) {
						if (cas.ifThrow != null && cas.ifThrow.test(ce) && cas.rethrow != null) {
							throw cas.rethrow.apply(e, ce);
						}
					}
				});
				throw asUncheckedException(e);
			} finally {
				cases.clear();
			}
		}

		public Case<S> ifThrow(final Class<? extends Throwable> causeClass) {
			return ifThrow(causeClass::isInstance);
		}

		public Case<S> ifThrow(final Predicate<Throwable> p) {
			Case<S> cas = new Case<>(this);
			cas.ifThrow = p;
			cases.add(cas);
			return cas;
		}
	}

	public static class Case<S> {
		final Attempt<S> attempt;
		Predicate<Throwable> ifThrow;
		BiFunction<Throwable, Throwable, RuntimeException> rethrow;

		private Case(Attempt<S> attempt) {
			this.attempt = attempt;
		}

		public Attempt<S> rethrow(final BiFunction<Throwable, Throwable, RuntimeException> rethrow) {
			this.rethrow = AssertionUtils.shouldNotNull(rethrow);
			return attempt;
		}

		public Attempt<S> rethrow(final Function<Throwable, RuntimeException> rethrow) {
			return rethrow((rc, c) -> AssertionUtils.shouldNotNull(rethrow).apply(c));
		}

		public Attempt<S> rethrow(final RuntimeException rethrow) {
			return rethrow((rc, c) -> AssertionUtils.shouldNotNull(rethrow));
		}

		public Attempt<S> rethrow(final Supplier<RuntimeException> rethrow) {
			return rethrow((rc, c) -> AssertionUtils.shouldNotNull(rethrow).get());
		}
	}
}