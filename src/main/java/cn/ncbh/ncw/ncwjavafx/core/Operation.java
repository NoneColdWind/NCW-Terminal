package cn.ncbh.ncw.ncwjavafx.core;

import cn.ncbh.ncw.ncwjavafx.base.HighPrecision;

public class Operation {

    public static String Addition(String var1, String var2) {
        HighPrecision operate = new HighPrecision(var1);
        return operate.add(var2);
    }

    public static String Subtraction(String var1, String var2) {
        HighPrecision operate = new HighPrecision(var1);
        return operate.sub(var2);
    }

    public static String Multiplication(String var1, String var2) {
        HighPrecision operate = new HighPrecision(var1);
        return operate.mul(var2);
    }

    public static String Division(String var1, String var2) {
        HighPrecision operate = new HighPrecision(var1);
        return operate.div(var2);
    }

    public static String Division(String var1, String var2, Integer float_part_length) {
        HighPrecision operate = new HighPrecision(var1);
        return operate.div(var2, float_part_length);
    }

    public static String SquareRoot(String var1) {
        HighPrecision operate = new HighPrecision(var1);
        return operate.sqrt();
    }

    public static String SquareRoot(String var1, Integer float_part_length) {
        HighPrecision operate = new HighPrecision(var1);
        return operate.sqrt(float_part_length);
    }

    public static String Power(String var1, String var2) {
        HighPrecision operate = new HighPrecision(var1);
        return operate.pow(var2);
    }

}
