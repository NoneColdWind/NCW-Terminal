package cn.ncbh.ncw.ncwjavafx.algorithm.encrypt.rsa;

import javax.crypto.Cipher;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class RSAEncryption {

    // 密钥算法
    private static final String KEY_ALGORITHM = "RSA";
    // 签名算法
    private static final String SIGNATURE_ALGORITHM = "SHA256withRSA";
    // 密钥长度
    private static final int KEY_SIZE = 2048;

    /**
     * 生成RSA密钥对
     * @return 包含公钥和私钥的Map
     */
    public static Map<String, String> generateKeyPair() throws Exception {
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(KEY_ALGORITHM);
        keyPairGen.initialize(KEY_SIZE);
        KeyPair keyPair = keyPairGen.generateKeyPair();

        Map<String, String> keyMap = new HashMap<>();
        keyMap.put("PUBLIC_KEY", Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded()));
        keyMap.put("PRIVATE_KEY", Base64.getEncoder().encodeToString(keyPair.getPrivate().getEncoded()));

        return keyMap;
    }

    /**
     * RSA公钥加密
     * @param data 待加密数据
     * @param publicKeyBase64 Base64编码的公钥
     * @return Base64编码的加密数据
     */
    public static String encrypt(String data, String publicKeyBase64) throws Exception {
        byte[] keyBytes = Base64.getDecoder().decode(publicKeyBase64);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        PublicKey publicKey = keyFactory.generatePublic(keySpec);

        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);

        byte[] encryptedBytes = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    /**
     * RSA私钥解密
     * @param encryptedDataBase64 Base64编码的加密数据
     * @param privateKeyBase64 Base64编码的私钥
     * @return 解密后的原始数据
     */
    public static String decrypt(String encryptedDataBase64, String privateKeyBase64) throws Exception {
        byte[] keyBytes = Base64.getDecoder().decode(privateKeyBase64);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        PrivateKey privateKey = keyFactory.generatePrivate(keySpec);

        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, privateKey);

        byte[] encryptedBytes = Base64.getDecoder().decode(encryptedDataBase64);
        byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
        return new String(decryptedBytes, StandardCharsets.UTF_8);
    }

    /**
     * 使用私钥生成数字签名
     * @param data 原始数据
     * @param privateKeyBase64 Base64编码的私钥
     * @return Base64编码的数字签名
     */
    public static String sign(String data, String privateKeyBase64) throws Exception {
        byte[] keyBytes = Base64.getDecoder().decode(privateKeyBase64);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        PrivateKey privateKey = keyFactory.generatePrivate(keySpec);

        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initSign(privateKey);
        signature.update(data.getBytes(StandardCharsets.UTF_8));

        byte[] signBytes = signature.sign();
        return Base64.getEncoder().encodeToString(signBytes);
    }

    /**
     * 使用公钥验证数字签名
     * @param data 原始数据
     * @param signBase64 Base64编码的数字签名
     * @param publicKeyBase64 Base64编码的公钥
     * @return 签名是否有效
     */
    public static boolean verify(String data, String signBase64, String publicKeyBase64) throws Exception {
        byte[] keyBytes = Base64.getDecoder().decode(publicKeyBase64);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        PublicKey publicKey = keyFactory.generatePublic(keySpec);

        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initVerify(publicKey);
        signature.update(data.getBytes(StandardCharsets.UTF_8));

        byte[] signBytes = Base64.getDecoder().decode(signBase64);
        return signature.verify(signBytes);
    }

    public static void main(String[] args) {
        try {
            // 1. 生成密钥对
            Map<String, String> keyPair = generateKeyPair();
            String publicKey = keyPair.get("PUBLIC_KEY");
            String privateKey = keyPair.get("PRIVATE_KEY");

            System.out.println("公钥: " + publicKey);
            System.out.println("私钥: " + privateKey);
            System.out.println();

            // 2. 加密解密示例
            String originalText = "这是一段需要加密的敏感数据";
            System.out.println("原始文本: " + originalText);

            String encryptedText = encrypt(originalText, publicKey);
            System.out.println("加密后: " + encryptedText);

            String decryptedText = decrypt(encryptedText, privateKey);
            System.out.println("解密后: " + decryptedText);
            System.out.println();

            // 3. 签名验证示例
            String dataToSign = "需要签名的数据";
            System.out.println("待签名数据: " + dataToSign);

            String signature = sign(dataToSign, privateKey);
            System.out.println("数字签名: " + signature);

            boolean isValid = verify(dataToSign, signature, publicKey);
            System.out.println("签名验证结果: " + (isValid ? "有效" : "无效"));

            // 4. 篡改数据验证
            String tamperedData = dataToSign + " (已篡改)";
            boolean isTamperedValid = verify(tamperedData, signature, publicKey);
            System.out.println("篡改数据验证结果: " + (isTamperedValid ? "有效" : "无效"));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
