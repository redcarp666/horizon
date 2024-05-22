package org.redcarp.horizon.core.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.write.handler.SheetWriteHandler;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteWorkbookHolder;
import lombok.Data;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author redcarp
 * @date 2024/5/22
 */
@Data
public class SelectedSheetWriteHandler implements SheetWriteHandler {
	private Map<Integer, ExcelSelectedResolve> selectedMap;

	public SelectedSheetWriteHandler(Class<?> head) {
		resolveSelectedAnnotation(head);
	}

	private void resolveSelectedAnnotation(Class<?> head) {
		selectedMap = new HashMap<>();
		Field[] fields = head.getDeclaredFields();
		List<Field> fieldList = Arrays.stream(fields).filter(e -> !Modifier.isStatic(e.getModifiers())).collect(
				Collectors.toList());
		for (int i = 0; i < fieldList.size(); i++) {
			Field field = fieldList.get(i);
			ExcelSelected selected = field.getAnnotation(ExcelSelected.class);
			ExcelProperty property = field.getAnnotation(ExcelProperty.class);
			if (selected != null) {
				ExcelSelectedResolve excelSelectedResolve = new ExcelSelectedResolve();
				String[] source = excelSelectedResolve.resolveSelectedSource(selected);
				if (source != null && source.length > 0) {
					excelSelectedResolve.setSource(source);
					excelSelectedResolve.setFirstRow(selected.firstRow());
					excelSelectedResolve.setLastRow(selected.lastRow());
					if (property != null && property.index() >= 0) {
						selectedMap.put(property.index(), excelSelectedResolve);
					} else {
						selectedMap.put(i, excelSelectedResolve);
					}
				}
			}
		}
	}

	@Override
	public void afterSheetCreate(WriteWorkbookHolder writeWorkbookHolder, WriteSheetHolder writeSheetHolder) {
		Sheet sheet = writeSheetHolder.getSheet();
		Workbook workbook = writeWorkbookHolder.getWorkbook();
		DataValidationHelper helper = sheet.getDataValidationHelper();
		String hiddenName = "hidden";
		SXSSFWorkbook sw = (SXSSFWorkbook) workbook;
		XSSFSheet hiddenSheet = sw.getXSSFWorkbook().createSheet(hiddenName);
		workbook.setSheetHidden(workbook.getSheetIndex(hiddenName), true);
		selectedMap.forEach((k, v) -> {
			CellRangeAddressList rangeList = new CellRangeAddressList(v.getFirstRow(), v.getLastRow(), k, k);
			String excelLine = getExcelLine(k);
			String[] values = v.getSource();
			generateSelectValue(hiddenSheet, k, values);
			String refers = hiddenName + "!$" + excelLine + "$1:$" + excelLine + "$" + (values.length);
			DataValidationConstraint constraint = helper.createFormulaListConstraint(refers);
			DataValidation validation = helper.createValidation(constraint, rangeList);
			// 设置下拉列表的值
			//DataValidationConstraint constraint = helper.createExplicitListConstraint(v.getSource());
			validation.setErrorStyle(DataValidation.ErrorStyle.STOP);
			validation.setShowErrorBox(true);
			validation.setSuppressDropDownArrow(true);
			validation.createErrorBox("提示", "请输入下拉选项中的内容");
			sheet.addValidationData(validation);
		});

	}

	private void generateSelectValue(Sheet sheet, int col, String[] values) {
		for (int i = 0, length = values.length; i < length; i++) {
			Row row = sheet.getRow(i);
			if (row == null) {
				row = sheet.createRow(i);
			}
			row.createCell(col).setCellValue(values[i]);
		}
	}

	private String getExcelLine(int num) {
		String line = "";
		int first = num / 26;
		int second = num % 26;
		if (first > 0) {
			line = (char) ('A' + first - 1) + "";
		}
		line += (char) ('A' + second) + "";
		return line;
	}

}
