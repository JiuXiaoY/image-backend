package com.image.project.utils;

/**
 * @author JXY
 * @version 1.0
 * @since 2024/6/1
 */
public class String2LongUtil {
    /**
     * 将一个两位小数的 String 类型的金额转换为 Long
     * @param str 金额
     * @return Long
     */
    public static Long string2Long(String str) {
        Number number = Float.parseFloat(str) * 100;
        int newNumber = number.intValue();
        return (long) newNumber;
    }
}
