package cn.ncw.javafx.ncwjavafx.network;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;

import static cn.ncw.javafx.ncwjavafx.base.CommonVariable.logger;


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
            logger.error("Error!" + e, "getIPv6");
        }
        return ipv6;
    }

}
