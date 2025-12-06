package cn.ncbh.ncw.ncwjavafx.log;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Logger {

    private static String LoggerName;

    public Logger(String app_name) {
        LoggerName = app_name;
    }

    public void log(String level, String name, String text) {
        List<String> list_wait = new ArrayList<>();
        list_wait.addLast(level);
        list_wait.addLast(LoggerName + ":" +name);
        list_wait.addLast(text);
        System.out.println(formatLogEntry(list_wait));
    }

    private String formatLogEntry(List<String> context) {

        String today = LocalDate.now().toString();
        String currentTime = LocalTime.now().toString();
        int maxLength = 15;
        String truncated = currentTime.substring(0, Math.min(currentTime.length(), maxLength));
        String formatted = String.format("%-" + maxLength + "s", truncated);
        String timestamp = today + " " + formatted;

        if (Objects.equals(context.get(0), "INFO")) {
            return String.format("\033[94m%s \033[92m[%-5s] \033[90m%-24s \033[92m%s\033[0m",
                    timestamp,
                    context.getFirst(),
                    context.get(1),
                    context.getLast()
            );
        } else if (Objects.equals(context.get(0), "WARN")) {
            return String.format("\033[94m%s \033[33m[%-5s] \033[90m%-24s \033[33m%s\033[0m",
                    timestamp,
                    context.getFirst(),
                    context.get(1),
                    context.getLast()
            );
        } else if (Objects.equals(context.get(0), "DEBUG")) {
            return String.format("\033[94m%s \033[90m[%-5s] \033[90m%-24s \033[90m%s\033[0m",
                    timestamp,
                    context.getFirst(),
                    context.get(1),
                    context.getLast()
            );
        } else if (Objects.equals(context.get(0), "ERROR")) {
            return String.format("\033[94m%s \033[31m[%-5s] \033[90m%-24s \033[31m%s\033[0m",
                    timestamp,
                    context.getFirst(),
                    context.get(1),
                    context.getLast()
            );
        } else {
            return String.format("\033[94m%s \033[35m[%-5s] \033[90m%-24s \033[35m%s\033[0m",
                    timestamp,
                    context.getFirst(),
                    context.get(1),
                    context.getLast()
            );
        }
    }
}

