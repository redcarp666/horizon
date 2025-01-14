package org.redcarp.horizon.core.util;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateUtil;
import org.redcarp.horizon.core.exception.NotSupportedException;

import java.util.List;
import java.util.stream.Collectors;

import static cn.hutool.core.date.DatePattern.*;
import static cn.hutool.core.date.DatePattern.NORM_YEAR_PATTERN;

/**
 * @author ssh
 * @date 2025/1/14
 */
public class DateUtils extends DateUtil {
	private static final String DATE_REGEX = "\\d{4}-\\d{2}-\\d{2}";
	private static final String MONTH_REGEX = "\\d{4}-\\d{2}";
	private static final String YEAR_REGEX = "\\d{4}";

	/**
	 * eg.param: 2024-01,2024-03
	 * return: 2024-01,2024-02,2024-03
	 * @param startDate 开始日期
	 * @param endDate 结束日期
	 * @return 开始日期到结束日期每个日期按顺序的列表
	 * @author ssh
	 * @date 2025/1/14
	 * */
	public static List<String> dateRangeToSeqList(String startDate, String endDate) {
		if (startDate.matches(DATE_REGEX) && endDate.matches(DATE_REGEX)) {
			// 格式为 yyyy-MM-dd
			return DateUtil.rangeToList(DateUtil.parse(startDate, NORM_DATE_PATTERN),
			                            DateUtil.parse(endDate, NORM_DATE_PATTERN),
			                            DateField.DAY_OF_YEAR).stream().map(dateTime -> dateTime.toString(
					NORM_DATE_PATTERN)).collect(Collectors.toList());
		} else if (startDate.matches(MONTH_REGEX) && endDate.matches(MONTH_REGEX)) {
			// 格式为 yyyy-MM
			return DateUtil.rangeToList(DateUtil.parse(startDate, NORM_MONTH_PATTERN),
			                            DateUtil.parse(endDate, NORM_MONTH_PATTERN),
			                            DateField.MONTH).stream().map(dateTime -> dateTime.toString(NORM_MONTH_PATTERN)).collect(
					Collectors.toList());
		} else if (startDate.matches(YEAR_REGEX) && endDate.matches(YEAR_REGEX)) {
			// 格式为 yyyy
			return DateUtil.rangeToList(DateUtil.parse(startDate, NORM_YEAR_PATTERN),
			                            DateUtil.parse(endDate, NORM_YEAR_PATTERN),
			                            DateField.YEAR).stream().map(dateTime -> dateTime.toString(NORM_YEAR_PATTERN)).collect(
					Collectors.toList());
		} else {
			throw new NotSupportedException("startDate %s format and endDate %s format should be equaled",
			                                startDate,
			                                endDate);
		}
	}

}
