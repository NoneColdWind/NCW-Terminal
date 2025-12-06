package cn.ncbh.ncw.ncwjavafx.base.hash;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class FileSHA256Hasher {

    /**
     * 计算文件的SHA256哈希值
     *
     * @param filePath 文件路径
     * @return 32位小写十六进制格式的SHA256哈希值
     * @throws IOException 如果文件读取失败
     * @throws NoSuchAlgorithmException 如果SHA256算法不可用（在标准JDK中通常不会发生）
     */
    public static String calculateFileSHA256(String filePath) throws IOException, NoSuchAlgorithmException {
        return calculateFileSHA256(new File(filePath));
    }

    /**
     * 计算文件的SHA256哈希值
     *
     * @param file 要处理的文件对象
     * @return 32位小写十六进制格式的SHA256哈希值
     * @throws IOException 如果文件读取失败
     * @throws NoSuchAlgorithmException 如果SHA256算法不可用
     */
    public static String calculateFileSHA256(File file) throws IOException, NoSuchAlgorithmException {
        // 验证文件存在且可读
        if (!file.exists()) {
            throw new IOException("文件不存在: " + file.getAbsolutePath());
        }
        if (!file.isFile()) {
            throw new IOException("路径不是文件: " + file.getAbsolutePath());
        }
        if (!file.canRead()) {
            throw new IOException("文件不可读: " + file.getAbsolutePath());
        }

        // 创建MessageDigest实例
        MessageDigest md = MessageDigest.getInstance("SHA-256");

        // 使用try-with-resources确保流正确关闭
        try (FileInputStream fis = new FileInputStream(file)) {
            // 缓冲区大小（可根据性能调整）
            byte[] buffer = new byte[8192];  // 8KB缓冲区
            int bytesRead;

            // 分块读取文件并更新摘要
            while ((bytesRead = fis.read(buffer)) != -1) {
                md.update(buffer, 0, bytesRead);
            }
        }

        // 完成计算并获取摘要
        byte[] digestBytes = md.digest();

        // 将摘要转换为十六进制字符串
        return bytesToHex(digestBytes);
    }

    /**
     * 将字节数组转换为十六进制字符串
     *
     * @param bytes 字节数组
     * @return 十六进制字符串
     */
    private static String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder(32);
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

}
