package com.zj.util;

import com.zj.constant.BaseConstants;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zhengjie
 * @version 1.0
 * @date 2023/11/9 14:25
 */
public class AesUtils {

    /**
     * @author chen
     */
    public enum EncodeType {
        /**
         * base64
         */
        Base64,
        /**
         * hex
         */
        Hex
    }

    private static final int KEY_SIZE = 128;
    private static final String KEY_ALGORITHM = "AES";
    private static final String CIPHER_ALGORITHM = "AES/ECB/PKCS5Padding";
    private static final String RANDOM_TYPE = "SHA1PRNG";

    private static final Map<EncodeType, Encoder> ENCODE_TYPE_FUNCTION_MAP = new HashMap<EncodeType, Encoder>();

    static {
        ENCODE_TYPE_FUNCTION_MAP.put(EncodeType.Base64, new Encoder() {
            @Override
            public String encode(byte[] bytes) {
                return Base64Utils.encodeToString(bytes);
            }
        });
        ENCODE_TYPE_FUNCTION_MAP.put(EncodeType.Hex, new Encoder() {
            @Override
            public String encode(byte[] bytes) {
                return HexUtils.bytes2Hex(bytes);
            }
        });
    }

    private interface Encoder {
        /**
         * encode
         * @param bytes
         * @return
         */
        String encode(byte[] bytes);
    }

    private AesUtils() {
    }

    public static byte[] encrypt(byte[] content, Key key) {
        Cipher cipher = null;
        try {
            cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return cipher.doFinal(content);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (NoSuchPaddingException e) {
            throw new RuntimeException(e);
        } catch (BadPaddingException e) {
            throw new RuntimeException(e);
        } catch (IllegalBlockSizeException e) {
            throw new RuntimeException(e);
        } catch (InvalidKeyException e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[] encrypt(byte[] content, byte[] keyBytes) {
        return encrypt(content, new SecretKeySpec(keyBytes, KEY_ALGORITHM));
    }

    public static String encryptString(byte[] content, EncodeType encodeType, Key key) {
        return ENCODE_TYPE_FUNCTION_MAP.get(encodeType).encode(encrypt(content, key));
    }

    public static String encryptString(byte[] content, EncodeType encodeType, byte[] keyBytes) {
        return ENCODE_TYPE_FUNCTION_MAP.get(encodeType).encode(
                encrypt(content, new SecretKeySpec(keyBytes, KEY_ALGORITHM)));
    }

    public static byte[] decrypt(byte[] ciphertext, Key key) {
        Cipher cipher = null;
        try {
            cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, key);
            return cipher.doFinal(ciphertext);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (NoSuchPaddingException e) {
            throw new RuntimeException(e);
        } catch (BadPaddingException e) {
            throw new RuntimeException(e);
        } catch (IllegalBlockSizeException e) {
            throw new RuntimeException(e);
        } catch (InvalidKeyException e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[] decrypt(byte[] ciphertext, byte[] keyBytes) {
        return decrypt(ciphertext, new SecretKeySpec(keyBytes, KEY_ALGORITHM));
    }

    public static byte[] genRandomKeyBytes(String seed) throws NoSuchAlgorithmException {
        KeyGenerator keyGen = KeyGenerator.getInstance(KEY_ALGORITHM);
        SecureRandom random = SecureRandom.getInstance(RANDOM_TYPE);
        random.setSeed(getBytes(seed));
        keyGen.init(KEY_SIZE, random);
        return keyGen.generateKey().getEncoded();
    }

    private static byte[] getBytes(String seed) {
        try {
            return seed.getBytes(BaseConstants.DEFAULT_CHARSET_NAME);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public static Key genRandomKeyBySeed(String seed) throws NoSuchAlgorithmException {
        return new SecretKeySpec(genRandomKeyBytes(seed), KEY_ALGORITHM);
    }

    public static Key restoreKey(byte[] keyBytes) throws NoSuchAlgorithmException {
        return new SecretKeySpec(keyBytes, KEY_ALGORITHM);
    }

}
