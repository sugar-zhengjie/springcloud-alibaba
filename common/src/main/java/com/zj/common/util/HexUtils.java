package com.zj.common.util;

/**
 * @author chen
 */
public class HexUtils {

    private static final char[] DIGITS_LOWER =
            {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    private static final char[] DIGITS_UPPER =
            {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    private static final int HEX_LEN =0x01;

    private HexUtils() {
    }

    public static String bytes2Hex(byte[] data, boolean lower) {
        char[] toDigits = lower ? DIGITS_LOWER : DIGITS_UPPER;
        StringBuilder sb = new StringBuilder();
        final int l = data.length;
        for (int i = 0, j = 0; i < l; i++) {
            sb.append(toDigits[(0xF0 & data[i]) >>> 4]);
            sb.append(toDigits[0x0F & data[i]]);
        }
        return sb.toString();
    }

    public static String bytes2Hex(byte[] data) {
        return bytes2Hex(data, true);
    }

    public static byte[] hex2Bytes(String hex) {
        char[] data = hex.toCharArray();
        final int len = data.length;

        if ((len & HEX_LEN) != 0) {
            throw new RuntimeException("Odd number of characters.");
        }

        final byte[] out = new byte[len >> 1];

        for (int i = 0, j = 0; j < len; i++) {
            int f = toDigit(data[j], j) << 4;
            j++;
            f = f | toDigit(data[j], j);
            j++;
            out[i] = (byte) (f & 0xFF);
        }

        return out;
    }

    protected static int toDigit(final char ch, final int index) {
        final int digit = Character.digit(ch, 16);
        if (digit == -1) {
            throw new RuntimeException("Illegal hexadecimal character " + ch + " at index " + index);
        }
        return digit;
    }

    public static String byte2hex(byte[] b) {
        if (b == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < b.length; i++) {
            int v = b[i] & 0xff;
            sb.append(DIGITS_UPPER[v / 0x10]).append(DIGITS_UPPER[v % 0x10]);
        }
        return sb.toString();
    }

    public static byte[] hex2byte(String s) {
        if (s == null) {
            return null;
        }
        byte[] b = s.getBytes();
        if ((b.length % 2) != 0) {
            throw new IllegalArgumentException();
        }
        byte[] b2 = new byte[b.length / 2];
        for (int n = 0; n < b.length; n += 2) {
            String item = new String(b, n, 2);
            b2[n / 2] = (byte) Integer.parseInt(item, 16);
        }
        return b2;
    }

}
