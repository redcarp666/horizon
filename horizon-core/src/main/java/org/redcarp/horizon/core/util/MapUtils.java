package org.redcarp.horizon.core.util;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.IntFunction;


public class MapUtils {

	private MapUtils() {
	}


	public static <K, V, M extends Map<K, V>> M mapOf(IntFunction<M> factory, Object... objects) {
		int oLen;
		if (objects == null || (oLen = objects.length) == 0) {
			return factory.apply(0);
		}
		int rLen = (oLen & 1) == 0 ? oLen : oLen - 1;
		int size = (rLen >> 1) + 1;
		M map = factory.apply(size);
		for (int i = 0; i < rLen; i += 2) {
			map.put(ObjectUtils.forceCast(objects[i]), ObjectUtils.forceCast(objects[i + 1]));
		}
		if (rLen < oLen) {
			map.put(ObjectUtils.forceCast(objects[rLen]), null);
		}
		return map;
	}

	@SafeVarargs
	public static <K, V> Map<K, V> mapOf(Map.Entry<? extends K, ? extends V>... entries) {
		Object[] array = new Object[entries.length << 1];
		int len = 0;
		for (Map.Entry<? extends K, ? extends V> entry : entries) {
			array[len++] = entry.getKey();
			array[len++] = entry.getValue();
		}
		return mapOf(HashMap::new, array);
	}

	public static <K, V> Map<K, V> linkedHashMapOf(Object... objects) {
		return mapOf(LinkedHashMap::new, objects);
	}

	public static <K, V> Map<K, V> mapOf(Object... objects) {
		return mapOf(HashMap::new, objects);
	}

	public static <K, V> Map<K, V> immutableMapOf(Object... objects) {
		if (objects == null || objects.length == 0) {
			return Collections.emptyMap();
		}
		return Collections.unmodifiableMap(mapOf(objects));
	}
}
