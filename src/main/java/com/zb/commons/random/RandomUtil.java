package com.zb.commons.random;

import com.zb.commons.date.DateOperator;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.Date;

/**
 * @author zhangbo
 */
public final class RandomUtil {

    private RandomUtil() {
    }
    
    public static String formatDatetimeNumberic(int randomCount) {
        return DateOperator.formatDate(new Date(), "yyyyMMddHHmmssSSS") + RandomStringUtils.randomNumeric(randomCount);
    }
    
    public static String millisecondsNumberic(int randomCount) {
        return System.currentTimeMillis() + RandomStringUtils.randomNumeric(randomCount);
    }
    
    public static String numberic(int count) {
        return RandomStringUtils.randomNumeric(count);
    }
    
    public static String alphabetic(int count) {
        return RandomStringUtils.randomAlphabetic(count);
    }
    
    public static String alphanumeric(int count) {
        return RandomStringUtils.randomAlphanumeric(count);
    }
    
    public static String ascii(int count) {
        return RandomStringUtils.randomAscii(count);
    }
    
}
