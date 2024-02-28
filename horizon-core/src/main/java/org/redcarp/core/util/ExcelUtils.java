package org.redcarp.core.util;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.apache.poi.ss.usermodel.*;
import org.redcarp.core.exception.HorizonRuntimeException;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class ExcelUtils {

	public static Object parseCellValue(Cell cell) {

		if (cell == null) {
			return null;
		}
		switch (cell.getCellType() == CellType.FORMULA ? cell.getCachedFormulaResultType() : cell.getCellType()) {
			case NUMERIC:
				return cell.getNumericCellValue();
			case BOOLEAN:
				return cell.getBooleanCellValue();
			case BLANK:
			case ERROR:
				return null;
			default:
				return cell.getStringCellValue();
		}
	}

	public static void map2Row(LinkedHashMap<?, ?> map, Row row) {
		AtomicInteger idx = new AtomicInteger();
		map.forEach((key, value) -> row.createCell(idx.getAndIncrement()).setCellValue(value == null ? "" : value.toString()));
	}

	public static Map<String, ?> parseRowValues(Row row) {
		int idx = row.getLastCellNum();
		Map<String, Object> map = new LinkedHashMap<>(idx);
		for (int seq = 0; seq < idx; seq++) {
			map.put(EncryptUtils.intToAlphabetScale(seq + 1).toLowerCase(), parseCellValue(row.getCell(seq)));
		}
		return map;
	}

	public static LinkedHashMap<String, Object> parseRowValuesX(Row row) {
		int idx = row.getLastCellNum();
		LinkedHashMap<String, Object> map = new LinkedHashMap<>(idx);
		for (int seq = 0; seq < idx; seq++) {
			map.put(EncryptUtils.intToAlphabetScale(seq + 1).toLowerCase(), parseCellValue(row.getCell(seq)));
		}
		return map;
	}

	public static Map<Integer, ?> parseRowValues(Row row, int... ids) {
		int x = row.getLastCellNum();
		Map<Integer, Object> map = new LinkedHashMap<>();
		if (EmptyUtils.isEmpty(ids)) {
			for (int seq = 0; seq < x; seq++) {
				map.put(seq, parseCellValue(row.getCell(seq)));
			}
		} else {
			for (int id : ids) {
				if (id > x) {
					map.put(id, null);
				} else {
					map.put(id, parseCellValue(row.getCell(id)));
				}
			}
		}
		return map;
	}

	public static Map<String, ?> parseRowValues(Row row, String... idxs) {
		if (EmptyUtils.isEmpty(idxs)) {
			return parseRowValues(row);
		} else {
			int[] intIds = StreamUtils.streamOf(idxs).map(i -> EncryptUtils.alphabetToIntScale(i) - 1).mapToInt(
					Integer::intValue).toArray();
			Map<Integer, ?> m = parseRowValues(row, intIds);
			Map<String, Object> map = new LinkedHashMap<>(intIds.length);
			for (int id : intIds) {
				map.put(EncryptUtils.intToAlphabetScale(id + 1).toLowerCase(), m.get(id));
			}
			return map;
		}
	}

	public static ExcelStreamlizer streamlizer(InputStream is) {
		return ExcelStreamlizer.of(is);
	}

	public static ExcelStreamlizer initWorkbook(Workbook wb, Stream<LinkedHashMap<?, ?>> mapStream) {
		return ExcelStreamlizer.of(wb, mapStream);
	}

	public static List<Map<String, ?>> tryList(InputStream is) {
		try {
			return streamlizer(new BufferedInputStream(is)).sheetIndex(0).stream().skip(0).map(ExcelUtils::parseRowValues).collect(
					Collectors.toList());
		} catch (Exception e) {
			throw new HorizonRuntimeException(e);
		}
	}

	public static class ExcelStreamlizer {

		InputStream is;
		Predicate<Row> filter = null;
		int sheetIndex = 0;
		String sheetName = null;
		Workbook wb;
		Stream<LinkedHashMap<?, ?>> mapStream;

		ExcelStreamlizer(InputStream is) {
			this.is = AssertionUtils.shouldNotNull(is);
		}

		ExcelStreamlizer(Workbook wb, Stream<LinkedHashMap<?, ?>> mapStream) {
			this.mapStream = mapStream;
			this.wb = wb;
		}

		static ExcelStreamlizer of(InputStream is) {
			return new ExcelStreamlizer(is);
		}

		static ExcelStreamlizer of(Workbook wb, Stream<LinkedHashMap<?, ?>> mapStream) {
			return new ExcelStreamlizer(wb, mapStream);
		}

		public ExcelStreamlizer autoSizeColumn(int columnNumber) {
			for (int i = 0; i < columnNumber; i++) {
				getSheet().autoSizeColumn(i);
			}
			return this;
		}

		public ExcelStreamlizer createRowTitle(String... titles) {
			int[] indexArr = {0};
			Row row = getSheet().createRow(0);
			StreamUtils.streamOf(titles).forEach(title -> row.createCell(indexArr[0]++).setCellValue(
					title));
			return this;
		}

		public ExcelStreamlizer createRowsFrom(int index) {
			AtomicInteger idx = new AtomicInteger(index);
			mapStream.forEach(map -> ExcelUtils.map2Row(map, getSheet().createRow(idx.getAndIncrement())));
			return this;
		}

		public ExcelStreamlizer createSheet() {
			if (sheetName != null) {
				wb.createSheet(sheetName);
			} else {
				wb.createSheet();
			}
			return this;
		}

		public ExcelStreamlizer filter(Predicate<Row> filter) {
			if (filter != null) {
				this.filter = filter;
			}
			return this;
		}

		public Stream<Map<Integer, ?>> mapStream(int... ids) throws Exception {
			AssertionUtils.shouldBeTrue(EmptyUtils.isNotEmpty(ids));
			return stream().map(r -> ExcelUtils.parseRowValues(r, ids));
		}

		public Stream<Map<String, ?>> mapStream(String... idxs) throws Exception {
			AssertionUtils.shouldBeTrue(EmptyUtils.isNotEmpty(idxs));
			return stream().map(r -> ExcelUtils.parseRowValues(r, idxs));
		}

		public Stream<Map<String, ?>> mapStream() throws Exception {
			return stream().map(ExcelUtils::parseRowValues);
		}

		public Stream<LinkedHashMap<String, Object>> linkedHashMapStream() throws Exception {
			return stream().map(ExcelUtils::parseRowValuesX);
		}

		public <T> Stream<T> objectStream(Class<T> cls, Object... idxPro) throws Exception {
			AssertionUtils.shouldBeTrue(EmptyUtils.isNotEmpty(idxPro));
			final Map<Object, Object> mapping = MapUtils.mapOf(idxPro);
			final int size = mapping.size();
			final String[] keys = mapping.keySet().stream().filter(ObjectUtils::isNotNull).map(Object::toString).toArray(
					String[]::new);
			return stream().map(r -> {
				Map<Object, Object> proMap = new LinkedHashMap<>(size);
				parseRowValues(r,
				               keys).entrySet().stream().filter(e -> mapping.containsKey(e.getKey())).forEach(e -> proMap.put(
						mapping.get(e.getKey()),
						e.getValue()));
				return JSONUtil.toBean(new JSONObject(proMap), cls);
			});
		}


		public ExcelStreamlizer sheetName(String sheetName) {
			this.sheetName = AssertionUtils.shouldNotNull(sheetName);
			sheetIndex = -1;
			return this;
		}

		public void write(OutputStream stream) throws IOException {
			wb.write(stream);
		}

		void close() {
			if (wb != null) {
				try {
					wb.close();
				} catch (IOException e) {
					// NOOP!
				}
			}
		}

		Sheet getSheet() {
			return sheetName != null ? wb.getSheet(sheetName) : wb.getSheetAt(sheetIndex);
		}

		Sheet sheet() throws Exception {
			wb = WorkbookFactory.create(is);
			return getSheet();
		}

		ExcelStreamlizer sheetIndex(int sheetIndex) {
			this.sheetIndex = sheetIndex;
			sheetName = null;
			return this;
		}

		Stream<Row> stream() throws Exception {
			if (filter == null) {
				return StreamUtils.streamOf(sheet().rowIterator()).onClose(this::close);
			} else {
				return StreamUtils.streamOf(sheet().rowIterator()).filter(filter).onClose(this::close);
			}
		}

		private ExcelStreamlizer setRowTitleCellStyle(Map<Integer, CellStyle> map) {
			Row row = getSheet().getRow(0);
			for (Map.Entry<Integer, CellStyle> entry : map.entrySet()) {
				Integer integer = entry.getKey();
				CellStyle cellStyle = entry.getValue();
				row.getCell(integer).setCellStyle(cellStyle);
			}
			return this;
		}
	}
}
