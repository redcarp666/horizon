import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.poi.excel.ExcelUtil;
import lombok.AllArgsConstructor;
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

	@Test
	public void testWrite() {
		User user1 = new User("张飞", 28, "110");
		User user2 = new User("关羽", 26, "120");
		User user3 = new User("刘备", 30, "119");
		ExcelUtil.getWriter("测试-写-hutool.xlsx").write(ListUtil.of(user1, user2, user3)).flush().close();
	}

	@Data
	@AllArgsConstructor
	private static class User {
		private String name;
		private Integer age;
		private String telephone;
	}
}
