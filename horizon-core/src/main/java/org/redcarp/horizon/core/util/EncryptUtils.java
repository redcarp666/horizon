package org.redcarp.horizon.core.util;

import org.redcarp.horizon.core.exception.HorizonRuntimeException;

import java.util.Locale;


public class EncryptUtils {

	
	private EncryptUtils() {
	}

	/**
	 * 从字母组合转为正整数，A->1，B->2...Z-26，AA-27，AB-28....
	 */
	public static int alphabetToIntScale(String alphabet) {
		if (alphabet == null) {
			throw new HorizonRuntimeException("Param can't null!");
		}
		if (!alphabet.chars().allMatch(x -> x >= 0x41 && x <= 0x5a || x >= 0x61 && x <= 0x7a)) {
			throw new HorizonRuntimeException("Param must be alphabet!");
		}
		int[] idx = {0};
		return new StringBuilder(alphabet.trim().toUpperCase(Locale.ENGLISH)).reverse().chars().map(i -> (i - 64) * (int) Math.pow(
				26,
				idx[0]++)).reduce(-1, Integer::sum) + 1;
	}


	/**
	 * 正整数转为26进制的大写字母组合，1->A，2->B...26->Z，27->AA...
	 */
	public static String intToAlphabetScale(int number) {
		int num = number;
		if (num <= 0) {
			throw new HorizonRuntimeException("Param must greater than 0!");
		}
		StringBuilder sb = new StringBuilder();
		while (num > 0) {
			int r = num % 26;
			if (r == 0) {
				r = 26;
			}
			sb.append((char) (r + 64));
			num = (num - r) / 26;
		}
		return sb.reverse().toString();
	}


}
