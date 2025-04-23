package com.fu.springboot3demo;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fu.springboot3demo.util.CryptoUtils;
import com.fu.springboot3demo.util.JacksonUtils;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Map;
import java.util.TreeMap;

public class CryptoTests {

    @Test
    public void encrypt() {
        try {
            // 1. 生成RSA密钥对
            Map<String, String> rsaKeys = CryptoUtils.generateRSAKeyPair();

            // 2. AES加密数据
            String plainText = "Sensitive data需要加密的内容";
            Map<String, String> aesResult = CryptoUtils.aesEncrypt(plainText);

            // 3. RSA加密AES密钥
            String encryptedAesKey = CryptoUtils.rsaEncrypt(
                    rsaKeys.get("public"),
                    aesResult.get("key")
            );

            // 4. 构建签名用payload（不含签名字段）
            // 使用TreeMap构建payload
            TreeMap<String, Object> payloadForSigning = new TreeMap<>();
            payloadForSigning.put("timestamp", Instant.now().getEpochSecond());
            payloadForSigning.put("encryptedKey", encryptedAesKey);
            payloadForSigning.put("iv", aesResult.get("iv"));
            payloadForSigning.put("cipherText", aesResult.get("cipherText"));

            // 生成数字签名（自动按字段名排序）
            String signingData = JacksonUtils.objectToJson(payloadForSigning);
            String signature = CryptoUtils.signData(rsaKeys.get("private"), signingData);

            // 构建最终传输payload
            TreeMap<String, Object> finalPayload = new TreeMap<>(payloadForSigning);
            finalPayload.put("signature", signature);
            String encryptedData = JacksonUtils.objectToJson(finalPayload);

            // ================= 解密流程 =================
            // 解析接收数据
            TreeMap<String, Object> receivedData = JacksonUtils.jsonToObject(encryptedData, new TypeReference<>() {});

            // 提取签名并创建验证用payload
            String receivedSignature = (String) receivedData.remove("signature");
            String verificationData = JacksonUtils.objectToJson(receivedData);

            // 验证签名
            boolean valid = CryptoUtils.verifySignature(rsaKeys.get("public"), verificationData, receivedSignature);

            if (!valid) throw new SecurityException("Invalid signature");

            // 3. 验证有效期（示例：1年有效期）
            long timestamp = Long.parseLong(receivedData.get("timestamp").toString());
            if (Instant.now().getEpochSecond() - timestamp > 31536000) {
                throw new SecurityException("Data expired");
            }

            // 4. RSA解密AES密钥
            String decryptedAesKey = CryptoUtils.rsaDecrypt(
                    rsaKeys.get("private"),
                    receivedData.get("encryptedKey").toString()
            );

            // 5. AES解密数据
            String decryptedText = CryptoUtils.aesDecrypt(
                    decryptedAesKey,
                    receivedData.get("iv").toString(),
                    receivedData.get("cipherText").toString()
            );

            System.out.println("Decrypted text: " + decryptedText);

        } catch (Exception e) {
            System.err.println("Error during encryption/decryption: " + e.getMessage());
            e.printStackTrace();
        }
    }

}
