package cn.ncbh.ncw.ncwjavafx.pyex;

import cn.ncbh.ncw.ncwjavafx.base.StringSplitter;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class time {

    public static void sleep(Double time_s) {

        List<Character> list_wait = StringSplitter.splitToCharList(time_s.toString());

        int dot_key = list_wait.indexOf('.');

        int float_part_length = list_wait.size() - dot_key;

        StringBuilder final_time = new StringBuilder();
        try {
            if (float_part_length > 9) {
                for (int i = dot_key + 10; i < list_wait.size(); i++) {
                    list_wait.removeLast();
                }
                for (Character character : list_wait) {
                    final_time.append(character);
                }
                long timeNs = Long.parseLong(final_time.toString().replace(".", ""));
                TimeUnit.NANOSECONDS.sleep(timeNs);
            } else {
                for (int i = float_part_length; i < 9; i++) {
                    list_wait.addLast('0');
                }
                for (Character character : list_wait) {
                    final_time.append(character);
                }
                long timeNs = Long.parseLong(final_time.toString().replace(".", ""));
                TimeUnit.NANOSECONDS.sleep(timeNs);
            }
        } catch (Exception ignored) {

        }
    }
}
