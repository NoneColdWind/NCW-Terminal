package cn.ncbh.ncw.ncwjavafx.base.hash;

import java.util.Arrays;

public class WhatCanISay {

    public static String QAQ(String s) {
        return qwertyuiopasdfghjklzxcvbnm(s);
    }

    private static String qwertyuiopasdfghjklzxcvbnm(String str) {
        return O0oOo00o(str, SHA3Utils.抽象哈希3加密(str));
    }

    protected static String O0oOo00o(String value, String key) {
        value+="WhatCanISay";
        return O0oOoo0o(value, key);
    }

    protected static String O0oOoo0o(String value, String key) {
        value+="lao_da";
        return O00Ooo0o(value, key);
    }

    protected static String O00Ooo0o(String value, String key) {
        return O00Ooooo(value, key);
    }

    protected static String O00Ooooo(String value, String key) {
        return oO0OOOo0(new Ooo0oo0(value), key);
    }

    protected static String oO0OOOo0(Ooo0oo0 obj, String key) {
        String input = obj.getValue();
        String var = "";
        for (int i = 0; i < 114; i++) {
            var = SHA3Utils.抽象哈希3加密(input + Arrays.toString(key.getBytes()));
        }
        for (int i = 0; i < 24; i++) {
            var = SHA2Utils.抽象哈希2加密(var);
        }
        return var;
    }

    protected static class Ooo0oo0 {
        public String O0o0000 = "";
        public Ooo0oo0(String string) {
            this.O0o0000 = string;
        }
        public String getValue() {
            return Ooo00ooO();
        }
        private String Ooo00ooO() {
            return Ooooooo();
        }
        protected String Oooo0oo() {
            return Oooo00o();
        }
        protected String Ooooooo() {
            return Oooo0oo();
        }
        protected String Oooo00o() {
            return OoooOOo();
        }
        protected String OoooOOo() {
            return this.O0o0000;
        }
    }

    public static void main(String[] args) {
        System.out.println(QAQ("114514"));
        System.out.println(SHA3Utils.sha3_512Hash("114514"));
    }

}
