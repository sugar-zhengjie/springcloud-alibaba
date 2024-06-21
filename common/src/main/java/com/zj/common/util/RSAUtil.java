package com.zj.common.util;

import org.apache.commons.io.IOUtils;
import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class RSAUtil {

    /**
     * 字符集
     */
    public static String CHARSET = "UTF-8";

    /**
     * 生成密钥对
     * @param keyLength  密钥长度
     * @return KeyPair
     */
    public static KeyPair getKeyPair(int keyLength) {
        try {
            //默认:RSA/None/PKCS1Padding
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(keyLength);
            return keyPairGenerator.generateKeyPair();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("生成密钥对时遇到异常" +  e.getMessage());
        }
    }

    /**
     * 获取公钥
     */
    public static byte[] getPublicKey(KeyPair keyPair) {
        RSAPublicKey rsaPublicKey = (RSAPublicKey) keyPair.getPublic();
        return rsaPublicKey.getEncoded();
    }

    /**
     * 获取私钥
     */
    public static byte[] getPrivateKey(KeyPair keyPair) {
        RSAPrivateKey rsaPrivateKey = (RSAPrivateKey) keyPair.getPrivate();
        return rsaPrivateKey.getEncoded();
    }

    /**
     * 公钥字符串转PublicKey实例
     * @param publicKey 公钥字符串
     * @return          PublicKey
     * @throws Exception e
     */
    public static PublicKey getPublicKey(String publicKey) throws Exception {
        byte[] publicKeyBytes = Base64.getDecoder().decode(publicKey.getBytes());
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKeyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePublic(keySpec);
    }

    /**
     * 私钥字符串转PrivateKey实例
     * @param privateKey  私钥字符串
     * @return PrivateKey
     * @throws Exception e
     */
    public static PrivateKey getPrivateKey(String privateKey) throws Exception {
        byte[] privateKeyBytes = Base64.getDecoder().decode(privateKey.getBytes());
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePrivate(keySpec);
    }

    /**
     * 获取公钥字符串
     * @param keyPair KeyPair
     * @return  公钥字符串
     */
    public static String getPublicKeyString(KeyPair keyPair){
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();  // 得到公钥
        return new String(org.apache.commons.codec.binary.Base64.encodeBase64(publicKey.getEncoded()));
    }

    /**
     * 获取私钥字符串
     * @param keyPair  KeyPair
     * @return 私钥字符串
     */
    public static String getPrivateKeyString(KeyPair keyPair){
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();   // 得到私钥
        return new String(org.apache.commons.codec.binary.Base64.encodeBase64((privateKey.getEncoded())));
    }


    /**
     * 公钥加密
     * @param data        明文
     * @param publicKey   公钥
     * @return            密文
     */
    public static String publicEncrypt(String data, RSAPublicKey publicKey) {
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            byte[] bytes = rsaSplitCodec(cipher, Cipher.ENCRYPT_MODE, data.getBytes(CHARSET), publicKey.getModulus().bitLength());
            return new String(org.apache.commons.codec.binary.Base64.encodeBase64(bytes));
        } catch (Exception e) {
            throw new RuntimeException("加密字符串[" + data + "]时遇到异常"+  e.getMessage());
        }
    }

    /**
     * 私钥解密
     * @param data        密文
     * @param privateKey  私钥
     * @return            明文
     */
    public static String privateDecrypt(String data, RSAPrivateKey privateKey) {
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            return new String(rsaSplitCodec(cipher, Cipher.DECRYPT_MODE, Base64.getDecoder().decode(data), privateKey.getModulus().bitLength()), CHARSET);
        } catch (Exception e) {
            throw new RuntimeException("privateKey解密字符串[" + data + "]时遇到异常"+  e.getMessage());
        }
    }


    /**
     * 私钥加密
     * @param content 明文
     * @param privateKey 私钥
     * @return 密文
     */
    public static String encryptByPrivateKey(String content, RSAPrivateKey privateKey){

        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, privateKey);
            byte[] bytes = rsaSplitCodec(cipher, Cipher.ENCRYPT_MODE,content.getBytes(CHARSET), privateKey.getModulus().bitLength());
            return new String(org.apache.commons.codec.binary.Base64.encodeBase64(bytes));
        } catch (Exception e) {
            throw new RuntimeException("privateKey加密字符串[" + content + "]时遇到异常" +  e.getMessage());
        }
    }

    /**
     * 公钥解密
     * @param content  密文
     * @param publicKey 私钥
     * @return  明文
     */
    public static String decryByPublicKey(String content, RSAPublicKey publicKey){
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, publicKey);
            return new String(rsaSplitCodec(cipher, Cipher.DECRYPT_MODE, Base64.getDecoder().decode(content), publicKey.getModulus().bitLength()), CHARSET);
        } catch (Exception e) {
            throw new RuntimeException("publicKey解密字符串[" + content + "]时遇到异常" +e.getMessage());
        }
    }

    public static RSAPublicKey getRSAPublicKeyByString(String publicKey){
        try {
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(Base64.getDecoder().decode(publicKey));
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return (RSAPublicKey)keyFactory.generatePublic(keySpec);
        } catch (Exception e) {
            throw new RuntimeException("String转PublicKey出错" + e.getMessage());
        }
    }


    public static RSAPrivateKey getRSAPrivateKeyByString(String privateKey){
        try {
            PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKey));
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return (RSAPrivateKey)keyFactory.generatePrivate(pkcs8EncodedKeySpec);
        } catch (Exception e) {
            throw new RuntimeException("String转PrivateKey出错" + e.getMessage());
        }
    }


    //rsa切割解码  , ENCRYPT_MODE,加密数据   ,DECRYPT_MODE,解密数据
    private static byte[] rsaSplitCodec(Cipher cipher, int opmode, byte[] datas, int keySize) {
        int maxBlock = 0;  //最大块
        if (opmode == Cipher.DECRYPT_MODE) {
            maxBlock = keySize / 8;
        } else {
            maxBlock = keySize / 8 - 11;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] buff;
        int i = 0;
        try {
            while (datas.length > offSet) {
                if (datas.length - offSet > maxBlock) {
                    //可以调用以下的doFinal（）方法完成加密或解密数据：
                    buff = cipher.doFinal(datas, offSet, maxBlock);
                } else {
                    buff = cipher.doFinal(datas, offSet, datas.length - offSet);
                }
                out.write(buff, 0, buff.length);
                i++;
                offSet = i * maxBlock;
            }
        } catch (Exception e) {
            throw new RuntimeException("加解密阀值为[" + maxBlock + "]的数据时发生异常: " + e.getMessage());
        }
        byte[] res = out.toByteArray();
        IOUtils.closeQuietly(out);
        return res;
    }
}
