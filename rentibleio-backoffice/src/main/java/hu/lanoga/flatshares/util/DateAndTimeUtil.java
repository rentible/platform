package hu.lanoga.flatshares.util;

import java.sql.Timestamp;

public class DateAndTimeUtil {

    /**
     * @return the current time in Timestamp(Millisec)
     */
    public static Timestamp now() {
        return new Timestamp(System.currentTimeMillis());
    }

    private DateAndTimeUtil() {
    }
}
