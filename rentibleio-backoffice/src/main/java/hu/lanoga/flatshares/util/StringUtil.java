package hu.lanoga.flatshares.util;

import org.apache.commons.lang3.StringUtils;

public class StringUtil {

    /**
     * This method check the parameter is not blank and does not contain "null" as string.
     * Useful to check value of an input field
     *
     * @param str
     * @return true if it contains valid value
     */
    public static boolean isNotBlackAndNull(String str) {
        return StringUtils.isNotBlank(str) && !str.equalsIgnoreCase("null");
    }

    private StringUtil() {
    }
}
