package org.redcarp.horizon.infrastructure.test;

import cn.hutool.http.HttpUtil;
import org.junit.jupiter.api.Test;

/**
 * @author redcarp
 * @date 2024/3/11
 */

public class ExcelWebTest {

	@Test
	public void download() {
		HttpUtil.downloadFile("http://localhost:8080/infrastructure/excel/export",
		                      "/Users/renweiwei/IdeaProjects/tempFile/abc.xlsx");
	}
}
