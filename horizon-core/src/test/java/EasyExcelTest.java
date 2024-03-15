import cn.hutool.core.io.FileUtil;
import com.alibaba.excel.EasyExcel;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * @author redcarp
 * @date 2024/3/15
 */
public class EasyExcelTest {
	static String filePath = "测试.xlsx";

	@Test
	public void test() {
		File file = FileUtil.file(filePath);
		//headRowNumber(0)从第0行开始读取
		List<Map<?, ?>> objects = EasyExcel.read(file).sheet().headRowNumber(0).doReadSync();
		//map的key值为0,1,2,3,4
		objects.forEach(object -> {
			System.out.println("object = " + object);
		});
	}

}
