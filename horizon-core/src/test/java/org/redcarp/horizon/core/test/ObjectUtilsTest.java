package org.redcarp.horizon.core.test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.redcarp.horizon.core.util.ObjectUtils;

import java.util.Comparator;

import static org.redcarp.horizon.core.test.ExcelUtilTest.User;

/**
 * @author redcarp
 * @date 2024/3/15
 */
public class ObjectUtilsTest {
	@Test
	public void asStringNullDefault() {
		String string = ObjectUtils.asString(null, "abc");
		Assertions.assertEquals("abc", string);
	}

	@Test
	public void asString() {
		User user = new User("redcarp", 28, "18865564654");
		String string = ObjectUtils.asString(user);
		Assertions.assertEquals(
				"org.redcarp.horizon.core.test.ExcelUtilTest.User(name=redcarp, age=28, telephone=18865564654)",
				string);
	}

	@Test
	public void defaultObject() {
		User user = null;
		User redcarp = new User("redcarp", 23, "110");
		User defaultObj = ObjectUtils.defaultObject(user, redcarp);
		Assertions.assertNotNull(defaultObj);
	}

	@Test
	public void defaultObj() {
		User user = null;
		User redcarp = ObjectUtils.defaultObject(user, () -> new User("redcarp", 23, "110"));
		Assertions.assertNotNull(redcarp);

	}

	@Test
	public void compare() {
		int compare = ObjectUtils.compare(1, 2);
		Assertions.assertEquals(-1, compare);
	}

	@Test
	public void compare1() {
		User redcarp = new User("redcarp", 23, "110");
		User ray = new User("ray", 24, "119");
		int compare = ObjectUtils.compare(ray, redcarp, Comparator.comparing(User::getAge));
		Assertions.assertEquals(1, compare);
		int compare1 = ObjectUtils.compare(redcarp, ray, Comparator.comparing(User::getAge));
		Assertions.assertEquals(-1, compare1);
		// equal
		User zf = new User("zhangfei", 23, "110");
		User gy = new User("guanyu", 23, "119");
		int compare2 = ObjectUtils.compare(zf, gy, Comparator.comparing(User::getAge));
		Assertions.assertEquals(0, compare2);
	}

	@Test
	public void maxAndMin() {
		Integer max = ObjectUtils.max(1, 2, 3, 4, 45);
		Assertions.assertEquals(45, max);
		Integer min = ObjectUtils.min(1, 2, 3, 4, 45);
		Assertions.assertEquals(1, min);
	}
}
