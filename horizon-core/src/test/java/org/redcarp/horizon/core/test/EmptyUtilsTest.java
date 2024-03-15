package org.redcarp.horizon.core.test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.redcarp.horizon.core.util.EmptyUtils;
import org.redcarp.horizon.core.util.MapUtils;

import java.util.ArrayList;
import java.util.Map;

/**
 * @author redcarp
 * @date 2024/3/15
 */
public class EmptyUtilsTest {

	@Test
	public void testList() {
		boolean empty = EmptyUtils.isEmpty(new ArrayList<String>());
		Assertions.assertTrue(empty);
	}

	@Test
	public void testMap() {
		Map<Object, Object> map = MapUtils.mapOf();
		boolean empty = EmptyUtils.isEmpty(map);
		Assertions.assertTrue(empty);
		map.put("abc", "def");
		empty = EmptyUtils.isEmpty(map);
		Assertions.assertFalse(empty);
	}
}
