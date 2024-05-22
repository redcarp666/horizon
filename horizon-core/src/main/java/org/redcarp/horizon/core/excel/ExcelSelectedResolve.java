package org.redcarp.horizon.core.excel;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;

/**
 * @author redcarp
 * @date 2024/5/22
 */
@Data
@Slf4j
public class ExcelSelectedResolve {

	private String[] source;

	private int firstRow;

	private int lastRow;

	public String[] resolveSelectedSource(ExcelSelected excelSelected) {
		if (excelSelected == null) {
			return null;
		}
		String[] source = excelSelected.source();
		if (source.length > 0) {
			return source;
		}
		Class<? extends ExcelDynamicSelect>[] classes = excelSelected.sourceClass();
		if (classes.length > 0) {
			try {
				ExcelDynamicSelect excelDynamicSelect = classes[0].getDeclaredConstructor().newInstance();
				String[] dynamicSelectSource = excelDynamicSelect.getSource();
				if (dynamicSelectSource != null && dynamicSelectSource.length > 0) {
					return dynamicSelectSource;
				}
			} catch (InstantiationException | IllegalAccessException | NoSuchMethodException |
			         InvocationTargetException e) {
				log.error("动态下拉框数据解析异常", e);
			}
		}
		return null;
	}
}
