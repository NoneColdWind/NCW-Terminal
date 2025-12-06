package cn.ncbh.ncw.ncwjavafx.algorithm.encrypt.aes;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.util.Base64;

public class AESEncryption {

    private static final String ALGORITHM = "AES/CBC/PKCS5Padding";
    private static final int ITERATION_COUNT = 65536;
    private static final int KEY_LENGTH = 256;
    private static final String KEYGEN_ALGORITHM = "PBKDF2WithHmacSHA256";

    // 加密方法
    public static String encrypt(String plainText, String password) throws Exception {
        // 生成随机盐 (16字节)
        byte[] salt = generateSalt();

        // 通过PBKDF2生成密钥
        SecretKeyFactory factory = SecretKeyFactory.getInstance(KEYGEN_ALGORITHM);
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, ITERATION_COUNT, KEY_LENGTH);
        SecretKey secretKey = new SecretKeySpec(factory.generateSecret(spec).getEncoded(), "AES");

        // 初始化加密器
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);

        // 获取IV并执行加密
        byte[] iv = cipher.getIV();
        byte[] encrypted = cipher.doFinal(plainText.getBytes());

        // 组合输出: Salt + IV + 加密数据
        byte[] combined = new byte[salt.length + iv.length + encrypted.length];
        System.arraycopy(salt, 0, combined, 0, salt.length);
        System.arraycopy(iv, 0, combined, salt.length, iv.length);
        System.arraycopy(encrypted, 0, combined, salt.length + iv.length, encrypted.length);

        return Base64.getEncoder().encodeToString(combined);
    }

    // 解密方法
    public static String decrypt(String cipherText, String password) throws Exception {
        byte[] combined = Base64.getDecoder().decode(cipherText);

        // 分离Salt (前16字节)
        byte[] salt = new byte[16];
        System.arraycopy(combined, 0, salt, 0, 16);

        // 分离IV (后续16字节)
        byte[] iv = new byte[16];
        System.arraycopy(combined, 16, iv, 0, 16);

        // 分离加密数据
        byte[] encrypted = new byte[combined.length - 32];
        System.arraycopy(combined, 32, encrypted, 0, encrypted.length);

        // 重新生成密钥
        SecretKeyFactory factory = SecretKeyFactory.getInstance(KEYGEN_ALGORITHM);
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, ITERATION_COUNT, KEY_LENGTH);
        SecretKey secretKey = new SecretKeySpec(factory.generateSecret(spec).getEncoded(), "AES");

        // 初始化解密器
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(iv));

        byte[] decrypted = cipher.doFinal(encrypted);
        return new String(decrypted);
    }

    // 生成随机盐值
    private static byte[] generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return salt;
    }

    // 示例使用
    public static void main(String[] args) throws Exception {
        String password = "MyStrongPassword!123";
        String originalText = "Hello, AES Encryption! 你好，加密世界！";

        // 加密
        String encrypted = encrypt(originalText, password);
        System.out.println("加密结果: " + encrypted);

        // 解密
        String decrypted = decrypt(encrypted, password);
        System.out.println("解密结果: " + decrypted);
    }
}
