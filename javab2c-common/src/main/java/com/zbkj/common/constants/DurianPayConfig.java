package com.zbkj.common.constants;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 支付宝配置
 */
public class DurianPayConfig {

    public static final String MERCHANT_CODE = "durian_merchant_code";
    // 私钥
    public static final String SECRET_KEY = "durian_secret_key";
    public static final String DURIAN_URL = "durian_url";
    // 货币代码
    public static final String CURRENCY = "MYR";
    public static MessageDigest digest;

    static {
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public static String getSignature(String... args) {
        String preHash = String.join("", args);
        //String preHash = "KRE888bb841904-4eea-44e5-8ca3-0af1e304ff4fT1234";
        byte[] hash = digest.digest(preHash.getBytes(StandardCharsets.UTF_8));
        return String.format("%064x", new BigInteger(1, hash));
    }
}
