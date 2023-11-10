package com.zj.common.util;

import com.zj.common.constant.BaseConstants;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author chen
 */
@SuppressWarnings({"methodname"})
public class HashUtils {

    private static final String SHA_TYPE = "HmacSHA256";
    public static final String SHA_256 = "SHA-256";
    public static final String SHA_1 = "SHA-1";

    private HashUtils() {
    }

    /**
     *  SHA256 最后使用十六进制字符串表示
     *
     * @param src
     * @return
     */
    public static String sha256(String src) {
        return HexUtils.bytes2Hex(sha256ToBytes(StringUtils.getBytes(src)));
    }

    /**
     *  SHA256 最后使用十六进制字符串表示
     *
     * @param src
     * @return
     */
    public static String sha256(byte[] src) {
        return HexUtils.bytes2Hex(sha256ToBytes(src));
    }

    public static byte[] sha256ToBytes(byte[] src) {
        return getDigest(SHA_256).digest(src);
    }

    public static byte[] sha256ToBytes(String src) {
        return getDigest(SHA_256).digest(StringUtils.getBytes(src));
    }

    private static MessageDigest getDigest(final String algorithm) {
        try {
            return MessageDigest.getInstance(algorithm);
        } catch (final NoSuchAlgorithmException e) {
            throw new IllegalArgumentException(e);
        }
    }

    /**
     *  SHA256 最后使用Base64字符串表示
     *
     * @param src
     * @return
     */
    public static String sha256Base64(String src) {
        return Base64Utils.encodeToString(sha256ToBytes(src));
    }

    /**
     *  SHA256 最后使用Base64字符串表示
     *
     * @param src
     * @return
     */
    public static String sha256Base64(byte[] src) {
        return Base64Utils.encodeToString(sha256ToBytes(src));
    }

    /**
     *  SHA1 最后使用十六进制字符串表示
     *
     * @param src
     * @return
     */
    public static String sha1(String src) {
        return HexUtils.bytes2Hex(getDigest(SHA_1).digest(StringUtils.getBytes(src)));
    }

    /**
     *  hmacSHA256 最后使用Base64字符串表示
     *
     * @param src
     * @return
     */
    public static String hmacSha256(String src, String secret) {
        try {
            Mac hmacSha256 = Mac.getInstance(SHA_TYPE);
            byte[] keyBytes = secret.getBytes(BaseConstants.DEFAULT_CHARSET_NAME);
            hmacSha256.init(new SecretKeySpec(keyBytes, 0, keyBytes.length, SHA_TYPE));
            return Base64Utils.encodeToString(hmacSha256.doFinal(src.getBytes(BaseConstants.DEFAULT_CHARSET_NAME)));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (InvalidKeyException e) {
            throw new RuntimeException(e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }


}
