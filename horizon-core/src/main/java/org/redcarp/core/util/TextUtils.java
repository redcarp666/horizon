package org.redcarp.core.util;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.resource.Resource;
import cn.hutool.core.text.StrPool;
import cn.hutool.core.util.StrUtil;
import org.redcarp.core.exception.HorizonRuntimeException;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.Math.max;
import static java.nio.charset.StandardCharsets.UTF_8;


public class TextUtils {

	public static final char CSV_FIELD_DELIMITER = ',';
	public static final char CSV_FIELD_QUOTES = '"';
	public static final String CSV_FIELD_DELIMITER_STRING = ",";
	public static final String CSV_FIELD_QUOTES_STRING = "\"";
	public static final String CSV_DOUBLE_QUOTES = "\"\"";
	public static final String XSV_CR_REP = "\\r";
	public static final String XSV_LF_REP = "\\n";
	public static final byte CR = 0x0D;
	public static final byte LF = 0x0A;

	private TextUtils() {
	}

	/**
	 * CSV rows from file input stream, use for read CSV file line by line.
	 *
	 * @param file the CSV file
	 */
	public static Stream<List<String>> asCSVLines(final File file) {
		return asCSVLines(file, null, -1, -1);
	}

	/**
	 * CSV rows from file input stream, use for read CSV file line by line.
	 *
	 * @param file    the CSV file
	 * @param charset the CSV file charset
	 */
	public static Stream<List<String>> asCSVLines(final File file, final Charset charset) {
		return asCSVLines(file, charset, -1, -1);
	}

	/**
	 * CSV rows from file input stream, use for read CSV file line by line.
	 *
	 * @param file    the CSV file
	 * @param charset the CSV file charset
	 * @param offset  the offset start from 0
	 * @param limit   the number of rows returned streamCSVRows
	 */
	public static Stream<List<String>> asCSVLines(final File file, final Charset charset, final int offset, final int limit) {
		final FileInputStream fis;
		try {
			fis = new FileInputStream(AssertionUtils.shouldNotNull(file));
		} catch (FileNotFoundException e1) {
			throw new HorizonRuntimeException(e1);
		}
		return asCSVLines(fis,
		                  ObjectUtils.defaultObject(charset, UTF_8),
		                  offset,
		                  (i, t) -> limit >= 1 && i > limit).onClose(() -> {
			try {
				fis.close();
			} catch (IOException e) {
				throw new HorizonRuntimeException(e);
			}
		});
	}

	/**
	 * CSV rows from input stream, use for read CSV file line by line.
	 * <p>
	 * Note: The caller must maintain resource release by himself
	 * </p>
	 *
	 * @param is the CSV format input stream
	 * @return streamCSVRows
	 */
	public static Stream<List<String>> asCSVLines(final InputStream is) {
		return asCSVLines(is, UTF_8, 0, null);
	}

	/**
	 * CSV rows from input stream, use for read CSV file line by line.
	 * <p>
	 * Note: The caller must maintain resource release by himself
	 * </p>
	 *
	 * @param is         the CSV format input stream
	 * @param charset    the content charset
	 * @param offset     the offset start from 0
	 * @param terminator used to brake out the stream, terminator return true means need to brake out
	 */
	public static Stream<List<String>> asCSVLines(final InputStream is, final Charset charset, final int offset, final BiPredicate<Integer, String> terminator) {
		final BufferedReader reader = new CSVBufferedReader(new InputStreamReader(AssertionUtils.shouldNotNull(is),
		                                                                          ObjectUtils.defaultObject(charset,
		                                                                                                    UTF_8)));
		return lines(reader, offset, terminator, TextUtils::readCSVFields);
	}

	/**
	 * Convert string to byte array input stream.
	 *
	 * @param data the input stream data
	 * @return asInputStream
	 */
	public static InputStream asInputStream(final String data) {
		return asInputStream(data, UTF_8);
	}

	/**
	 * Convert string to byte array input stream with charset
	 *
	 * @param data    the input stream data
	 * @param charset the data charset
	 * @return asInputStream
	 */
	public static InputStream asInputStream(final String data, final Charset charset) {
		return new ByteArrayInputStream(AssertionUtils.shouldNotNull(data).getBytes(charset));
	}


	/**
	 * Reads a string from given input stream with 'UTF-8' charset.
	 * <p>
	 * Note: The caller must maintain resource release by himself
	 * </p>
	 *
	 * @param is the input stream to read into the string
	 * @throws IOException fromInputStream
	 */
	public static String fromInputStream(final InputStream is) throws IOException {
		return fromInputStream(is, UTF_8);
	}

	/**
	 * Reads a string from given input stream with given charset.
	 * <p>
	 * Note: The caller must maintain resource release by himself
	 * </p>
	 *
	 * @param is      the input stream
	 * @param charset the charset used by input stream reader
	 * @throws IOException fromInputStream
	 */
	public static String fromInputStream(final InputStream is, final Charset charset) throws IOException {
		StringBuilder sb = new StringBuilder();
		try (Reader reader = new BufferedReader(new InputStreamReader(AssertionUtils.shouldNotNull(is),
		                                                              ObjectUtils.defaultObject(charset, UTF_8)))) {
			int c;
			while ((c = reader.read()) != -1) {
				sb.append((char) c);
			}
		}
		return sb.toString();
	}

	/**
	 * Reads a string from given resource with given charset.
	 *
	 * @param resource the resource to be read
	 * @param charset  the charset used by input stream reader
	 * @throws IOException fromInputStream
	 */
	public static String fromResource(final Resource resource, final Charset charset) throws IOException {
		if (resource == null) {
			throw new IOException("The resource to be read can't null!");
		}
		try (InputStream is = resource.getStream()) {
			return fromInputStream(is, charset);
		}
	}

	public static <T> Stream<T> lines(final BufferedReader reader, final int offset, final BiPredicate<Integer, String> terminator, final Function<String, T> converter) {
		AssertionUtils.shouldNoneNull(reader, converter);
		return StreamUtils.streamOf(new Iterator<T>() {
			final BiPredicate<Integer, String> useTerminator = terminator == null ? (i, t) -> false : terminator;
			String nextLine = null;
			int readLines = 0;
			boolean valid = true;

			// skip lines if necessary
			{
				try {
					if (offset > 0) {
						for (int i = 0; i < offset; i++) {
							if ((nextLine = reader.readLine()) == null) {
								valid = false;
								break;
							}
						}
					}
				} catch (IOException e) {
					throw new HorizonRuntimeException(e, "Skip lines error!");
				}
			}

			@Override
			public boolean hasNext() {
				if (!valid || Thread.currentThread().isInterrupted()) {
					return false;
				}
				try {
					nextLine = reader.readLine();
					return nextLine != null && !useTerminator.test(++readLines, nextLine);
				} catch (IOException e) {
					throw new UncheckedIOException(e);
				}
			}

			@Override
			public T next() {
				if (valid && (nextLine != null || hasNext())) {
					String line = nextLine;
					nextLine = null;
					return converter.apply(line);
				} else {
					throw new NoSuchElementException();
				}
			}
		});
	}

	/**
	 * String lines from file, use for read text file line by line.
	 *
	 * @param file the text file
	 * @return lines
	 */
	public static Stream<String> lines(final File file) {
		return lines(file, -1, -1);
	}

	/**
	 * String lines from file, use for read text line by line.
	 *
	 * @param file       the file to read
	 * @param offset     used to skip lines, the offset start from 0
	 * @param terminator used to brake out the stream, terminator return true means need to brake out
	 * @return lines
	 */
	public static Stream<String> lines(final File file, int offset, final BiPredicate<Integer, String> terminator) {
		final FileInputStream fis;
		try {
			fis = new FileInputStream(AssertionUtils.shouldNotNull(file));
		} catch (FileNotFoundException e1) {
			throw new HorizonRuntimeException(e1);
		}
		return lines(fis, offset, terminator).onClose(() -> {
			try {
				fis.close();
			} catch (IOException e) {
				throw new HorizonRuntimeException(e);
			}
		});
	}

	/**
	 * String lines from file, use for read text line by line.
	 *
	 * @param file   the file to read
	 * @param offset used to skip lines, the offset start from 0
	 * @param limit  the number of lines returned
	 */
	public static Stream<String> lines(final File file, int offset, int limit) {
		final FileInputStream fis;
		try {
			fis = new FileInputStream(AssertionUtils.shouldNotNull(file));
		} catch (FileNotFoundException e1) {
			throw new HorizonRuntimeException(e1);
		}
		return lines(fis, offset, limit).onClose(() -> {
			try {
				fis.close();
			} catch (IOException e) {
				throw new HorizonRuntimeException(e);
			}
		});
	}

	/**
	 * String lines from input stream, use for read text file line by line.
	 * <p>
	 * Note: The caller must maintain resource release by himself
	 *
	 * @param is the text input stream
	 */
	public static Stream<String> lines(final InputStream is) {
		return lines(new InputStreamReader(AssertionUtils.shouldNotNull(is), UTF_8), 0, null);
	}

	/**
	 * String lines from input stream, use for read text line by line.
	 * <p>
	 * Note: The caller must maintain resource release by himself
	 *
	 * @param is         the text input stream
	 * @param charset    the charset
	 * @param offset     used to skip lines, the offset start from 0
	 * @param terminator used to brake out the stream, terminator return true means need to brake out
	 */
	public static Stream<String> lines(final InputStream is, final Charset charset, final int offset, final BiPredicate<Integer, String> terminator) {
		return lines(new InputStreamReader(AssertionUtils.shouldNotNull(is), ObjectUtils.defaultObject(charset, UTF_8)),
		             offset,
		             terminator);
	}

	/**
	 * String lines from input stream, use for read text file line by line.
	 * <p>
	 * Note: The caller must maintain resource release by himself
	 *
	 * @param is         the text input stream
	 * @param offset     used to skip lines, the offset start from 0
	 * @param terminator used to brake out the stream, terminator return true means need to brake out
	 */
	public static Stream<String> lines(final InputStream is, final int offset, final BiPredicate<Integer, String> terminator) {
		return lines(new InputStreamReader(AssertionUtils.shouldNotNull(is), UTF_8), offset, terminator);
	}

	/**
	 * String lines from input stream, use for read text file line by line.
	 * <p>
	 * Note: The caller must maintain resource release by himself
	 *
	 * @param is     the text file input stream
	 * @param offset the offset start from 0
	 * @param limit  the number of lines returned
	 */
	public static Stream<String> lines(final InputStream is, final int offset, final int limit) {
		return lines(new InputStreamReader(AssertionUtils.shouldNotNull(is), UTF_8),
		             offset,
		             (i, t) -> limit >= 1 && i > limit);
	}

	/**
	 * String lines from input stream reader, use for read text file line by line.
	 * <p>
	 * Note: The caller must maintain resource release by himself
	 *
	 * @param isr        the text input stream reader
	 * @param offset     the offset start from 0
	 * @param terminator used to brake out the stream, terminator return true means need to brake out
	 */
	public static Stream<String> lines(final InputStreamReader isr, final int offset, final BiPredicate<Integer, String> terminator) {
		final BufferedReader reader = new BufferedReader(AssertionUtils.shouldNotNull(isr));
		return lines(reader, offset, terminator, UnaryOperator.identity());
	}

	/**
	 * Sting lines from file path.
	 *
	 * @param filePath the text file path
	 * @return lines
	 */
	public static Stream<String> lines(final String filePath) {
		return lines(new File(filePath));
	}

	/**
	 * String lines from file, use for read text file line by line.
	 *
	 * @param file the text file
	 * @return a LocableFileLine object stream which contain line data and line offset info.
	 * @see #locableLines(File, long, Charset, int, Predicate)
	 */
	public static Stream<LocableFileLine> locableLines(final File file) {
		return locableLines(file, 0, UTF_8, 0, t -> false);
	}

	/**
	 * String lines from file, use for read text file line by line.
	 *
	 * @param file      the text file
	 * @param skipBytes the offset position, measured in bytes from the beginning of the file, at
	 *                  which to set the file pointer.
	 * @return a LocableFileLine object stream which contain line data and line offset info.
	 * @see #locableLines(File, long, Charset, int, Predicate)
	 */
	public static Stream<LocableFileLine> locableLines(final File file, final long skipBytes) {
		return locableLines(file, skipBytes, UTF_8, 0, t -> false);
	}

	/**
	 * String lines from file, use for read text file line by line.
	 * <p>
	 * Note: The caller must maintain resource release by himself, the method is not thread-safe.
	 *
	 * @param file                the text file
	 * @param skipBytes           the offset position, measured in bytes from the beginning of the file, at
	 *                            which to set the file pointer.
	 * @param charset             the content charset
	 * @param lineBufferIncrement the line buffer increment, use to increase the line read buffer size
	 *                            and reduce byte array copy.
	 * @param terminator          used to brake out the stream, terminator return true means need to brake out
	 * @return a LocableFileLine object stream which contain line data and line offset info.
	 * @see LocableFileLine
	 * @see LocablelFileLineReader
	 * @see RandomAccessFile
	 */
	public static Stream<LocableFileLine> locableLines(final File file, final long skipBytes, final Charset charset, final int lineBufferIncrement, final Predicate<LocableFileLine> terminator) {
		final LocablelFileLineReader reader = new LocablelFileLineReader(file, charset, lineBufferIncrement);
		final Stream<LocableFileLine> stream = StreamUtils.streamOf(new Iterator<LocableFileLine>() {
			final Predicate<LocableFileLine> useTerminator = terminator == null ? l -> false : terminator;
			LocableFileLine nextLine = null;
			boolean valid = true;

			// skip read bytes if necessary
			{
				try {
					if (skipBytes > 0) {
						valid = reader.skipBytes(skipBytes) > 0;
					}
				} catch (IOException e) {
					throw new HorizonRuntimeException(e, "Skip bytes error!");
				}
			}

			@Override
			public boolean hasNext() {
				if (!valid || Thread.currentThread().isInterrupted()) {
					return false;
				}
				try {
					long bp = reader.readBytes();
					String content = reader.readLine();
					long ep = reader.readBytes();
					if (content != null) {
						nextLine = new LocableFileLine(bp, ep, content);
					} else {
						nextLine = null;
					}
					return nextLine != null && !useTerminator.test(nextLine);
				} catch (IOException e) {
					throw new UncheckedIOException(e);
				}
			}

			@Override
			public LocableFileLine next() {
				if (valid && (nextLine != null || hasNext())) {
					LocableFileLine line = nextLine;
					nextLine = null;
					return line;
				} else {
					throw new NoSuchElementException();
				}
			}
		});
		return stream.onClose(() -> {
			try {
				reader.close();
			} catch (IOException e) {
				throw new HorizonRuntimeException(e);
			}
		});
	}

	/**
	 * String lines from file, use for read text file line by line.
	 *
	 * @param file       the text file
	 * @param skipBytes  the offset position, measured in bytes from the beginning of the file, at
	 *                   which to set the file pointer.
	 * @param terminator used to brake out the stream, terminator return true means need to brake out
	 * @return a LocableFileLine object stream which contain line data and line offset info.
	 * @see #locableLines(File, long, Charset, int, Predicate)
	 */
	public static Stream<LocableFileLine> locableLines(final File file, final long skipBytes, final Predicate<LocableFileLine> terminator) {
		return locableLines(file, skipBytes, UTF_8, 0, terminator);
	}

	/**
	 * Parse CSV line to field list
	 * <p>
	 * NOTE: Some codes come from com.sun.tools.jdeprscan.CSV, if there is infringement, please inform
	 * me(finesoft@gmail.com).
	 *
	 * @param line the CSV format line
	 * @return the CSV fields
	 */
	public static List<String> readCSVFields(final String line) {
		List<String> result = new ArrayList<>();
		if (line != null) {
			StringBuilder buf = new StringBuilder();
			int len = line.length();
			byte state = 0; // 0:start,1:in field,2:in field quote,3:end field quote
			for (int i = 0; i < len; i++) {
				char c = line.charAt(i);
				switch (c) {
					case CSV_FIELD_DELIMITER:
						switch (state) {
							case 2:
								buf.append(CSV_FIELD_DELIMITER);
								break;
							default:
								result.add(buf.toString());
								buf.setLength(0);
								state = 0;
								break;
						}
						break;
					case CSV_FIELD_QUOTES:
						switch (state) {
							case 0:
								state = 2;
								break;
							case 2:
								state = 3;
								break;
							case 1:
								throw new IllegalArgumentException(String.format(
										"Unexpected csv quote, line: [%s] char at: [%d]",
										line,
										i));
							case 3:
								buf.append(CSV_FIELD_QUOTES);
								state = 2;
								break;
						}
						break;
					default:
						switch (state) {
							case 0:
								state = 1;
								break;
							case 1:
							case 2:
								break;
							case 3:
								throw new IllegalArgumentException(String.format(
										"Extra csv character after quoted string, line: [%s] char at: [%d]",
										line,
										i));
						}
						buf.append(c);
						break;
				}
			}
			if (state == 2) {
				throw new IllegalArgumentException(String.format("Unclosed csv quote, line: [%s] length: [%d]",
				                                                 line,
				                                                 line.length()));
			}
			result.add(buf.toString());
		}
		return result;
	}

	/**
	 * Return string lines from file path.
	 *
	 * @param path the file path
	 * @return readFromFile
	 */
	public static List<String> readLines(final String path) {
		return TextUtils.lines(new File(path)).collect(Collectors.toList());
	}

	/**
	 * Format objects to CSV line string.
	 *
	 * @param objects the objects which compose a CSV line
	 * @return toCSVLine
	 */
	public static String toCSVLine(final Iterable<?> objects) {
		String line = StreamUtils.streamOf(objects).map(o -> ObjectUtils.asString(o, StrUtil.EMPTY)).map(s -> {
			boolean q = s.contains(CSV_FIELD_DELIMITER_STRING) || s.contains(StrUtil.CR) || s.contains(StrUtil.LF);
			String r;
			if (s.contains(CSV_FIELD_QUOTES_STRING)) {
				q = true;
				r = s.replace(CSV_FIELD_QUOTES_STRING, CSV_DOUBLE_QUOTES);
			} else {
				r = s;
			}
			return q ? CSV_FIELD_QUOTES_STRING + r + CSV_FIELD_QUOTES_STRING : r;
		}).collect(Collectors.joining(CSV_FIELD_DELIMITER_STRING));
		return line.endsWith(String.valueOf(CSV_FIELD_DELIMITER)) ? line.substring(0, line.length() - 1) : line;
	}

	/**
	 * Format objects to CSV line string.
	 *
	 * @param objects the objects array which compose a CSV line
	 * @return toCSVLine
	 */
	public static String toCSVLine(Object... objects) {
		return toCSVLine(CollUtil.toList(objects));
	}

	/**
	 * Format objects to XSV line string.
	 *
	 * @param objects   the objects which compose a XSV line
	 * @param delimiter the field delimiter in XSV line
	 * @return toXSVLine
	 */
	public static String toXSVLine(final Iterable<?> objects, String delimiter) {
		final String re = StrUtil.BACKSLASH.concat(AssertionUtils.shouldNotEmpty(delimiter));
		return StreamUtils.streamOf(objects).map(o -> ObjectUtils.asString(o, StrUtil.EMPTY)).map(o -> StrUtil.replace(
				StrUtil.replace(StrUtil.replace(o, StrUtil.LF, XSV_LF_REP), StrUtil.CR, XSV_CR_REP),
				delimiter,
				re)).collect(Collectors.joining(delimiter));
	}

	/**
	 * Convert input stream to String
	 * <p>
	 * Note: The caller must maintain resource release by himself
	 *
	 * @param is the input stream
	 * @return tryFromInputStream
	 */
	public static String tryFromInputStream(final InputStream is) {
		try {
			return fromInputStream(is);
		} catch (IOException e) {
			return null;
		}
	}

	public static void tryWriteToFile(File file, boolean append, Charset charset, String data) {
		try (OutputStream os = new FileOutputStream(file,
		                                            append); BufferedWriter fileWriter = new BufferedWriter(new OutputStreamWriter(
				os,
				charset))) {
			fileWriter.append(data);
		} catch (IOException e) {
			// Noop!
		}
	}

	public static void tryWriteToFile(File file, boolean append, String data) {
		try (OutputStream os = new FileOutputStream(file,
		                                            append); BufferedWriter fileWriter = new BufferedWriter(new OutputStreamWriter(
				os,
				UTF_8))) {
			fileWriter.append(data);
		} catch (IOException e) {
			// Noop!
		}
	}

	public static void tryWriteToFile(File file, Iterable<?> data) {
		try {
			writeToFile(file, data);
		} catch (IOException e) {
			// Noop!
		}
	}

	public static void tryWriteToFile(File file, Stream<?> data) {
		try {
			writeToFile(file, false, data);
		} catch (IOException e) {
			// Noop!
		}
	}

	public static void writeCSVFile(File file, boolean append, Charset charset, List<List<String>> list) throws IOException {
		writeToFile(file,
		            append,
		            charset,
		            IterableUtils.transform(AssertionUtils.shouldNotNull(list).iterator(), TextUtils::toCSVLine));
	}

	public static void writeCSVFile(File file, boolean append, Charset charset, Stream<List<String>> stream) throws IOException {
		writeToFile(file, append, charset, AssertionUtils.shouldNotNull(stream).map(TextUtils::toCSVLine).iterator());
	}

	public static void writeCSVFile(File file, List<List<String>> data) throws IOException {
		writeToFile(file,
		            false,
		            null,
		            IterableUtils.transform(AssertionUtils.shouldNotNull(data).iterator(), TextUtils::toCSVLine));
	}

	public static void writeToFile(File file, boolean append, Charset charset, Iterator<?> lines) throws IOException {
		AssertionUtils.shouldNotNull(lines);
		if (!file.exists()) {
			AssertionUtils.shouldBeTrue(file.createNewFile());
		}
		try (OutputStream os = new FileOutputStream(file,
		                                            append); BufferedWriter fileWriter = new BufferedWriter(new OutputStreamWriter(
				os,
				ObjectUtils.defaultObject(charset, UTF_8)))) {
			while (lines.hasNext() && !Thread.currentThread().isInterrupted()) {
				Object line = lines.next();
				if (line == null) {
					fileWriter.append(StrUtil.EMPTY);
				} else {
					fileWriter.append(line.toString());
				}
				fileWriter.newLine();
				fileWriter.flush();
			}
		}
	}

	public static void writeToFile(File file, boolean append, Stream<?> lines) throws IOException {
		writeToFile(file, append, UTF_8, AssertionUtils.shouldNotNull(lines).iterator());
	}

	/**
	 * Write string to file line by line, string lines will be written to the beginning of the file.
	 *
	 * @param file the destination file which the data write to
	 * @param data the data to be written
	 * @throws IOException writeToFile
	 */
	public static void writeToFile(File file, Iterable<?> data) throws IOException {
		writeToFile(file, false, null, AssertionUtils.shouldNotNull(data).iterator());
	}


	public static void writeXSVFile(File file, boolean append, Charset charset, String delimiter, Stream<List<String>> stream) throws IOException {
		writeToFile(file,
		            append,
		            charset,
		            AssertionUtils.shouldNotNull(stream).map(s -> toXSVLine(s, delimiter)).iterator());
	}


	public static class LocableFileLine implements Serializable {
		private static final long serialVersionUID = -3379179413769504801L;
		final long beginPosition;
		final long endPosition;
		final String content;

		public LocableFileLine(long beginPosition, long endPosition, String content) {
			this.beginPosition = beginPosition;
			this.endPosition = endPosition;
			this.content = content;
		}

		/**
		 * Returns the offset from the beginning of the line in file, in bytes.
		 */
		public long getBeginPosition() {
			return beginPosition;
		}

		/**
		 * Returns the line
		 */
		public String getContent() {
			return content;
		}

		/**
		 * Returns the offset from the ending of the line in file, in bytes.
		 */
		public long getEndPosition() {
			return endPosition;
		}

		@Override
		public String toString() {
			return String.format("[%d-%d] %s", beginPosition, endPosition, content);
		}

	}


	static class CSVBufferedReader extends BufferedReader {

		public CSVBufferedReader(Reader in) {
			super(in);
		}

		@Override
		public String readLine() throws IOException {
			return readCSVLine();
		}

		String readCSVLine() throws IOException {
			StringBuilder result = new StringBuilder(128);
			boolean inquotes = false;
			for (; ; ) {
				int intRead = read();
				if (intRead == -1) {
					return result.length() == 0 ? null : result.toString();
				}
				char c = (char) intRead;
				if (c == CSV_FIELD_QUOTES) {
					inquotes = !inquotes;
				}
				if (!inquotes) {
					if (c == StrPool.C_LF) {
						break;
					} else if (c == StrPool.C_CR) {
						if (markSupported()) {
							mark(1);// for windows excel '\r\n' check if next is '\n' then skip it
							if ((char) read() != StrPool.C_LF) {
								reset();
							}
						}
						break;
					}
				}
				result.append(c);
			}
			return result.toString();
		}

	}


	static class LocablelFileLineReader implements Closeable {

		static final int THRESHOLD = 2 * 1024 * 1024;
		static final int DFLT_LINE_INC = 8192;
		static final int DFLT_CHUNK_FACTOR = 256;

		final RandomAccessFile file;
		final FileChannel channel;
		final int lineBufferIncrement;
		final int chuckBufferSize;
		final CharsetDecoder decoder;

		ByteBuffer chuckBuffer;
		byte[] lineBuffer = null;
		long lastChannelOffset;

		LocablelFileLineReader(File file, Charset charset, int lineBufferIncrement) {
			try {
				this.file = new RandomAccessFile(AssertionUtils.shouldNotNull(file), "r");
				this.lineBufferIncrement = max(lineBufferIncrement, DFLT_LINE_INC);
				chuckBufferSize = this.lineBufferIncrement * DFLT_CHUNK_FACTOR;
				channel = this.file.getChannel();
				lastChannelOffset = channel.position();
				decoder = ObjectUtils.defaultObject(charset, StandardCharsets.UTF_8).newDecoder();
			} catch (Exception e) {
				throw new HorizonRuntimeException(e);
			}
		}

		@Override
		public void close() throws IOException {
			if (chuckBuffer != null) {
				chuckBuffer.clear();
			}
			if (channel != null) {
				channel.close();
			}
			if (file != null) {
				file.close();
			}
		}

		byte[] growLineBuffer(byte[] useLineBuffer) {
			if (useLineBuffer.length < lineBufferIncrement) {
				return new byte[useLineBuffer.length * 2];
			} else {
				return new byte[useLineBuffer.length + lineBufferIncrement];
			}
		}

		byte read() throws IOException {
			if (chuckBuffer == null) {
				chuckBuffer = ByteBuffer.allocate(chuckBufferSize);
				lastChannelOffset = channel.position();
				if (channel.read(chuckBuffer) != -1) {
					chuckBuffer.flip();
					return chuckBuffer.get();
				}
				chuckBuffer.limit(0);
				return -1;
			} else if (chuckBuffer.hasRemaining()) {
				return chuckBuffer.get();
			} else {
				chuckBuffer.clear();
				lastChannelOffset = channel.position();
				if (channel.read(chuckBuffer) != -1) {
					chuckBuffer.flip();
					return chuckBuffer.get();
				}
				chuckBuffer.limit(0);
				return -1;
			}
		}

		long readBytes() {
			return lastChannelOffset + (chuckBuffer == null ? 0 : chuckBuffer.position());
		}

		String readLine() throws IOException {

			byte[] useLineBuffer = lineBuffer;
			if (useLineBuffer == null || useLineBuffer.length > THRESHOLD) {
				useLineBuffer = lineBuffer = new byte[256];
			}

			int c = -1, limit = useLineBuffer.length, offset = 0;
			boolean eol = false;

			while (!eol) {
				switch (c = read()) {
					case -1:
					case LF:
						eol = true;
						break;
					case CR:
						eol = true;
						long pos = readBytes();
						if ((read()) != LF) { // for windows '\r\n'
							skipBytes(pos);
						}
						break;
					default:
						if (--limit < 0) {
							// reach limit, need to grow up.
							useLineBuffer = growLineBuffer(useLineBuffer);
							limit = useLineBuffer.length - offset - 1;
							System.arraycopy(lineBuffer, 0, useLineBuffer, 0, offset);
							lineBuffer = useLineBuffer;
						}
						useLineBuffer[offset++] = (byte) c;
						break;
				}
			}

			if ((c == -1) && (offset == 0)) {
				return null;
			}

			return decoder.decode(ByteBuffer.wrap(useLineBuffer, 0, offset)).toString();
		}

		long skipBytes(long skipBytes) throws IOException {
			if (chuckBuffer != null) {
				chuckBuffer.limit(0);
			}
			long useSkipBytes = max(skipBytes, 0);
			if (useSkipBytes > channel.size()) {
				lastChannelOffset = channel.size();
				return -1;
			} else {
				channel.position(useSkipBytes);
				lastChannelOffset = useSkipBytes;
				return useSkipBytes;
			}
		}

	}
}