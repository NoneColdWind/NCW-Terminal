package cn.ncbh.ncw.ncwjavafx.base.hash;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import static cn.ncbh.ncw.ncwjavafx.base.hash.FileMD5Hasher.calculateFileMD5;

public class SHA256Utils {

    /**
     * 将字符串进行 SHA-256 哈希
     *
     * @param input 原始字符串
     * @return 64位小写十六进制格式的SHA-256哈希值
     */
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

    /**
     * 将字符串进行 SHA-256 哈希并返回 Base64 格式
     *
     * @param input 原始字符串
     * @return Base64 编码的 SHA-256 哈希值
     */
    public static String sha256Base64(String input) {
        if (input == null) {
            return sha256Base64("");
        }

        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] inputBytes = input.getBytes(StandardCharsets.UTF_8);
            byte[] hashBytes = md.digest(inputBytes);

            // 返回Base64编码结果
            return Base64.getEncoder().encodeToString(hashBytes);

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not available", e);
        }
    }

    /**
     * 带盐值的增强哈希方法（更安全的密码存储）
     *
     * @param input 原始字符串
     * @param salt 盐值
     * @param iterations 迭代次数
     * @return 64位小写十六进制格式的哈希值
     */
    public static String sha256WithSalt(String input, String salt, int iterations) {
        if (input == null) input = "";
        if (salt == null) salt = "";

        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");

            // 初始化盐值处理
            byte[] saltBytes = salt.getBytes(StandardCharsets.UTF_8);

            // 组合输入和盐值
            String combined = salt + input;
            byte[] hash = combined.getBytes(StandardCharsets.UTF_8);

            // 多次迭代增加安全性
            for (int i = 0; i < iterations; i++) {
                md.update(saltBytes);
                hash = md.digest(hash);
            }

            return bytesToHex(hash);

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not available", e);
        }
    }

    /**
     * 将字节数组转换为十六进制字符串（优化版）
     *
     * @param bytes 字节数组
     * @return 十六进制字符串
     */
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

    public static void main(String[] args) throws IOException, NoSuchAlgorithmException {
        System.out.println(sha256Hash(calculateFileMD5("check\\ncw.json")));
    }
}
