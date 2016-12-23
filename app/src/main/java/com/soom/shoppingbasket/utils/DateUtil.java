package com.soom.shoppingbasket.utils;

import org.apache.commons.lang3.time.DateFormatUtils;

import java.util.Date;

/**
 * Created by kjs on 2016-12-23.
 */

public class DateUtil {
    public static final String DATETIME = "yyyy-MM-dd HH:mm:ss";

    /**
     * Date -> String 포맷.
     *
     * @param date
     * @param format
     * @return
     */
    public static String format(Date date, String format) {
        return DateFormatUtils.format(date, format);
    }

    /**
     * 현재 시간을 yyyy-mm-dd hh:mm:ss 형식으로 반환한다.
     *
     * @return 현재시간.
     */
    public static String currentDateToString() {
        return format(new Date(), DATETIME);
    }
}
