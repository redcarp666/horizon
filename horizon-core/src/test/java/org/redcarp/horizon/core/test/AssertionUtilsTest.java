package org.redcarp.horizon.core.test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.redcarp.horizon.core.exception.HorizonRuntimeException;
import org.redcarp.horizon.core.util.AssertionUtils;

/**
 * @author redcarp
 * @date 2024/3/18
 */
public class AssertionUtilsTest {

	@Test
	public void shouldBeEquals() {
		Assertions.assertThrowsExactly(HorizonRuntimeException.class,
		                               () -> AssertionUtils.shouldBeEquals("110", "120", "should be %s", "string"));

	}

	@Test
	public void shouldBeEquals1() {
		String a = "110";
		String b = "120";
		Assertions.assertThrowsExactly(HorizonRuntimeException.class,
		                               () -> AssertionUtils.shouldBeEquals(a,
		                                                                   b,
		                                                                   () -> new HorizonRuntimeException(
				                                                                   "should be equal %s",
				                                                                   a)));
	}

	@Test
	public void shouldBeTrue() {
		AssertionUtils.shouldBeTrue(1 == 2);
	}


}
