package com.zb.commons.number;

/**
 * 数字处理工具类
 * 
 */
public final class NumberUtil {

    private NumberUtil() {
    }

    /**
     * 包装类转换为原生
     * 
     * @param value
     * @param defaultValue
     * @return
     */
    public static int toInt(Integer value, int defaultValue) {
        return value == null ? defaultValue : value;
    }

    /**
     * 求和
     * <p>
     *     如有参数为 null, 默认按 0 处理
     * </p>
     * 
     * @param integers
     * @return
     */
    public static int sum(Integer... integers) {
        if (integers == null || integers.length == 0) {
            return 0;
        }
        int sum = 0;
        for (Integer i : integers) {
            sum += toInt(i, 0);
        }
        return sum;
    }
}
