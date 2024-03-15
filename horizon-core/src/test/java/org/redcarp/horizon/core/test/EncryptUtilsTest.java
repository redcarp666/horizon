package org.redcarp.horizon.core.test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.redcarp.horizon.core.util.EncryptUtils;

/**
 * @author redcarp
 * @date 2024/3/15
 */
public class EncryptUtilsTest {
	@Test
	public void test() {
		int a = EncryptUtils.alphabetToIntScale("a");
		Assertions.assertEquals(a, 1);
		a = EncryptUtils.alphabetToIntScale("A");
		Assertions.assertEquals(a, 1);
		int b = EncryptUtils.alphabetToIntScale("b");
		Assertions.assertEquals(b, 2);
		int c = EncryptUtils.alphabetToIntScale("c");
		Assertions.assertEquals(c, 3);
		int d = EncryptUtils.alphabetToIntScale("d");
		Assertions.assertEquals(d, 4);
		int e = EncryptUtils.alphabetToIntScale("e");
		Assertions.assertEquals(e, 5);
		int f = EncryptUtils.alphabetToIntScale("f");
		Assertions.assertEquals(f, 6);
		int g = EncryptUtils.alphabetToIntScale("g");
		Assertions.assertEquals(g, 7);
		int h = EncryptUtils.alphabetToIntScale("h");
		Assertions.assertEquals(h, 8);
		int i = EncryptUtils.alphabetToIntScale("i");
		Assertions.assertEquals(i, 9);
		int j = EncryptUtils.alphabetToIntScale("j");
		Assertions.assertEquals(j, 10);
		int k = EncryptUtils.alphabetToIntScale("k");
		Assertions.assertEquals(k, 11);
		int l = EncryptUtils.alphabetToIntScale("l");
		Assertions.assertEquals(l, 12);
		int m = EncryptUtils.alphabetToIntScale("m");
		Assertions.assertEquals(m, 13);

	}
}
