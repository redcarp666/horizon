import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.io.FileUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.annotation.ExcelProperty;
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
public class EasyExcelTest {
	static String filePath = "测试.xlsx";

	@Test
	public void testRead() {
		File file = FileUtil.file(filePath);
		//headRowNumber(0)从第0行开始读取
		List<Map<?, ?>> objects = EasyExcel.read(file).sheet().headRowNumber(0).doReadSync();
		//map的key值为0,1,2,3,4
		objects.forEach(object -> {
			System.out.println("object = " + object);
		});
	}

	@Test
	public void testWrite() {
		User user1 = new User("张飞", 28, "110");
		User user2 = new User("关羽", 26, "120");
		User user3 = new User("刘备", 30, "119");
		File file = FileUtil.file("测试-写.xlsx");
		EasyExcel.write(file, User.class).sheet().doWrite(ListUtil.of(user1, user2, user3));
	}


	@Data
	@AllArgsConstructor
	private static class User {
		@ExcelProperty("姓名")
		private String name;
		@ExcelProperty("年龄")
		private Integer age;
		@ExcelProperty("电话")
		private String telephone;
	}

}
