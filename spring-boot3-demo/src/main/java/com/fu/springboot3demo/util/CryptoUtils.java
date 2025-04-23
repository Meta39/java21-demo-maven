package com.fu.springboot3demo.util;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * 加密：
 * 1.RSA非对称加密公钥用于加密AES对称密钥
 * 2.AES-GCM对称密钥用于加密数据
 * 3.ECDSA非对称加密公钥用于签名
 * 解密：
 * 1.ECDSA非对称加密私钥用于签名
 * 2.RSA非对称加密私钥用于解密AES对称密钥
 * 3.AES-GCM对称密钥用于解密数据
 */
public class CryptoUtils {
    // 算法常量
    private static final String AES = "AES";//算法名称
    private static final int GCM_IV_LENGTH = 12; //AES-GCM推荐12字节IV
    private static final int GCM_TAG_LENGTH = 128; //AES-GCM标签长度128位
    /**
     * AES-GCM(虽然要尽量避免使用 NoPadding，但是 AES-GCM 一定不会有 NoPadding)
     */
    private static final String AES_ALGORITHM = "AES/GCM/NoPadding";
    private static final String RSA_ALGORITHM = "RSA/ECB/OAEPWithSHA-256AndMGF1Padding";//RSA
    private static final String SIGNATURE_ALGORITHM = "SHA256withECDSA";//ECDSA签名
    private static final Base64.Encoder BASE64_ENCODER = Base64.getEncoder();
    private static final Base64.Decoder BASE64_DECODER = Base64.getDecoder();
    /**
     * 默认推荐全局静态常量：平衡性能与资源管理，适合多数场景。
     * 高并发场景优化：使用 ThreadLocal 为每个线程分配独立实例。
     * 避免频繁初始化：SecureRandom 的初始化成本较高，尽量减少重复创建。
     */
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    //RSA、ECC、AES的密钥工厂及生成器
    private static final KeyFactory RSA_KEY_FACTORY;
    private static final KeyPairGenerator RSA_KEY_PAIR_GEN;
    private static final KeyFactory EC_KEY_FACTORY;
    private static final KeyPairGenerator EC_KEY_PAIR_GEN;
    private static final KeyGenerator AES_KEY_GEN;

    static {
        try {
            //初始化RSA部分
            RSA_KEY_FACTORY = KeyFactory.getInstance("RSA");
            RSA_KEY_PAIR_GEN = KeyPairGenerator.getInstance("RSA");
            //SecureRandom.getInstanceStrong()使用安全随机数生成器，这种方法性能较低，因此只适用于次数少的场景。
            RSA_KEY_PAIR_GEN.initialize(2048, SecureRandom.getInstanceStrong());//1024密钥长度(位数)已经不安全，所以>=2048密钥长度(位数)【NIST建议】

            //初始化EC部分
            EC_KEY_FACTORY = KeyFactory.getInstance("EC");
            EC_KEY_PAIR_GEN = KeyPairGenerator.getInstance("EC");
            //直接指定曲线名称（如secp256r1）比指定位数更可靠，避免不同JDK实现映射差异。
            EC_KEY_PAIR_GEN.initialize(new ECGenParameterSpec("secp256r1"));//默认256密钥长度(位数)

            //初始化AEC部分
            AES_KEY_GEN = KeyGenerator.getInstance(AES);
            //SecureRandom.getInstanceStrong()使用安全随机数生成器，这种方法性能较低，因此只适用于次数少的场景。
            AES_KEY_GEN.init(256, SecureRandom.getInstanceStrong());//128密钥长度(位数)不安全，所以用256密钥长度(位数)
        } catch (Exception e) {
            throw new SecurityException("Cryptographic algorithm unavailable", e);
        }
    }

    // 生成RSA密钥对（保留用于加密）
    public static void generateRSAKeyPair() {
        KeyPair pair = RSA_KEY_PAIR_GEN.generateKeyPair();
        System.out.println("RSA Public Key: " + BASE64_ENCODER.encodeToString(pair.getPublic().getEncoded()));
        System.out.println("RSA Private Key: " + BASE64_ENCODER.encodeToString(pair.getPrivate().getEncoded()));
    }

    // 生成ECC密钥对（用于签名）
    public static void generateECKeyPair() {
        KeyPair pair = EC_KEY_PAIR_GEN.generateKeyPair();
        System.out.println("EC Public Key: " + BASE64_ENCODER.encodeToString(pair.getPublic().getEncoded()));
        System.out.println("EC Private Key: " + BASE64_ENCODER.encodeToString(pair.getPrivate().getEncoded()));
    }

    // 生成AES密钥
    public static String generateAESKey() {
        SecretKey secretKey = AES_KEY_GEN.generateKey();
        return BASE64_ENCODER.encodeToString(secretKey.getEncoded());
    }

    // 生成IV（GCM推荐12字节）
    public static String generateIv() {
        byte[] iv = new byte[GCM_IV_LENGTH];
        SECURE_RANDOM.nextBytes(iv);
        return BASE64_ENCODER.encodeToString(iv);
    }

    // AES加密（使用GCM模式）
    public static String aesEncrypt(String aesKeyStr, String ivStr, String json) {
        SecretKeySpec secretKey = new SecretKeySpec(BASE64_DECODER.decode(aesKeyStr), AES);
        byte[] ivBytes = BASE64_DECODER.decode(ivStr);
        try {
            Cipher cipher = Cipher.getInstance(AES_ALGORITHM);
            GCMParameterSpec parameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH, ivBytes);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, parameterSpec);
            byte[] cipherText = cipher.doFinal(json.getBytes(StandardCharsets.UTF_8));
            return BASE64_ENCODER.encodeToString(cipherText);
        } catch (Exception e) {
            throw new SecurityException("AES encryption failed", e);
        }
    }

    // RSA加密AES密钥（保留原有实现）
    public static String rsaEncrypt(String publicKeyStr, String aesKey) {
        try {
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(BASE64_DECODER.decode(publicKeyStr));
            PublicKey publicKey = RSA_KEY_FACTORY.generatePublic(keySpec);

            Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            return BASE64_ENCODER.encodeToString(cipher.doFinal(BASE64_DECODER.decode(aesKey)));
        } catch (Exception e) {
            throw new SecurityException("RSA encryption failed", e);
        }
    }

    // 使用ECDSA生成数字签名
    public static String signData(String privateKeyStr, String data) {
        try {
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(BASE64_DECODER.decode(privateKeyStr));
            PrivateKey privateKey = EC_KEY_FACTORY.generatePrivate(keySpec);

            Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
            signature.initSign(privateKey);
            signature.update(data.getBytes(StandardCharsets.UTF_8));
            return BASE64_ENCODER.encodeToString(signature.sign());
        } catch (Exception e) {
            throw new SecurityException("Signing failed", e);
        }
    }

    // 使用ECDSA验证签名
    public static boolean verifySignature(String publicKeyStr, String data, String signature) {
        try {
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(BASE64_DECODER.decode(publicKeyStr));
            PublicKey publicKey = EC_KEY_FACTORY.generatePublic(keySpec);

            Signature sig = Signature.getInstance(SIGNATURE_ALGORITHM);
            sig.initVerify(publicKey);
            sig.update(data.getBytes(StandardCharsets.UTF_8));
            return sig.verify(BASE64_DECODER.decode(signature));
        } catch (Exception e) {
            throw new SecurityException("Signature verification failed", e);
        }
    }

    // RSA解密AES密钥（保留原有实现）
    public static String rsaDecrypt(String privateKeyStr, String encryptedAesKey) {
        try {
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(BASE64_DECODER.decode(privateKeyStr));
            PrivateKey privateKey = RSA_KEY_FACTORY.generatePrivate(keySpec);

            Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            return BASE64_ENCODER.encodeToString(cipher.doFinal(BASE64_DECODER.decode(encryptedAesKey)));
        } catch (Exception e) {
            throw new SecurityException("RSA decryption failed", e);
        }
    }

    // AES解密（使用GCM模式）
    public static String aesDecrypt(String aesKeyStr, String ivStr, String cipherText) {
        try {
            SecretKeySpec keySpec = new SecretKeySpec(BASE64_DECODER.decode(aesKeyStr), AES);
            byte[] ivBytes = BASE64_DECODER.decode(ivStr);

            Cipher cipher = Cipher.getInstance(AES_ALGORITHM);
            GCMParameterSpec parameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH, ivBytes);
            cipher.init(Cipher.DECRYPT_MODE, keySpec, parameterSpec);
            return new String(cipher.doFinal(BASE64_DECODER.decode(cipherText)), StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new SecurityException("AES decryption failed", e);
        }
    }

}