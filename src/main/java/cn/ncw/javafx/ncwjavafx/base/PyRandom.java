package cn.ncw.javafx.ncwjavafx.base;

import java.util.List;
import java.util.Random;

public class PyRandom {

    public static int randint(int var1, int var2) {
        Random random = new Random();
        return random.nextInt(var1, var2);
    }

    public static double random(double var1, double var2) {
        Random random = new Random();
        return random.nextDouble(var1, var2);
    }

    public static String choose(List<String> list_str) {
        Random random = new Random();
        return list_str.get(random.nextInt(list_str.size()));
    }

    public static void print(String text) {
        System.out.println(text);
    }

}
