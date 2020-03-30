package com.flow.chat.common;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Description:
 * @Auther: qingruizhu
 * @Date: 2020/3/24 14:54
 */
public class DateformateUtil {
    private static DateFormat yMd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static String yMd(Date date) {
        return yMd.format(date);
    }

}
