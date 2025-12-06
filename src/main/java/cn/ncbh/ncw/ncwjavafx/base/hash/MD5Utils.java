package cn.ncbh.ncw.ncwjavafx.base.hash;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Utils {

    /**
     * 将字符串进行 MD5 加密
     * @param input 原始字符串
     * @return 32位小写十六进制格式的MD5哈希值，如果加密失败返回null
     */
    public static String md5Hash(String input) {
        if (input == null || input.isEmpty()) {
            return null;
        }

        try {
            // 1. 获取MD5实例（使用单例模式推荐写法）
            MessageDigest md = MessageDigest.getInstance("MD5");

            // 2. 明确指定UTF-8编码，避免平台差异问题
            byte[] inputBytes = input.getBytes(StandardCharsets.UTF_8);

            // 3. 计算哈希值
            byte[] hashBytes = md.digest(inputBytes);

            // 4. 将字节数组转换为十六进制字符串
            return bytesToHex(hashBytes);

        } catch (NoSuchAlgorithmException e) {
            // 实际MD5算法在标准JDK中总是可用，但为安全起见保留异常处理
            throw new RuntimeException("MD5 algorithm not available", e);
        }
    }

    /**
     * 将字节数组转换为十六进制字符串
     * @param hash 字节数组形式的哈希值
     * @return 32位小写十六进制字符串
     */
    private static String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder(32);  // 预设长度优化性能
        for (byte b : hash) {
            // 使用位运算确保无符号转换
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');  // 补零
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
