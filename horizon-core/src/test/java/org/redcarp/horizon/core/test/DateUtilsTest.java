package org.redcarp.horizon.core.test;

import org.junit.jupiter.api.Test;
import org.redcarp.horizon.core.util.DateUtils;

import java.util.List;

/**
 * @author ssh
 * @date 2025/1/14
 */
public class DateUtilsTest {

	@Test
	public void testDateRange() {

		List<String> strings = DateUtils.dateRangeToSeqList("1237-01-02", "1235-01-01");
		for (String string : strings) {
			System.out.println("string = " + string);
		}

	}

}
