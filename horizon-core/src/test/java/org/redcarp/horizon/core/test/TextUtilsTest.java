package org.redcarp.horizon.core.test;

import org.junit.jupiter.api.Test;
import org.redcarp.horizon.core.util.FileUtils;
import org.redcarp.horizon.core.util.TextUtils;

import java.io.File;
import java.io.IOException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author redcarp
 * @date 2024/3/18
 */
public class TextUtilsTest {
	@Test
	public void test() throws IOException {
		File file = FileUtils.file("文本.txt");
		File writeFile = FileUtils.file("文本-写.txt");
		Stream<String> lines = TextUtils.lines(file);
		lines.forEach(System.out::println);
		System.out.println("--------- limit offset test ---------");
		TextUtils.lines(file, 3, 4).forEach(System.out::println);
		// 相对路径，在target目录下找
		TextUtils.writeToFile(writeFile, TextUtils.lines(file, 2, 5).collect(Collectors.toList()));
	}
}
