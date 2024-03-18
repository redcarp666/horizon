package org.redcarp.horizon.core.test;

import cn.hutool.core.collection.ListUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.redcarp.horizon.core.util.StreamUtils;

import java.util.List;
import java.util.Optional;

/**
 * @author redcarp
 * @date 2024/3/18
 */
public class StreamUtilsTest {
	@Test
	public void test() {
		List<String> list = ListUtil.toList("a", "b", "c");
		Optional<String> any = StreamUtils.streamOf(list).filter(s -> s.equals("a")).findAny();
		any.ifPresent(s -> Assertions.assertEquals("a", s));
	}
}
