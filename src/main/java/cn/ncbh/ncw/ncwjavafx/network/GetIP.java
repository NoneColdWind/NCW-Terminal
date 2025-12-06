package cn.ncbh.ncw.ncwjavafx.network;

import cn.ncbh.ncw.ncwjavafx.log.LEVEL;
import cn.ncbh.ncw.ncwjavafx.log.ThreadLogger;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;

import static cn.ncbh.ncw.ncwjavafx.base.CommonVariable.logger;
import static cn.ncbh.ncw.ncwjavafx.base.CommonVariable.threadLogger;


public class GetIP {

    public static String getIPv6() {
        return getPublicIPv6();
    }

    private static String getPublicIPv6() {
        String ipv6 = "";
        try {
            URL url = URI.create("https://v6.ident.me").toURL();

            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(url.openStream()))) {

                ipv6 = reader.readLine();
            }
        } catch (Exception e) {
            logger.log(LEVEL.ERROR, "getIPv6", "Error!" + e);
            threadLogger.log(ThreadLogger.LogLevel.ERROR, "Error!", "getIPv6", e);
        }
        return ipv6;
    }

}
