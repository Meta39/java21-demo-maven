package com.fu.springboot3demo.util;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;


public class CryptoUtils {
    // 算法常量
    private static final String AES = "AES";//数据加密
    private static final String AES_ALGORITHM = "AES/CBC/PKCS5Padding";
    private static final String RSA_ALGORITHM = "RSA/ECB/PKCS1Padding";
    /**
     * SHA256withRSA 广泛支持，但计算较慢，适合签名次数少的场景。（推荐：EdDSA (Edwards-curve DSA)）
     */
    private static final String SHA256withRSA = "SHA256withRSA";//签名算法
    private static final int GCM_IV_LENGTH = 16;
    private static final Base64.Encoder BASE64_ENCODER = Base64.getEncoder();
    private static final Base64.Decoder BASE64_DECODER = Base64.getDecoder();
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    private static final KeyFactory RSA_KEY_FACTORY;
    private static final KeyPairGenerator RSA_KEY_PAIR_GEN;
    private static final KeyGenerator AES_KEY_GEN;

    static {
        String RSA = "RSA";
        try {
            RSA_KEY_FACTORY = KeyFactory.getInstance(RSA);
            RSA_KEY_PAIR_GEN = KeyPairGenerator.getInstance(RSA);
            RSA_KEY_PAIR_GEN.initialize(2048);

            AES_KEY_GEN = KeyGenerator.getInstance(AES);
            AES_KEY_GEN.init(256);
        } catch (Exception e) {
            throw new SecurityException("Cryptographic algorithm unavailable", e);
        }
    }

    // 生成RSA密钥对
    public static void generateRSAKeyPair() {
        KeyPair pair = RSA_KEY_PAIR_GEN.generateKeyPair();
        System.out.println("RSA Public Key: " + BASE64_ENCODER.encodeToString(pair.getPublic().getEncoded()));
        System.out.println("RSA Private Key: " + BASE64_ENCODER.encodeToString(pair.getPrivate().getEncoded()));
    }

    //生成AES密钥
    public static String generateAESKey() {
        SecretKey secretKey = AES_KEY_GEN.generateKey();
        return BASE64_ENCODER.encodeToString(secretKey.getEncoded());
    }

    //生成IV
    public static String generateIv() {
        byte[] iv = new byte[GCM_IV_LENGTH];
        SECURE_RANDOM.nextBytes(iv);
        return BASE64_ENCODER.encodeToString(iv);
    }

    // AES加密
    public static String aesEncrypt(String aesKeyStr, String ivStr,String json) {
        SecretKeySpec secretKey = new SecretKeySpec(BASE64_DECODER.decode(aesKeyStr), AES);
        IvParameterSpec iv = new IvParameterSpec(BASE64_DECODER.decode(ivStr));
        try {
            Cipher cipher = Cipher.getInstance(AES_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);
            byte[] cipherText = cipher.doFinal(json.getBytes(StandardCharsets.UTF_8));
            return BASE64_ENCODER.encodeToString(cipherText);
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

            Signature signature = Signature.getInstance(SHA256withRSA);
            signature.initSign(privateKey);
            signature.update(data.getBytes(StandardCharsets.UTF_8));
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

            Signature sig = Signature.getInstance(SHA256withRSA);
            sig.initVerify(publicKey);
            sig.update(data.getBytes(StandardCharsets.UTF_8));
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
            SecretKeySpec keySpec = new SecretKeySpec(BASE64_DECODER.decode(aesKeyStr), AES);
            IvParameterSpec iv = new IvParameterSpec(BASE64_DECODER.decode(ivStr));

            Cipher cipher = Cipher.getInstance(AES_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, keySpec, iv);
            return new String(cipher.doFinal(BASE64_DECODER.decode(cipherText)), StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new SecurityException("AES decryption failed", e);
        }
    }
}