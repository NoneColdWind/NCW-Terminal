package cn.ncw.javafx.ncwjavafx.network.server;

import cn.ncw.javafx.ncwjavafx.core.key.PublicAndPrivate;
import cn.ncw.javafx.ncwjavafx.network.GetIP;
import cn.ncw.javafx.ncwjavafx.network.UnicodeConvert;
import cn.ncw.utils.algorithm.encrypt.aes.AESEncryption;
import cn.ncw.utils.algorithm.encrypt.rsa.RSAEncryption;
import cn.ncw.utils.hash.SHA3Utils;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.Objects;

import static cn.ncw.javafx.ncwjavafx.base.CommonVariable.*;

public class IPv6Server extends Thread {

    protected static Map<String, String> keyMap = PublicAndPrivate.getKeysMap();

    private final ServerSocket serverSocket;
    private final String IPv6Address = GetIP.getIPv6();
    private int port = 48848;

    public IPv6Server() throws IOException {
        this.serverSocket = new ServerSocket(this.port);
        this.serverSocket.setSoTimeout(24 * 60 * 60 * 1000);
    }

    public IPv6Server(int Port) throws IOException {
        this.port = Port;
        this.serverSocket = new ServerSocket(this.port);
        this.serverSocket.setSoTimeout(24 * 60 * 60 * 1000);
    }

    public String getIPv6Address() {
        return this.IPv6Address;
    }

    public int getPort() {
        return this.port;
    }

    @Override
    public void run() {
        while (true) {
            try {
                logger.info("Address: " + this.IPv6Address + ". Port: " + this.port, "IPv6Server");
                Socket server = serverSocket.accept();
                String remoteSocketAddress = server.getRemoteSocketAddress().toString();
                logger.info("RemoteSocketAddress: " + remoteSocketAddress, "IPv6Server");
                DataInputStream in = new DataInputStream(server.getInputStream());
                String data = in.readUTF();
                String[] notFinalData = data.split(":");
                long sendTime = Long.parseLong(notFinalData[0]);
                long receiveTime = System.currentTimeMillis();
                long delay = receiveTime - sendTime;
                String[] encryptMessage = notFinalData[1].replace("{", "").replace("}", "").split(",");
                String publicKey = encryptMessage[0];
                String rsaEncryptPassword = encryptMessage[1];
                String aesEncryptData = encryptMessage[2];
                String verifyHash = encryptMessage[3];
                String finalString;
                if (Objects.equals(SHA3Utils.sha3_512Hash(aesEncryptData), verifyHash)) {
                    String decryptData = AESEncryption.decrypt(aesEncryptData, RSAEncryption.decrypt(rsaEncryptPassword, keyMap.get(publicKey)));
                    finalString = UnicodeConvert.unicodeToString(decryptData);
                } else {
                    finalString = "";
                    logger.warn("Cannot pass the verification.", "IPv6Server");
                }
                LIST_MESSAGE.addLast(finalString);
                logger.info("Message: " + finalString, "IPv6Server");
                logger.info("Delay: " + delay + "ms", "IPv6Server");
                DataOutputStream out = new DataOutputStream(server.getOutputStream());
                out.writeUTF("Server IP: " + this.IPv6Address);
                server.close();
            } catch (Exception e) {
                logger.error("Error!" + e, "IPv6Server");
                break;
            }
        }
    }

    public static void main(String[] args) {
        while (true) {
            try {
                Thread thread = new IPv6Server(11451);
                thread.start();
                thread.join();
            } catch (IOException | InterruptedException e) {
                break;
            }
        }
    }
}
