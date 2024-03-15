import cn.hutool.core.io.FileUtil;
import cn.hutool.poi.excel.ExcelUtil;
import lombok.Data;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * @author redcarp
 * @date 2024/3/15
 */
public class ExcelUtilTest {

	static String filePath = "测试.xlsx";

	@Test
	public void testReadAll() {
		File file = FileUtil.file(filePath);
		//读取Excel为Map的列表，读取所有行，默认第一行做为标题，数据从第二行开始
		//key值为标题
		List<Map<String, Object>> maps = ExcelUtil.getReader(file).readAll();
		maps.forEach(stringObjectMap -> System.out.println("stringObjectMap = " + stringObjectMap));
	}

	@Test
	public void testReadAll1() {
		//推荐直接使用返回的map，可遍历map自己组装成bean
		//源码是用map转bean的方式实现返回bean类型的,所以用bean类型的话需要字段名和excel的标题一致
		List<User> users = ExcelUtil.getReader(filePath).readAll(User.class);
		users.forEach(user -> System.out.println("user = " + user));
	}

	@Data
	private static class User {
		private String name;
		private String age;
		private String telephone;
	}
}
