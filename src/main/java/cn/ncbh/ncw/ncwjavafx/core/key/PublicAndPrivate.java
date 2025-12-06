package cn.ncbh.ncw.ncwjavafx.core.key;

import cn.ncbh.ncw.ncwjavafx.algorithm.encrypt.aes.AESEncryption;
import cn.ncbh.ncw.ncwjavafx.algorithm.encrypt.rsa.RSAEncryption;
import cn.ncbh.ncw.ncwjavafx.pyex.os;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PublicAndPrivate {

    protected static final Map<String, String> RSAKeys = Map.of("public", "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAlKfziDWbifQi55ZZ/ZddZb14HqJE8LLSfWX15Hon7WkcLLCaPihIi0wuM9cvHEu0HYSbdcA6XIacm36rKqLLIdqcqkTWYhxQekiRL8H69VxP2IRFye1ReDsmVWh37czObWCa4oD1+1DOMShJiqOjtrbvP/cUHGczu+g7hSQVmmrhaZS+OkmBaYs//+Ii65/YVUEOrqnoxwtSAqiGsD3Z+MSNhqIX1KwgGukOYEj11JNxwFavGONlrCM0JDkPUue1+MT7FUziBYkya9iTIb3B/oz2yPW2mFFR6r6Rjl2CRbtnr0jV7HAy/aMyXUjmF/9Wl5WMS4/1WkZIOzx+U7FFNwIDAQAB", "private", "MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQCUp/OINZuJ9CLnlln9l11lvXgeokTwstJ9ZfXkeiftaRwssJo+KEiLTC4z1y8cS7QdhJt1wDpchpybfqsqossh2pyqRNZiHFB6SJEvwfr1XE/YhEXJ7VF4OyZVaHftzM5tYJrigPX7UM4xKEmKo6O2tu8/9xQcZzO76DuFJBWaauFplL46SYFpiz//4iLrn9hVQQ6uqejHC1ICqIawPdn4xI2GohfUrCAa6Q5gSPXUk3HAVq8Y42WsIzQkOQ9S57X4xPsVTOIFiTJr2JMhvcH+jPbI9baYUVHqvpGOXYJFu2evSNXscDL9ozJdSOYX/1aXlYxLj/VaRkg7PH5TsUU3AgMBAAECggEAAj8plJju0V5xuptQn98OR8/dxWTBcvvY55FHbp0UBmgMx3Yh1qeoNQbBOgJaEWzpe3q6GMT/fM+CBW2cK6hLNqdFr+MbdF8mLjrzisu9PcEX6ddR8LPYA9+s/CR4j40FX6zSmnLFhPdbLm4D8ob3nfbU1dwnnyhGJWRkpeX+N5e5C9lNz5FR05BPjYjwhcI5NfWrFVNCeGQNS2RaeaPJRJET6BtRbprKKJ7rsJv2POBzKC7fmbtwwLNNt/NbSAoHoacc6xMS1SfiVM5ZIaYGQc8BTXw5LgnS7/npZoegIHy8oBVlfNtn78rNC2zD7E02dl3fxiU/oKrkmJYoiPrn2QKBgQDDYn+zq2bToMmss4EPn/5KkqtVikjw6CTguaE7Y6yMT/aTBcsmGPCubT/NHI4SH5maD+9SNuvkLUiNxulqV6LrloeP8Hs+PLHcKU5GYWFhikL05CBJt8OB1z/mQwdTz5HC4Q656jTht5bXhFuQHA+CkA5K8j6CmFgc1rGmXhGyfwKBgQDCxj+lJconKLyidTeUFYzinYccAMCmSgYvQ7EiNNn0A27ftJoRagDFcOdVSYGNS5teT1iOvL0ZTTdTuOoC9Pw/7wjvCWy8qou9+wfs3Yaomz+C3ZcYS2ybiDnzrUjUnoRVHlevIi2sMtx0HhzuU4ShWgSR2kgSw1i21a4XX90hSQKBgHP1DlMkg4dP4sFDW3JZgwLhHxxKUNXhAJn6sWzcz/gKMQ26n4GBxb3PitEBhHPPG0sq7978R22JTokOJ1N8pW1qThQoJgye2vQN862jd7b65pgOl/cIP9jcVckjuMKN6zUceq/uBup7GePYYrsZXD8hncnvERhSr+CVrqNNKvWjAoGAdiOXYQ1N03izCfr9LhXwMVgJJmQSdgp6O9rcKvpRpN8cNhYuSRe5xghYWHPQX6qXX5nTBijWre0W/PIxBoAgLItJKOvXh4wOXLpRsUsgrU4VNMRBLFw7UQ9Mz0+w2D4Z2CMiXjjl6Qekjc3iEHCaBuA521nB05enTKbJBKaB0vkCgYAalu24FLfzWf1qW1pnOT4i0zP7Zyyq5OBueK5nS/SKgV8KzaxcsfqGB0WmRm9V4lpwnYUqO+Uep7fpU050+ygjQT33JKcZYscHUop/7Agc8wQhHXsSA0FqbKeiD5dvfC1/4s+YtbY+AXmXGaLnd19nDIEoj8o6htWRbFCBlNFIwQ==");

    protected static final String PATH = "resources/network/key/keys.ncw";

    public static Map<String, String> getKeysMap() {
        return loadKeys();
    }

    public static List<String> getPublicKeys() {
        return getPublicKey();
    }

    private static List<String> getPublicKey() {
        List<String> list = new ArrayList<>();
        try {
            list.addAll(getList(loadKeysMap(decryptString(loadString(PublicAndPrivate.class, PATH)))));
        } catch (Exception ignored) {

        }
        return list;
    }

    private static void writeKeysMap(Map<String, String> map) {
        try {
            String[] keys = map.keySet().toArray(String[]::new);
            String[] values = map.values().toArray(String[]::new);
            List<String> list = new ArrayList<>();
            for (int i = 0; i < map.size(); i++) {
                list.add(keys[i] + "Ciallo～(∠・ω< )⌒★" + values[i]);
            }
            list.addFirst("NoneColdWind_114514");
            list = encryptString(list);
            Files.delete(Path.of(os.getcwd() + "\\resources\\network\\key\\keys.ncw"));
            Files.createFile(Path.of(os.getcwd() + "\\resources\\network\\key\\keys.ncw"));
            Files.write(Path.of(os.getcwd() + "\\resources\\network\\key\\keys.ncw"), list, StandardOpenOption.WRITE);
        } catch (Exception ignored) {

        }
    }

    private static Map<String, String> loadKeys() {
        return loadKeysMap();
    }

    private static Map<String, String> loadKeysMap() {
        Map<String, String> map = new HashMap<>();
        try {
            map.putAll(getMap(loadKeysMap(decryptString(loadString(PublicAndPrivate.class, PATH)))));
        } catch (Exception ignored) {

        }
        return map;
    }

    private static List<String> encryptString(List<String> list) throws Exception {
        String password = RSAEncryption.encrypt(list.getFirst(), RSAKeys.get("public"));
        List<String> newList = new ArrayList<>();
        list.removeFirst();
        newList.add(password);
        for (String s : list) {
            newList.add(AESEncryption.encrypt(s, RSAEncryption.decrypt(password, RSAKeys.get("private"))));
        }
        return newList;
    }

    @SuppressWarnings("unchecked")
    public static Map<String, String> getMap(List<Object> list) {
        return (Map<String, String>) list.getLast();
    }

    @SuppressWarnings("unchecked")
    public static List<String> getList(List<Object> list) {
        return (List<String>) list.getFirst();
    }

    private static List<Object> loadKeysMap(List<String> list) {
        list.removeFirst();
        Map<String, String> map = new HashMap<>();
        List<String> publicKeys = new ArrayList<>();
        List<Object> finalList = new ArrayList<>();
        for (String s : list) {
            String[] strings = s.split("Ciallo～\\(∠・ω< \\)⌒★");
            publicKeys.add(strings[0]);
            map.put(strings[0], strings[1]);
        }
        finalList.add(publicKeys);
        finalList.add(map);
        return finalList;
    }

    private static List<String> decryptString(List<String> list) throws Exception {
        String password = list.getFirst();
        List<String> newList = new ArrayList<>();
        list.removeFirst();
        newList.add(RSAEncryption.decrypt(password, RSAKeys.get("private")));
        for (String s : list) {
            newList.add(AESEncryption.decrypt(s, RSAEncryption.decrypt(password, RSAKeys.get("private"))));
        }
        return newList;
    }

    private static List<String> loadString(Class<?> clazz, String path) throws IOException {
        BufferedReader reader = getReader(getInputStream(clazz, path));
        List<String> list = new ArrayList<>();
        String line;
        while ((line = reader.readLine()) != null) {
            list.add(line);
        }
        return list;
    }

    private static BufferedReader getReader(InputStream inputStream) {
        return new BufferedReader(new InputStreamReader(inputStream));
    }

    private static InputStream getInputStream(Class<?> clazz, String path) {
        return getClassLoader(clazz) .getResourceAsStream(path);
    }

    private static ClassLoader getClassLoader(Class<?> clazz) {
        return clazz.getClassLoader();
    }

}
