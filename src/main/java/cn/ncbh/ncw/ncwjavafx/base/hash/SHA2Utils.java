package cn.ncbh.ncw.ncwjavafx.base.hash;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class SHA2Utils {

    public static String 抽象哈希2加密(String input) {
        String var1 = input;
        for (int i = 0; i < input.length(); i++) {
            var1 = sha256Hash(var1);
            var1 = sha384Hash(var1);
            var1 = sha512Hash(var1);
        }
        byte[] var2 = var1.getBytes();
        var1 = Base64.getEncoder().encodeToString(var2);
        for (int i = 0; i < var2.length; i++) {
            var1 = sha512Hash(var1);
            var1 = sha256Hash(var1);
            var1 = sha384Hash(var1);
            var1 = sha512Hash(var1);
        }
        var1+="114514";
        for (int i = 0; i < 6; i++) {
            var1 = sha256Hash(var1);
            var1 = sha512Hash(var1);
            var1 = sha384Hash(var1);
        }
        return var1;
    }

    public static String sha384Hash(String input) {
        // 处理空输入情况
        if (input == null) {
            return sha384Hash("");
        }

        try {
            // 1. 获取 SHA-384 消息摘要实例
            MessageDigest md = MessageDigest.getInstance("SHA-384");

            // 2. 明确指定 UTF-8 编码，避免平台差异问题
            byte[] inputBytes = input.getBytes(StandardCharsets.UTF_8);

            // 3. 计算哈希值
            byte[] hashBytes = md.digest(inputBytes);

            // 4. 将字节数组转换为十六进制字符串
            return bytesToHex(hashBytes);

        } catch (NoSuchAlgorithmException e) {
            // SHA-384 是标准算法，但为安全起见保留异常处理
            throw new RuntimeException("SHA-384 algorithm not available", e);
        }
    }

    public static String sha512Hash(String input) {
        // 处理空输入情况
        if (input == null) {
            return sha512Hash("");
        }

        try {
            // 1. 获取 SHA-512 消息摘要实例
            MessageDigest md = MessageDigest.getInstance("SHA-512");

            // 2. 明确指定 UTF-8 编码，避免平台差异问题
            byte[] inputBytes = input.getBytes(StandardCharsets.UTF_8);

            // 3. 计算哈希值
            byte[] hashBytes = md.digest(inputBytes);

            // 4. 将字节数组转换为十六进制字符串
            return bytesToHex(hashBytes);

        } catch (NoSuchAlgorithmException e) {
            // SHA-512 是标准算法，但为安全起见保留异常处理
            throw new RuntimeException("SHA-512 algorithm not available", e);
        }
    }

    public static String sha256Hash(String input) {
        // 处理空输入情况
        if (input == null) {
            return sha256Hash("");
        }

        try {
            // 1. 获取 SHA-256 消息摘要实例
            MessageDigest md = MessageDigest.getInstance("SHA-256");

            // 2. 明确指定 UTF-8 编码，避免平台差异问题
            byte[] inputBytes = input.getBytes(StandardCharsets.UTF_8);

            // 3. 计算哈希值
            byte[] hashBytes = md.digest(inputBytes);

            // 4. 将字节数组转换为十六进制字符串
            return bytesToHex(hashBytes);

        } catch (NoSuchAlgorithmException e) {
            // SHA-256 是标准算法，但为安全起见保留异常处理
            throw new RuntimeException("SHA-256 algorithm not available", e);
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
