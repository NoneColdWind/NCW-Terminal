package cn.ncbh.ncw.ncwjavafx.base.hash;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class SHA3Utils {

    public static String 抽象哈希3加密(String input) {
        String var1 = input + "114514";
        for (int i = 0; i < 114; i++) {
            var1 = sha3_256Hash(var1);
            var1 = sha3_384Hash(var1);
            var1 = sha3_512Hash(var1);
        }
        var1 = SHA2Utils.抽象哈希2加密(var1);
        for (int i = 0; i < 24; i++) {
            var1 = sha3_512Hash(var1);
            var1 = SHA2Utils.抽象哈希2加密(var1);
        }
        var1 = Base64.getEncoder().encodeToString(var1.getBytes());
        for (int i = 0; i < var1.length(); i++) {
            var1 = sha3_512Hash(sha3_384Hash(sha3_256Hash(sha3_512Hash(sha3_384Hash(sha3_256Hash(sha3_512Hash(sha3_384Hash(sha3_256Hash(sha3_512Hash(sha3_384Hash(sha3_256Hash(sha3_512Hash(sha3_384Hash(sha3_256Hash(sha3_512Hash(sha3_384Hash(sha3_256Hash(sha3_512Hash(sha3_384Hash(sha3_256Hash(var1)))))))))))))))))))));
        }
        return Base64.getEncoder().encodeToString(var1.getBytes());
    }

    public static String sha3_384Hash(String input) {
        // 处理空输入情况
        if (input == null) {
            return sha3_384Hash("");
        }

        try {
            // 1. 获取 SHA3-384 消息摘要实例
            MessageDigest md = MessageDigest.getInstance("SHA3-384");

            // 2. 明确指定 UTF-8 编码，避免平台差异问题
            byte[] inputBytes = input.getBytes(StandardCharsets.UTF_8);

            // 3. 计算哈希值
            byte[] hashBytes = md.digest(inputBytes);

            // 4. 将字节数组转换为十六进制字符串
            return bytesToHex(hashBytes);

        } catch (NoSuchAlgorithmException e) {
            // SHA3-384 是标准算法，但为安全起见保留异常处理
            throw new RuntimeException("SHA3-384 algorithm not available", e);
        }
    }

    public static String sha3_512Hash(String input) {
        // 处理空输入情况
        if (input == null) {
            return sha3_512Hash("");
        }

        try {
            // 1. 获取 SHA3-512 消息摘要实例
            MessageDigest md = MessageDigest.getInstance("SHA3-512");

            // 2. 明确指定 UTF-8 编码，避免平台差异问题
            byte[] inputBytes = input.getBytes(StandardCharsets.UTF_8);

            // 3. 计算哈希值
            byte[] hashBytes = md.digest(inputBytes);

            // 4. 将字节数组转换为十六进制字符串
            return bytesToHex(hashBytes);

        } catch (NoSuchAlgorithmException e) {
            // SHA3-512 是标准算法，但为安全起见保留异常处理
            throw new RuntimeException("SHA3-512 algorithm not available", e);
        }
    }

    public static String sha3_256Hash(String input) {
        // 处理空输入情况
        if (input == null) {
            return sha3_256Hash("");
        }

        try {
            // 1. 获取 SHA3-256 消息摘要实例
            MessageDigest md = MessageDigest.getInstance("SHA3-256");

            // 2. 明确指定 UTF-8 编码，避免平台差异问题
            byte[] inputBytes = input.getBytes(StandardCharsets.UTF_8);

            // 3. 计算哈希值
            byte[] hashBytes = md.digest(inputBytes);

            // 4. 将字节数组转换为十六进制字符串
            return bytesToHex(hashBytes);

        } catch (NoSuchAlgorithmException e) {
            // SHA3-256 是标准算法，但为安全起见保留异常处理
            throw new RuntimeException("SHA3-256 algorithm not available", e);
        }
    }

    private static String bytesToHex(byte[] bytes) {
        // 使用char[]更高效，避免StringBuilder的频繁扩容
        char[] hexChars = new char[bytes.length * 2];
        for (int i = 0; i < bytes.length; i++) {
            int value = bytes[i] & 0xFF;
            hexChars[i * 2] = "0123456789abcdef".charAt(value >>> 4);
            hexChars[i * 2 + 1] = "0123456789abcdef".charAt(value & 0x0F);
        }
        return new String(hexChars);
    }

}
