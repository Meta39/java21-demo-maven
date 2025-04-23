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
    private static final String rsaPublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAoChSVQX89qZ8eEc8/nxgSJvebExnCgGxrvxuolsYSIATs9SlKd7hOZCAT3xQEFKiHECOeZUTt5b9812uEk9a/aSyZN2hnX3gM+uZxZi4E0N5ezaM9fdnRakOuE/vaSEEQntj3p/Maz7gNXb+XleK0fsfF9D+boWKCRDNGjAduBEJeQCjacD/DHUjfEqj1gjPoN5Dz2dokb7gQ5ZxEko20tnRkaGlzuE9RJtijShXobwzZ0Gz0BPu7mFvicwYRSIsXtXdcBv3E8+rzLEXVbpVEUnMVIqXQRY3ZDt3sRf1j8+1FoPZ6NlwrQt6Zsx0igc5NxXnmugz1bbzBjdled5kLQIDAQAB";
    private static final String rsaPrivateKey = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCgKFJVBfz2pnx4Rzz+fGBIm95sTGcKAbGu/G6iWxhIgBOz1KUp3uE5kIBPfFAQUqIcQI55lRO3lv3zXa4ST1r9pLJk3aGdfeAz65nFmLgTQ3l7Noz192dFqQ64T+9pIQRCe2Pen8xrPuA1dv5eV4rR+x8X0P5uhYoJEM0aMB24EQl5AKNpwP8MdSN8SqPWCM+g3kPPZ2iRvuBDlnESSjbS2dGRoaXO4T1Em2KNKFehvDNnQbPQE+7uYW+JzBhFIixe1d1wG/cTz6vMsRdVulURScxUipdBFjdkO3exF/WPz7UWg9no2XCtC3pmzHSKBzk3Feea6DPVtvMGN2V53mQtAgMBAAECggEAAfo9egGtUlMf7YRDqbvzaTIZ5V06ihJKe8FvxEVuEu1+ty7q+Y3GjiVnA2EJKnDUW5dYd88L0HqAZdIRGI/7EzxGj53ei8lGf9DRTaVJfV0AUcsmPNGlm5b7/YtoucDHz3vvWCWOkP+hWRRzc5eOuT6c70Ulvxhn0FDXl3kv0Y2Vsv7gCjE72KQWS8DwRU1e60h6mhLrI+a9HDBCSa+o+V8/U4pOF5xV08leP7aErbUU1m9XTE0YSA7Wzhl7hMVzwjl4RYtm0dOP18bM4mUAhP8bVd/+9XVQAcjNheMmE3H+PS6ZShiAmJ19A7g8AJv1McaelkEl+NWV6OzRqMbCwQKBgQDMsGR4Xsy5RLZ9PW9CyofmlErADsnE6sVPSgYKoysro6EfBK43gPxlJrIiwz5QUOSYdXhG+er14h5rbvX5MEJJKhiYzX6h3WHPCVKJPgpuAYic0DVL4nyYlx70rdta6MW5Z5PBGC+rZW6AeQrdiDnWAOEnenXh2ovUbZ6e75yzhQKBgQDITi+lEuc57LeFA7/pQeeYO7b7y4YY8/PODkOQNhDqNZrtpqEqoSbTRQPP9fZOXNA4RBxle88x4aQutOB+CU4RHhQshh5recG9VhWVLj9+qZ/tBf/+GMm//RGYTpJJWXH562oQPlEY00q2elT50x6w/pXSc8v4A2hCHsG3vFyqiQKBgB1Ja8axQbeMdlPrE11KHzHFrABY3VO4h6y1mbGx8P5Ydjg68jZs1TAf3kMNDoBtYpfRzvjQz2aJCPM0OCuEVSekx0ottN2yEEmQOuf6IYHl5EcTn1yRNmSa0soNiiIyxf+mK0TokgGDDNGDACP8VtIg5BZC9lTvcSxe1uBExnYFAoGANT67YYkF2Br5qa0WfZ3cP1ZYTNSMH+PxQiv+f74vfuKi/VtW6isOVAi7e7NpNulrHBnW5o8jw6G0cAn6xuYYT2qMeE1qlq019+uDLzWtATF75pi6tMPIFXXjEHaKYl8ZchLHDexSyGOaL6mTHxmkxe3Wy6umY5YcR78/RWUXyOkCgYEAtJuF8mLkLFf134F5Xn2FlNKNGqpkmDqwd4KOT3AoM7bqVRA+Qp8a487mFYe0lQOMeJtoRyiFdAOmnijMhqB6vIz2WKT9CxoyZWIb7a6m8CBiXXBDza0rxcOiafHvftcLyN+rDMuiSkp7btBq28ZGvBfjZPN2vAi+SnZteRHond0=";
    private static final String ecPublicKey = "MFkwEwYHKoZIzj0CAQYIKoZIzj0DAQcDQgAEqmkDEss9EJHdZZ7ScirhFLbt1RZUO/8PDK1Bm0PVZx60D23G/pTpmuCNuf6JlPJL7YM8LUsdK4s7w67KuGByUg==";
    private static final String ecPrivateKey = "MEECAQAwEwYHKoZIzj0CAQYIKoZIzj0DAQcEJzAlAgEBBCBfSyvAqhY3EEVGubh49r0R67zIcG9/STwqo5EycL76eg==";

    @Test
    public void generateRSAKeyPair() {
        CryptoUtils.generateRSAKeyPair();
    }

    @Test
    public void generateECKeyPair() {
        CryptoUtils.generateECKeyPair();
    }

    @Test
    public void encrypt() {
        // 1. 先获取生成RSA密钥对存储为常量
        // 2. AES-GCM加密数据
        Map<String,Object> dataMap = new HashMap<>();
        dataMap.put("name", "Meta39");
        dataMap.put("age", 18);
        dataMap.put("sex", "Man");
        String json = JacksonUtils.objectToJson(dataMap);

        //3.获取AES-GCM密钥
        String aesGcmKeyStr = CryptoUtils.generateAESKey();
        //4.获取AES-GCM IV
        String ivStr = CryptoUtils.generateIv();

        //5.用AES-GCM密钥对数据进行加密
        String cipherText = CryptoUtils.aesEncrypt(aesGcmKeyStr, ivStr, json);

        //6.RSA加密AES密钥
        String encryptedAesKey = CryptoUtils.rsaEncrypt(rsaPublicKey, aesGcmKeyStr);

        //7.构建签名用payload（不含签名字段）
        TreeMap<String, String> payloadTreeMap = new TreeMap<>();
        payloadTreeMap.put("timestamp", String.valueOf(Instant.now().getEpochSecond()));
        payloadTreeMap.put("encryptedKey", encryptedAesKey);
        payloadTreeMap.put("iv", ivStr);
        payloadTreeMap.put("cipherText", cipherText);

        //8.生成数字签名（自动按字段名排序）
        String signingData = JacksonUtils.objectToJson(payloadTreeMap);//签名数据
        String signature = CryptoUtils.signData(ecPrivateKey, signingData);//EC私钥签名

        //9.构建最终传输payload（含签名字段【签名算法之后再把签名数据放入】）
        payloadTreeMap.put("signature", signature);

        //10.加密数据转为JSON
        String encryptedData = JacksonUtils.objectToJson(payloadTreeMap);
        log.info("encryptedData2:{}", encryptedData);
    }

    @Test
    public void decrypt() {
        String encryptedData = "{\"cipherText\":\"kn5B+Eu6Q/yjzRpv9AOSJDIzpJF8S3g66o19weo8TJVGOvToHiKhcoImzvoyIhOOfuz6li9t\",\"encryptedKey\":\"TeEqErRmVWrttrBm4AjHGcm/99oPmSCP8vZ2b8xGeNLXhzpEXNK36b4yAYxjmFtpPGao8t5JYqCllJiWAPG30nBuVh9UxPBTwElK2x7Wrp/28ILZFO6atfD/F7Q6o6/d9rIGgF3gNOzlXub/5rOnU2RRxOs5LTeA1OpaQMjaICyq4y2ZUeIHOVu+wCefaWwlxuQVxdfyw9VgpPVtimHIIYhMm3ADQehvNnbYZca9wHDaS/gJBZTPge9hhfAWv9AcqnaytiSxMkVAdI/myTFouLC9SdGVBh2C0B7RQskRBauKVgYdap7fp/QarWWY1k5iKD5W6l3iy2wcip4z1hS5uQ==\",\"iv\":\"25THEWX4W8B72cNA\",\"signature\":\"MEYCIQDcfYLIeGvTlMnvyCg7IKxvc8qJWOzVpFzJe6K7yhAdBQIhAMg/tRHpbKIwfhFOHzcvAlz7RifsVZT/lvsnM+yM9YUE\",\"timestamp\":\"1745412495\"}";
        //1.解析接收数据（必须为TreeMap，防止不按顺序导致解析失败。）
        TreeMap<String, String> receivedData = JacksonUtils.jsonToObject(encryptedData, new TypeReference<>() {
        });

        //2.从treeMap移除key并获取被移除的key的内容（即：只有签名）
        String receivedSignature = receivedData.remove("signature");
        //3.把移除key内容之后从treeMap转为JSON字符串（即：不包含签名的TreeMap）
        String verificationData = JacksonUtils.objectToJson(receivedData);

        //4.验证EC签名
        boolean valid = CryptoUtils.verifySignature(ecPublicKey, verificationData, receivedSignature);

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
