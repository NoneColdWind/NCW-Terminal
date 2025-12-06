package cn.ncbh.ncw.ncwjavafx.network.server;

import cn.ncbh.ncw.ncwjavafx.algorithm.encrypt.aes.AESEncryption;
import cn.ncbh.ncw.ncwjavafx.algorithm.encrypt.rsa.RSAEncryption;
import cn.ncbh.ncw.ncwjavafx.base.hash.SHA3Utils;
import cn.ncbh.ncw.ncwjavafx.core.key.PublicAndPrivate;
import cn.ncbh.ncw.ncwjavafx.log.LEVEL;
import cn.ncbh.ncw.ncwjavafx.log.ThreadLogger;
import cn.ncbh.ncw.ncwjavafx.network.GetIP;
import cn.ncbh.ncw.ncwjavafx.network.UnicodeConvert;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.Objects;

import static cn.ncbh.ncw.ncwjavafx.base.CommonVariable.*;

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
                logger.log(LEVEL.INFO, "IPv6Server", "Address: " + this.IPv6Address + ". Port: " + this.port);
                threadLogger.log(ThreadLogger.LogLevel.INFO, "Address: " + this.IPv6Address + ". Port: " + this.port, "IPv6Server", null);
                Socket server = serverSocket.accept();
                String remoteSocketAddress = server.getRemoteSocketAddress().toString();
                logger.log(LEVEL.INFO, "IPv6Server", "RemoteSocketAddress: " + remoteSocketAddress);
                threadLogger.log(ThreadLogger.LogLevel.INFO, "RemoteSocketAddress: " + remoteSocketAddress, "IPv6Server", null);
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
                    logger.log(LEVEL.WARN, "IPv6Server", "Cannot pass the verification.");
                    threadLogger.log(ThreadLogger.LogLevel.WARN, "Cannot pass the verification.", "IPv6Server", null);
                }
                LIST_MESSAGE.addLast(finalString);
                logger.log(LEVEL.INFO, "IPv6Server", "Message: " + finalString);
                threadLogger.log(ThreadLogger.LogLevel.INFO, "Message: " + finalString, "IPv6Server", null);
                logger.log(LEVEL.INFO, "IPv6Server", "Delay: " + delay + "ms");
                threadLogger.log(ThreadLogger.LogLevel.INFO, "Delay: " + delay + "ms", "IPv6Server", null);
                DataOutputStream out = new DataOutputStream(server.getOutputStream());
                out.writeUTF("Server IP: " + this.IPv6Address);
                server.close();
            } catch (Exception e) {
                logger.log(LEVEL.INFO, "IPv6Server", "Error!" + e);
                threadLogger.log(ThreadLogger.LogLevel.INFO, "Error!", "IPv6Server", e);
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
