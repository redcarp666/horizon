package org.redcarp.horizon.core.util;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.write.builder.ExcelWriterBuilder;
import com.alibaba.excel.write.handler.SheetWriteHandler;

import java.io.OutputStream;
import java.util.List;

/**
 * @author redcarp
 * @date 2024/5/20
 */
public class EasyExcelUtils extends EasyExcel {

	public static ExcelWriterBuilder write(OutputStream outputStream, Class<?> head, SheetWriteHandler writeHandler) {
		ExcelWriterBuilder excelWriterBuilder = new ExcelWriterBuilder();
		excelWriterBuilder.file(outputStream);
		if (head != null) {
			excelWriterBuilder.head(head);
		}
		if (writeHandler != null) {
			excelWriterBuilder.registerWriteHandler(writeHandler);
		}
		return excelWriterBuilder;
	}

	public static ExcelWriterBuilder write(OutputStream outputStream, List<List<String>> head) {
		ExcelWriterBuilder excelWriterBuilder = new ExcelWriterBuilder();
		excelWriterBuilder.file(outputStream);
		if (head != null) {
			excelWriterBuilder.head(head);
		}
		return excelWriterBuilder;
	}

}
