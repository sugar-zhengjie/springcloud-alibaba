package com.zj.common.util;

import java.util.Base64;

/**
 * @author zhengjie
 * @version 1.0
 * @date 2023/11/9 14:25
 */
public class Base64Utils {

    private static final Base64Handler handler;

    static {
        handler = new Base64HandlerCodecJava();
    }


    public static String encodeToString(byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        if (bytes.length == 0) {
            return "";
        }
        return handler.encode(bytes);
    }

    public static byte[] decodeFromString(String base64String) {
        if (base64String == null) {
            return new byte[0];
        }
        if (base64String.isEmpty()) {
            return new byte[0];
        }

        return handler.decode(base64String);
    }

    private interface Base64Handler {
        /**
         * encode
         * @param bytes
         * @return
         */
        String encode(byte[] bytes);

        /**
         * decode
         * @param base64
         * @return
         */
        byte[] decode(String base64);
    }

    private static class Base64HandlerCodecJava implements Base64Handler {

        Base64.Encoder encoder = Base64.getEncoder();
        Base64.Decoder decoder = Base64.getDecoder();

        @Override
        public String encode(byte[] bytes) {
            return encoder.encodeToString(bytes);
        }

        @Override
        public byte[] decode(String base64Text) {
            return decoder.decode(base64Text);
        }
    }

}
