import cn.hutool.core.io.FileUtil;
import org.junit.jupiter.api.Test;

import java.io.File;

/**
 * @author redcarp
 * @date 2024/3/15
 */
public class FileUtilTest {

	@Test
	public void test() {
		//创建File对象，自动识别相对或绝对路径，相对路径将自动从ClassPath下寻找
		File file = FileUtil.file("测试.xlsx");
		String name = file.getName();
		System.out.println("name = " + name);
		String absolutePath = file.getAbsolutePath();
		//absolutePath = /Users/renweiwei/IdeaProjects/horizon/horizon-core/target/test-classes/测试.xlsx
		System.out.println("absolutePath = " + absolutePath);
	}
}
