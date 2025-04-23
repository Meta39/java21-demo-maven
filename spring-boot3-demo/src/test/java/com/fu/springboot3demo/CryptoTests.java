package com.fu.springboot3demo;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fu.springboot3demo.util.CryptoUtils;
import com.fu.springboot3demo.util.JacksonUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

@Slf4j
public class CryptoTests {
    private static final String rsaPublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAv6iHWPRog0rU03Uz1cjZCwJjXaAwgMFowCeEKTlO57Qr/5q6V829jCFBsBFT62b/tIYpVi3cXZAlMPePueBZ29PAncmXY32m2L8lrUv44P2iijVSeLBOtdCeG77m/Tp+Ff++APvp3uCbGC79wdgIVtgKjX39SWjQz+UluJsb02g+YO/CP6bNnJCXed9OQv4iNRfojCJR2HWKIRdHf27WHs4kPvW5EpuhMgou1jLQEuQoYwN5ZUZENjbWHwjAwVysxagZzNjehIRsApDWOvfBUiMh+091sehbiMT1T2qvwpcKpSepHxOVCKJTz9zppi3c7f8iLcGYSB9cvkx3hgsq8QIDAQAB";
    private static final String rsaPrivateKey = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQC/qIdY9GiDStTTdTPVyNkLAmNdoDCAwWjAJ4QpOU7ntCv/mrpXzb2MIUGwEVPrZv+0hilWLdxdkCUw94+54Fnb08CdyZdjfabYvyWtS/jg/aKKNVJ4sE610J4bvub9On4V/74A++ne4JsYLv3B2AhW2AqNff1JaNDP5SW4mxvTaD5g78I/ps2ckJd5305C/iI1F+iMIlHYdYohF0d/btYeziQ+9bkSm6EyCi7WMtAS5ChjA3llRkQ2NtYfCMDBXKzFqBnM2N6EhGwCkNY698FSIyH7T3Wx6FuIxPVPaq/ClwqlJ6kfE5UIolPP3OmmLdzt/yItwZhIH1y+THeGCyrxAgMBAAECggEAOVDeASU+aB0l3oMLWGCzP86cBrDUMH0mpWhr5qQjy7ke+1a7aWy2xcRgT5YvAZQQ3lzM2d/nKffkl2tBiTXlyY7e6JET/AXksqcfTl8M88QGKszdeAhJ0aes0OFbVNl8QXn0yXiMrUGAQFA1RRYlEEuVhFDsQVreL334tOd1/9eFPLbBJh01RSaIxsiiJ/7w/+1+1zggTlt5cQslRD3Kxr40YCa+QeRLJrqyARZKcbPB9RLZJ6AXI5wIlHos6czumcbGzQdgEs79pXw439xLas+UnX3+8tGe1QS693WtTnalbDF8ctGvuSmZJSG4PSlsWBiuaBxK8XO8MLmcugvknQKBgQDpfv9xhkU/Rh93+0X/ABi/WHiyvX9t19EmGQoQvWWyw9Qr1PR8XWG9XE02pDK9mB+Xg8HU/BLyiTYjviWc7Uro8l9qCGQLDhyCfJYtk/Pkbk3+zymMtZCItLqpM1AKfj8SLP6P9j7wcaOxINonfBAQeQJTXWs4ysTEMotJZui9pwKBgQDSIUakpgTJEoJxrQCd7MPAemPYz2uhyRRpgrv6Ws6Kz8awFfM0DXpm79MjlvpYGXrwwvreszFgOwxshE0eeSx4pReXq7b/CNrghHqOXuyfQzk89/89u9Ks2Vlccp4d1uYPL6Rhjrzc+t/vnyfIuR9VVrxhSuaqwW/JdOl1BqlVpwKBgQDCKaH+4/l3STdnzSICjzEFBGuxrlRlod5AgPfWvbfwtFkdbcM0G27oSPXIJzguYm56zsyC8G5NEn5bsIWUfdoZ5VokUP/oFX7tnu4+zlY0MOlYpynU4TJkC21gv9OiNiL6WUIBsnHZ/ZVx+HCP/uFY4VdmvTspCawfviwHCLUm5wKBgDwEsyfOH6nK9qU/c+CnwtXUX3aK8EQ6qfDlHYoJq9rsM+URr/CbNO337EfLFOBbDsl796nqZt0EoKzSMTDWDYS8KvvwWQmJXvMoA9VseYuX6N5oO2hHoIosXqDQQCdnvThzNDGFUp6PrazEudlUPwXiC1aCzVXSgHp7QT9i0rUbAoGBANQH4l1BBVWfO/hwrq77ujYwZ9dEs3864KLev1C/eSv0psJ6Uc5ZV8eIgp5s2gIbWGNsRWjClsvChJfoqOrNqrjf8xk5BU8q3U3QISoUBPBWkQBzbqzEG9mcjYMjsvXJwdD5p+EoBcOq3F8dEP6ORd8Y47XKDJ3URWZcH4QVpbvL";

    @Test
    public void generateRSAKeyPair() {
        CryptoUtils.generateRSAKeyPair();
    }

    @Test
    public void encrypt() {
        // 1. 先获取生成RSA密钥对存储为常量
        // 2. AES加密数据
        Map<String,Object> dataMap = new HashMap<>();
        dataMap.put("name", "Meta39");
        dataMap.put("age", 18);
        dataMap.put("sex", "Man");
        String json = JacksonUtils.objectToJson(dataMap);

        //3.获取AES密钥
        String aesKeyStr = CryptoUtils.generateAESKey();
        //4.获取AES IV
        String ivStr = CryptoUtils.generateIv();

        //5.用AES密钥对数据进行加密
        String cipherText = CryptoUtils.aesEncrypt(aesKeyStr, ivStr, json);

        //6.RSA加密AES密钥
        String encryptedAesKey = CryptoUtils.rsaEncrypt(rsaPublicKey, aesKeyStr);

        //7.构建签名用payload（不含签名字段）
        TreeMap<String, String> payloadTreeMap = new TreeMap<>();
        payloadTreeMap.put("timestamp", String.valueOf(Instant.now().getEpochSecond()));
        payloadTreeMap.put("encryptedKey", encryptedAesKey);
        payloadTreeMap.put("iv", ivStr);
        payloadTreeMap.put("cipherText", cipherText);

        //8.生成数字签名（自动按字段名排序）
        String signingData = JacksonUtils.objectToJson(payloadTreeMap);//签名数据
        String signature = CryptoUtils.signData(rsaPrivateKey, signingData);//签名算法加密签名数据

        //9.构建最终传输payload（含签名字段【签名算法之后再把签名数据放入】）
        payloadTreeMap.put("signature", signature);

        //10.加密数据转为JSON
        String encryptedData = JacksonUtils.objectToJson(payloadTreeMap);
        log.info("encryptedData:{}", encryptedData);
    }

    @Test
    public void decrypt() {
        String encryptedData = "{\"cipherText\":\"7cqXofQ9+sn1p26aLowceeaiAiYAw4zS004dgCmb7ePjxLYgLIcDDic7+K//E5+C\",\"encryptedKey\":\"UoWGVQslJ3T4XAwVAvtf5gM4IgH8ZpBBYMXkoDT9+/ZswnI/gknFYZtlOrF9f7kJFhbHnf8JwuGUfhnd3Q3TYOz7GuNuSU/CmQ6y/LK6qU2VQOThfZes7d1WhsIYJulDrLo7tNrLNneZeDqF+L7qbkNNYJuQFFprYJf83u/M7E8OuP8aoRbgfdkBkQ3o1+UEmphnJ4qu6FNmy7C5DyXFF6SoahGvI1BbuyEtzeSDW5kq3K7etIgOLxK2ITeN2O7gA7T8AII/xzL0GHjzSEX78OK7BV5j/z8Nw09M01SFpuqu0SWqxEzTOOX0VBWCuYBRYayS2Jie9VBRUzR7ULbR0g==\",\"iv\":\"xHVKJf/jYaDouW95sjoIQg==\",\"signature\":\"BgSEgjDWyT8ba7E/0DiuqCvM96VXwg8tCnl5/Y2VO9mJtnl6HuF2Q1r7CB9c5vnzwQtsOqG9TVLPbc5FX6Dn8Ky1Rz5Zalg8p3auq5k2pmIrIrACTc/o1BUcLpNIKgabra7o10YLr1WgNcfCLxYxqXqdo9xKMPBNNzBCf+gnbo0Y77kOnUGVM8kIoy3hQvqI2GW+1O4kJQP+tCmakqHYISsqjAWZdH3FtagK/9pGA06G6bwE0edkEj8u++wi9/TOI43Cgm/L8JSauStCeSneI0I85OuIXVy3TjXcBUApEMFe+clZi31g8FyJWC88p8jMJ6QJKq87lt+8nNjNMLU3CQ==\",\"timestamp\":\"1745388880\"}";
        //1.解析接收数据（必须为TreeMap，防止不按顺序导致解析失败。）
        TreeMap<String, String> receivedData = JacksonUtils.jsonToObject(encryptedData, new TypeReference<>() {
        });

        //2.从treeMap移除key并获取被移除的key的内容（即：只有签名）
        String receivedSignature = receivedData.remove("signature");
        //3.把移除key内容之后从treeMap转为JSON字符串（即：不包含签名的TreeMap）
        String verificationData = JacksonUtils.objectToJson(receivedData);

        //4.验证签名
        boolean valid = CryptoUtils.verifySignature(rsaPublicKey, verificationData, receivedSignature);

        if (!valid) {
            throw new SecurityException("Invalid signature");
        }

        //5.验证有效期（示例：1年有效期）
        long timestamp = Long.parseLong(receivedData.get("timestamp"));
        if (Instant.now().getEpochSecond() - timestamp > 31536000) {
            throw new SecurityException("Data expired");
        }

        //6.RSA解密AES密钥
        String decryptedAesKey = CryptoUtils.rsaDecrypt(rsaPrivateKey, receivedData.get("encryptedKey"));

        //7. AES解密数据
        String decryptedText = CryptoUtils.aesDecrypt(decryptedAesKey, receivedData.get("iv"), receivedData.get("cipherText"));

        //8.原文JSON字符串转对象
        Map<String, Object> dataMap = JacksonUtils.jsonToObject(decryptedText, new TypeReference<>() {});

        log.info("decryptedData:{}", dataMap);
    }

}
