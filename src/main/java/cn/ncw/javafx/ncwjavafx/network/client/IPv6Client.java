package cn.ncw.javafx.ncwjavafx.network.client;

import cn.ncw.javafx.ncwjavafx.base.PyRandom;
import cn.ncw.javafx.ncwjavafx.core.key.PublicAndPrivate;
import cn.ncw.javafx.ncwjavafx.network.UnicodeConvert;
import cn.ncw.utils.algorithm.encrypt.aes.AESEncryption;
import cn.ncw.utils.algorithm.encrypt.rsa.RSAEncryption;
import cn.ncw.utils.hash.SHA3Utils;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static cn.ncw.javafx.ncwjavafx.base.CommonVariable.logger;

public class IPv6Client extends Thread {

    public static List<String> publicKeyList = PublicAndPrivate.getPublicKeys();

    protected static Map<Integer, String> passwordMap = new HashMap<>();

    public static List<Character> listCharacter = Arrays.asList('1', '2', '3', '4', '5', '6', '7', '8', '9', '0', 'q', 'w', 'e', 'r', 't', 'y', 'u', 'i', 'o', 'p', 'a', 's', 'd', 'f', 'g', 'h', 'j', 'k', 'l', 'z', 'x', 'c', 'V', 'b', 'n', 'm', 'Q', 'W','E', 'R', 'T', 'Y', 'U', 'I', 'O', 'P', 'A', 'S', 'D', 'F', 'G', 'H', 'J', 'K', 'L', 'Z', 'X', 'C', 'V', 'B', 'N', 'M', '!', '@', '#', '$', '%', '^', '&', '*', '(', ')', '-', '_', '+', '=', '~', '`', '[', ']', '{', '}', '|', '\\', ':', ';', '\'', '\"', ',', '<', '>', '.', '/', '?');

    protected static String password;

    static {
        passwordMap.put(0, "NCWTerminalDefaultPassword");
        passwordMap.put(1, "PasswordByNoneColdWind");
        passwordMap.put(2, "PasswordByJustNothing");
        passwordMap.put(3, "PasswordByNAN");
        passwordMap.put(4, "PasswordByYunMo");
        passwordMap.put(5, "PasswordByDYD");
        passwordMap.put(6, "114514");
        passwordMap.put(7, "1919810");
        passwordMap.put(8, "Ciallo～(∠・ω< )⌒★");
        passwordMap.put(9, "zako~");
        passwordMap.put(10, "Oooo0ooO0oo00ooooOOO0000ooo0");
    }

    static {
        if (PyRandom.randint(0, 100) > 50) {
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < PyRandom.randint(16, 114); i++) {
                stringBuilder.append(listCharacter.get(PyRandom.randint(0, listCharacter.size() - 1)));
            }
            password = stringBuilder.toString();
        } else {
            password = passwordMap.get(PyRandom.randint(0, 10));
        }
    }

    private final String IPv6Address;
    private int Port = 48848;
    private String Message = "";
    protected final String publicKey = PyRandom.choose(publicKeyList);

    public IPv6Client(String address, String message) {
        this.IPv6Address = address;
        String var = UnicodeConvert.stringToUnicode(message);
        try {
            String aesEncryptData = AESEncryption.encrypt(var, password);
            String rsaEncryptPassword = RSAEncryption.encrypt(password, publicKey);
            String verifyHash = SHA3Utils.sha3_512Hash(aesEncryptData);
            this.Message = ":{" + publicKey + "," + rsaEncryptPassword + "," + aesEncryptData + "," + verifyHash + "}";
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public IPv6Client(String address, int port, String message) {
        this.IPv6Address = address;
        this.Port = port;
        String var = UnicodeConvert.stringToUnicode(message);
        try {
            String aesEncryptData = AESEncryption.encrypt(var, password);
            String rsaEncryptPassword = RSAEncryption.encrypt(password, publicKey);
            String verifyHash = SHA3Utils.sha3_512Hash(aesEncryptData);
            this.Message = ":{" + publicKey + "," + rsaEncryptPassword + "," + aesEncryptData + "," + verifyHash + "}";
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getIPv6Address() {
        return this.IPv6Address;
    }

    public int getPort() {
        return this.Port;
    }

    @Override
    public void run() {
        try {
            Socket client = new Socket(this.IPv6Address, this.Port);
            logger.info("Connected to server: " + this.IPv6Address + ", port: " + this.Port, "IPv6Client");

            OutputStream outToServer = client.getOutputStream();
            DataOutputStream out = new DataOutputStream(outToServer);
            out.writeUTF(System.currentTimeMillis() + this.Message);
            DataInputStream in = new DataInputStream(client.getInputStream());
            String response = in.readUTF();
            logger.info(response, "IPv6Client");
            client.close();
        } catch (IOException e) {
            logger.error("Error!" + e, "IPv6Client");
        }
    }

    public static void main(String[] args) {
        try {
            Thread thread = new IPv6Client("2409:8a50:4ca:1a30:e132:a0be:b6de:63b7", 11451, "你好");
            thread.start();
            thread.join();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
