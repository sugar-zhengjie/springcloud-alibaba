package com.zj.util;


import com.zj.constant.BaseConstants;

import java.io.UnsupportedEncodingException;

/**
 * @author zhengjie
 * @version 1.0
 * @date 2023/11/9 14:25
 */
public class StringUtils {
    private StringUtils() {
    }

    public static byte[] getBytes(String txt) {
        if (txt == null) {
            throw new NullPointerException();
        }

        try {
            return txt.getBytes(BaseConstants.DEFAULT_CHARSET_NAME);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public static String toString(Object obj) {
        if (obj == null) {
            return null;
        }

        return obj.toString();
    }

    public static String newString(byte[] bytes) {
        try {
            return new String(bytes, BaseConstants.DEFAULT_CHARSET_NAME);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean isEmptyOrNull(String text) {
        if (text == null) {
            return true;
        }

        return text.trim().isEmpty();
    }

    /**
     * 补齐不足长度
     *
     * @param length 长度
     * @param number 数字
     * @return
     */
    public static String lpad(int length, int number) {
        String f = "%0" + length + "d";
        return String.format(f, number);
    }
}
