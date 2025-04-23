package com.fu.springboot3demo.util;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class CryptoUtils {
    private static final Base64.Encoder BASE64_ENCODER = Base64.getEncoder();
    private static final Base64.Decoder BASE64_DECODER = Base64.getDecoder();
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    private static final KeyFactory RSA_KEY_FACTORY;
    private static final KeyPairGenerator RSA_KEY_PAIR_GEN;
    private static final KeyGenerator AES_KEY_GEN;

    static {
        try {
            RSA_KEY_FACTORY = KeyFactory.getInstance("RSA");
            RSA_KEY_PAIR_GEN = KeyPairGenerator.getInstance("RSA");
            RSA_KEY_PAIR_GEN.initialize(2048);

            AES_KEY_GEN = KeyGenerator.getInstance("AES");
            AES_KEY_GEN.init(256);
        } catch (Exception e) {
            throw new SecurityException("Cryptographic algorithm unavailable", e);
        }
    }
    

    // 算法常量
    private static final String AES_ALGORITHM = "AES/CBC/PKCS5Padding";
    private static final String RSA_ALGORITHM = "RSA/ECB/PKCS1Padding";
    private static final int GCM_IV_LENGTH = 16;

    // 生成RSA密钥对
    public static Map<String, String> generateRSAKeyPair() {
        try {
            KeyPair pair = RSA_KEY_PAIR_GEN.generateKeyPair();

            Map<String, String> keys = new HashMap<>();
            keys.put("public", BASE64_ENCODER.encodeToString(pair.getPublic().getEncoded()));
            keys.put("private", BASE64_ENCODER.encodeToString(pair.getPrivate().getEncoded()));
            return keys;
        } catch (Exception e) {
            throw new SecurityException("Key generation failed", e);
        }
    }

    // AES加密
    public static Map<String, String> aesEncrypt(String plainText) {
        try {
            SecretKey secretKey = AES_KEY_GEN.generateKey();

            byte[] iv = new byte[GCM_IV_LENGTH];
            SECURE_RANDOM.nextBytes(iv);

            Cipher cipher = Cipher.getInstance(AES_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, new IvParameterSpec(iv));
            byte[] cipherText = cipher.doFinal(plainText.getBytes("UTF-8"));

            Map<String, String> result = new HashMap<>();
            result.put("key", BASE64_ENCODER.encodeToString(secretKey.getEncoded()));
            result.put("iv", BASE64_ENCODER.encodeToString(iv));
            result.put("cipherText", BASE64_ENCODER.encodeToString(cipherText));
            return result;
        } catch (Exception e) {
            throw new SecurityException("AES encryption failed", e);
        }
    }

    // RSA加密AES密钥
    public static String rsaEncrypt(String publicKeyStr, String aesKey) {
        try {
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(BASE64_DECODER.decode(publicKeyStr));
            PublicKey publicKey = RSA_KEY_FACTORY.generatePublic(keySpec);

            Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            return BASE64_ENCODER.encodeToString(
                    cipher.doFinal(BASE64_DECODER.decode(aesKey))
            );
        } catch (Exception e) {
            throw new SecurityException("RSA encryption failed", e);
        }
    }

    // 生成数字签名
    public static String signData(String privateKeyStr, String data) {
        try {
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(BASE64_DECODER.decode(privateKeyStr));
            PrivateKey privateKey = RSA_KEY_FACTORY.generatePrivate(keySpec);

            Signature signature = Signature.getInstance("SHA256withRSA");
            signature.initSign(privateKey);
            signature.update(data.getBytes("UTF-8"));
            return BASE64_ENCODER.encodeToString(signature.sign());
        } catch (Exception e) {
            throw new SecurityException("Signing failed", e);
        }
    }

    // 验证签名
    public static boolean verifySignature(String publicKeyStr, String data, String signature) {
        try {
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(BASE64_DECODER.decode(publicKeyStr));
            PublicKey publicKey = RSA_KEY_FACTORY.generatePublic(keySpec);

            Signature sig = Signature.getInstance("SHA256withRSA");
            sig.initVerify(publicKey);
            sig.update(data.getBytes("UTF-8"));
            return sig.verify(BASE64_DECODER.decode(signature));
        } catch (Exception e) {
            throw new SecurityException("Signature verification failed", e);
        }
    }

    // RSA解密AES密钥
    public static String rsaDecrypt(String privateKeyStr, String encryptedAesKey) {
        try {
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(BASE64_DECODER.decode(privateKeyStr));
            PrivateKey privateKey = RSA_KEY_FACTORY.generatePrivate(keySpec);

            Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            return BASE64_ENCODER.encodeToString(
                    cipher.doFinal(BASE64_DECODER.decode(encryptedAesKey))
            );
        } catch (Exception e) {
            throw new SecurityException("RSA decryption failed", e);
        }
    }

    // AES解密
    public static String aesDecrypt(String aesKeyStr, String ivStr, String cipherText) {
        try {
            SecretKeySpec keySpec = new SecretKeySpec(BASE64_DECODER.decode(aesKeyStr), "AES");
            IvParameterSpec iv = new IvParameterSpec(BASE64_DECODER.decode(ivStr));

            Cipher cipher = Cipher.getInstance(AES_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, keySpec, iv);
            return new String(cipher.doFinal(BASE64_DECODER.decode(cipherText)), "UTF-8");
        } catch (Exception e) {
            throw new SecurityException("AES decryption failed", e);
        }
    }
}