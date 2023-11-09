package com.zj.util;

/**
 * @author zhengjie
 * @version 1.0
 * @date 2023/11/9 14:25
 */
public class Assert {

    private Assert() {
    }

    public static void notNull(Object object, String message) {
        if (object == null) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void notNull(Object object) {
        notNull(object, "[Assertion failed] - this argument is required; it must not be null");
    }

    public static void notNullOrEmpty(String str, String message) {
        if (str == null || str.trim().isEmpty()) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void notNullOrEmpty(String str) {
        notNullOrEmpty(str, "[Assertion failed] - the argument is required; it must not be null or empty");
    }

}
