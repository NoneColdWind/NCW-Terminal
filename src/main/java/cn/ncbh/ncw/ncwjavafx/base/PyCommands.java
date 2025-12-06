package cn.ncbh.ncw.ncwjavafx.base;

import cn.ncbh.ncw.ncwjavafx.pyex.os;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class PyCommands {

    public static String input(String text) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.print(text);
        return reader.readLine();
    }

    public static void print(String text) {
        System.out.println(text);
    }

    public static void pause(String text) {
        text = (text == null) ? "按任意键继续......" : text;
        Scanner sc = new Scanner(System.in);
        System.out.println(text);
        sc.nextLine();
    }

    public static void cls() {
        os.system("cls", false);
    }

    public static void timeout(int timeMs) throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(timeMs);
    }



}
