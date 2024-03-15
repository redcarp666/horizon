package org.redcarp.horizon.core.test;

import cn.hutool.core.map.MapUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.redcarp.horizon.core.util.MapUtils;

import java.util.Map;

/**
 * @author redcarp
 * @date 2024/3/15
 */
public class MapUtilsTest {
	@Test
	public void testMapOf() {
		Map<Object, Object> map = MapUtils.mapOf("key1", "value1", "key2", "value2", "key3", 3);
		Assertions.assertEquals("{key1=value1, key2=value2, key3=3}", map.toString());
	}

	@Test
	public void testMapOf0() {
		Map<Object, Object> map = MapUtils.mapOf("key1", "value1", "key2", "value2", "key3");
		Assertions.assertEquals("{key1=value1, key2=value2, key3=null}", map.toString());
	}

	@Test
	public void testMapOf1() {
		Map<Object, Object> map = MapUtil.builder().put("key2", "value2").put("key3", "value3").put("key4", 4).build();
		Assertions.assertEquals("{key2=value2, key3=value3, key4=4}", map.toString());
	}
}
