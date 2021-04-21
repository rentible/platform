package org.wallride.util;

import org.apache.commons.lang.StringUtils;

import java.util.Arrays;
import java.util.Objects;

public class ObjectUtil {

	private ObjectUtil() {
	}

	/**
	 * Check (very first the whole object) all field of a class is null or empty
	 *
	 * @param o
	 * @return
	 */
	public static boolean checkAllFieldNullOrEmpty(Object o) {
		if (o == null) {
			return true;
		}

		return Arrays.asList(o.getClass().getDeclaredFields()).stream().allMatch(field -> {
			boolean result = true;
			try {
				if (!Objects.isNull(o.getClass().getDeclaredField(field.getName()).get(o)) || (o instanceof String && StringUtils.isNotBlank((String) o))) {
					result = false;
				}
			} catch (IllegalAccessException | NoSuchFieldException e) {
				throw new RuntimeException(e);
			}
			return result;
		});
	}
}
