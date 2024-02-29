package org.redcarp.horizon.core.util;

import cn.hutool.core.collection.CollUtil;

import java.io.*;
import java.util.*;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static org.redcarp.horizon.core.util.AssertionUtils.shouldNotNull;
import static org.redcarp.horizon.core.util.EmptyUtils.isNotEmpty;
import static org.redcarp.horizon.core.util.EmptyUtils.sizeOf;


public class StreamUtils {

	public static final int DFLT_BATCH_SIZE = 64;

	private StreamUtils() {
	}


	/**
	 * Receive iterable object converts it to a list stream, use for batch processing.
	 *
	 * <p>
	 * Note: Parallel not support, and if the current thread is in an interrupted state, the stream
	 * will be broken and finally exit.
	 *
	 * @param <T>       the element type
	 * @param batchSize the batch size
	 * @param source    the source
	 * @return the list stream
	 * @see #batchStream(int, Iterator)
	 */
	public static <T> Stream<List<T>> batchStream(int batchSize, Iterable<? extends T> source) {
		return batchStream(batchSize, shouldNotNull(source).iterator());
	}

	/**
	 * Receive iterator object converts it to a list stream, use for batch processing.
	 *
	 * <p>
	 * Note: Parallel not support, and if the current thread is in an interrupted state, the stream
	 * will be broken and finally exit.
	 *
	 * @param <T>       the element type
	 * @param batchSize the batch size
	 * @param it        the source
	 * @return the list stream
	 */
	public static <T> Stream<List<T>> batchStream(int batchSize, Iterator<? extends T> it) {

		return streamOf(new Iterator<List<T>>() {

			final int useBatchSize = batchSize < 1 ? DFLT_BATCH_SIZE : batchSize;
			final Iterator<? extends T> useIt = shouldNotNull(it);
			final List<T> buffer = new ArrayList<>(useBatchSize);
			boolean end = false;

			@Override
			public boolean hasNext() {
				if (end || Thread.currentThread().isInterrupted()) {
					return false;
				}
				if (isNotEmpty(buffer)) {
					return true;
				}
				int i = 0;
				while (useIt.hasNext()) {
					buffer.add(useIt.next());
					if (++i == useBatchSize) {
						break;
					}
				}
				end = i != useBatchSize;
				return i > 0;
			}

			@Override
			public List<T> next() {
				if (isNotEmpty(buffer) || hasNext()) {
					List<T> list = new ArrayList<>(buffer);
					buffer.clear();
					return list;
				} else {
					throw new NoSuchElementException();
				}
			}
		});
	}

	/**
	 * Receive stream object converts it to a list stream, use for batch processing.
	 *
	 * <p>
	 * Note: Parallel not support, and if the current thread is in an interrupted state, the stream
	 * will be broken and finally exit.
	 *
	 * @param <T>       the element type
	 * @param batchSize the batch size
	 * @param source    the source
	 * @return the list stream
	 * @see #batchStream(int, Iterator)
	 */
	public static <T> Stream<List<T>> batchStream(int batchSize, Stream<? extends T> source) {
		return batchStream(batchSize, shouldNotNull(source).iterator());
	}

	/**
	 * {@link #concat(List)}
	 *
	 * @param inputStreams the input streams.
	 * @return {@link SequenceInputStream}
	 */
	public static InputStream concat(InputStream... inputStreams) {
		return concat(CollUtil.toList(inputStreams));
	}

	/**
	 * Initializes a newly created <code>SequenceInputStream</code> by remembering the argument, which
	 * must be an <code>List</code> that produces objects whose run-time type is
	 * <code>InputStream</code>. The input streams that are produced by the iterator of given
	 * inputStreams will be read, in order, to provide the bytes to be read from this
	 * <code>SequenceInputStream</code>. After each input stream from the the iterator of given
	 * inputStreams is exhausted, it is closed by calling its <code>close</code> method.
	 * <p>
	 * In some scenarios, such as wanting to know the number of bytes of each input stream, the user
	 * can wrap each input stream into <code>CountingStream.CountingInputStream</code> and after the
	 * returns <code>InputStream</code> is read, the length of each specific input stream can be
	 * obtained.
	 *
	 * @param inputStreams a list of input streams.
	 * @return {@link SequenceInputStream}
	 */
	public static InputStream concat(List<InputStream> inputStreams) {
		int size = sizeOf(inputStreams);
		if (size == 0) {
			return null;
		} else if (size == 1) {
			return inputStreams.get(0);
		} else {
			return new SequenceInputStream(new Enumeration<InputStream>() {
				final Iterator<InputStream> iterator = inputStreams.iterator();

				@Override
				public boolean hasMoreElements() {
					return iterator.hasNext();
				}

				@Override
				public InputStream nextElement() {
					return iterator.next();
				}

			});
		}
	}


	/**
	 * Copy the given input stream to the given output stream without closing the streams.
	 *
	 * <p>
	 * Note: If the current thread is in an interrupted state, the copy will be stopped and finally
	 * exit.
	 *
	 * @param input      the input stream
	 * @param output     the output stream
	 * @param bufferSize the buffer size
	 * @return the bytes length
	 * @throws IOException If I/O errors occur
	 */
	public static long copy(InputStream input, OutputStream output, int bufferSize) throws IOException {
		byte[] buffer = new byte[ObjectUtils.max(1, bufferSize)];
		long count;
		int n;
		for (count = 0L; -1 != (n = input.read(buffer)) && !Thread.currentThread().isInterrupted(); count += n) {
			output.write(buffer, 0, n);
		}
		return count;
	}


	/**
	 * Copy character streams from the given reader to the given writer without closing them.
	 *
	 * <p>
	 * Note: If the current thread is in an interrupted state, the copy will be stopped and finally
	 * exit.
	 *
	 * @param reader     the character streams reader
	 * @param writer     the character streams writer
	 * @param bufferSize the buffer size
	 * @return the char length
	 * @throws IOException If I/O errors occur
	 */
	public static long copy(Reader reader, Writer writer, int bufferSize) throws IOException {
		char[] buffer = new char[ObjectUtils.max(1, bufferSize)];
		long count = 0;
		int n;
		while ((n = reader.read(buffer)) != -1 && !Thread.currentThread().isInterrupted()) {
			writer.write(buffer, 0, n);
			count += n;
		}
		return count;
	}

	/**
	 * Convert an Iterable object to parallel Stream object
	 */
	@SuppressWarnings("unchecked")
	public static <T> Stream<T> parallelStreamOf(final Iterable<? extends T> iterable) {
		if (iterable != null) {
			return StreamSupport.stream((Spliterator<T>) iterable.spliterator(), true);
		}
		return Stream.empty();
	}

	/**
	 * Convert Iterator object to parallel Stream object
	 */
	public static <T> Stream<T> parallelStreamOf(final Iterator<? extends T> iterator) {
		if (iterator != null) {
			return StreamSupport.stream(Spliterators.spliteratorUnknownSize(iterator, Spliterator.ORDERED), true);
		}
		return Stream.empty();
	}


	/**
	 * Read the input stream to byte array without closing the input stream.
	 *
	 * @param is         the input stream for reading
	 * @param bufferSize the read buffer size
	 * @return the bytes
	 * @throws IOException If I/O errors occur
	 */
	public static byte[] readAllBytes(InputStream is, int bufferSize) throws IOException {
		byte[] buf = new byte[ObjectUtils.max(1, bufferSize)];
		int capacity = buf.length;
		int nread = 0;
		int n;
		for (; ; ) {
			while ((n = is.read(buf, nread, capacity - nread)) > 0 && !Thread.currentThread().isInterrupted()) {
				nread += n;
			}
			// returned -1, done
			if (n < 0) {
				break;
			}
			if (capacity <= Integer.MAX_VALUE - capacity) {
				capacity = capacity << 1;
			} else {
				if (capacity == Integer.MAX_VALUE) {
					throw new OutOfMemoryError("Required array size too large");
				}
				capacity = Integer.MAX_VALUE;
			}
			buf = Arrays.copyOf(buf, capacity);
		}
		return capacity == nread ? buf : Arrays.copyOf(buf, nread);
	}

	/**
	 * Convert Enumeration object to Stream object
	 *
	 * @param <T>         the element type
	 * @param enumeration the source
	 * @return the enumeration object stream
	 */
	public static <T> Stream<T> streamOf(final Enumeration<? extends T> enumeration) {
		if (enumeration != null) {
			return streamOf(new Iterator<T>() {
				@Override
				public boolean hasNext() {
					return enumeration.hasMoreElements();
				}

				@Override
				public T next() {
					if (enumeration.hasMoreElements()) {
						return enumeration.nextElement();
					}
					throw new NoSuchElementException();
				}
			});
		}
		return Stream.empty();
	}

	/**
	 * Convert Iterable object to Stream object
	 *
	 * @param <T>      the element type
	 * @param iterable the source
	 * @return the object stream
	 */
	@SuppressWarnings({"unchecked", "rawtypes"})
	public static <T> Stream<T> streamOf(final Iterable<? extends T> iterable) {
		if (iterable instanceof Collection) {
			return ((Collection) iterable).stream();
		} else if (iterable != null) {
			return StreamSupport.stream((Spliterator<T>) iterable.spliterator(), false);
		}
		return Stream.empty();
	}

	/**
	 * Convert Iterator object to Stream object
	 *
	 * @param <T>      the element type
	 * @param iterator the source
	 * @return the object stream
	 */
	public static <T> Stream<T> streamOf(final Iterator<? extends T> iterator) {
		if (iterator != null) {
			return StreamSupport.stream(Spliterators.spliteratorUnknownSize(iterator, Spliterator.ORDERED), false);
		}
		return Stream.empty();
	}

	/**
	 * Convert the entries of the given map to Stream or {@link Stream#empty()} if given map is null.
	 *
	 * @param <K> the type of entry key
	 * @param <V> the type of entry value
	 * @param map the source
	 * @return then entries stream
	 */
	public static <K, V> Stream<Map.Entry<K, V>> streamOf(Map<K, V> map) {
		if (map != null) {
			return map.entrySet().stream();
		}
		return Stream.empty();
	}

	/**
	 * Convert object array to Stream
	 *
	 * @param <T>     the element type
	 * @param objects the source
	 * @return the object stream
	 */
	@SuppressWarnings("unchecked")
	public static <T> Stream<T> streamOf(T... objects) {
		return Arrays.stream(objects);
	}

}
