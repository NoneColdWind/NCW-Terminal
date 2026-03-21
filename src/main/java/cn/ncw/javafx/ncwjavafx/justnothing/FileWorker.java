package cn.ncw.javafx.ncwjavafx.justnothing;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public class FileWorker {

    private static Map<String, String> INFO = new Hashtable<>();

    private static Map<String, Charset> Charsets = new Hashtable<>();

    private static Map<String, OpenOption> OpenOptions = new Hashtable<>();

    static {
        Charsets.put("utf-8", StandardCharsets.UTF_8);
        Charsets.put("utf-16", StandardCharsets.UTF_16);
        Charsets.put("utf-16be", StandardCharsets.UTF_16BE);
        Charsets.put("utf-16le", StandardCharsets.UTF_16LE);
        Charsets.put("iso_8859_1", StandardCharsets.ISO_8859_1);
        Charsets.put("ascii", StandardCharsets.US_ASCII);
    }

    static {
        OpenOptions.put("r", StandardOpenOption.READ);
        OpenOptions.put("w", StandardOpenOption.WRITE);
        OpenOptions.put("a", StandardOpenOption.APPEND);
        OpenOptions.put("c", StandardOpenOption.CREATE);
        OpenOptions.put("c+", StandardOpenOption.CREATE_NEW);
        OpenOptions.put("dc", StandardOpenOption.DELETE_ON_CLOSE);
    }

    static {
        INFO.put("file_path", "");
        INFO.put("mode", "r");
        INFO.put("buffering", "-1");
        INFO.put("encoding", "utf-8");
        INFO.put("errors", "None");
        INFO.put("new_line", "None");
        INFO.put("close_fd", "None");
        INFO.put("opener", "None");
    }

    public FileWorker(String file_path) {
        INFO.replace("file_path", file_path);
    }

    public FileWorker(String file_path, String mode) {
        INFO.replace("file_path", file_path);
        INFO.replace("mode", mode);
    }

    public FileWorker(String file_path, String mode, String buffering) {
        INFO.replace("file_path", file_path);
        INFO.replace("mode", mode);
        INFO.replace("buffering", buffering);
    }

    public FileWorker(String file_path, String mode, String buffering, String encoding) {
        INFO.replace("file_path", file_path);
        INFO.replace("mode", mode);
        INFO.replace("buffering", buffering);
        INFO.replace("encoding", encoding);
    }

    public FileWorker(String file_path, String mode, String buffering, String encoding, String errors) {
        INFO.replace("file_path", file_path);
        INFO.replace("mode", mode);
        INFO.replace("buffering", buffering);
        INFO.replace("encoding", encoding);
        INFO.replace("errors", errors);
    }

    public FileWorker(String file_path, String mode, String buffering, String encoding, String errors, String new_line) {
        INFO.replace("file_path", file_path);
        INFO.replace("mode", mode);
        INFO.replace("buffering", buffering);
        INFO.replace("encoding", encoding);
        INFO.replace("errors", errors);
        INFO.replace("new_line", new_line);
    }

    public FileWorker(String file_path, String mode, String buffering, String encoding, String errors, String new_line, String close_fd) {
        INFO.replace("file_path", file_path);
        INFO.replace("mode", mode);
        INFO.replace("buffering", buffering);
        INFO.replace("encoding", encoding);
        INFO.replace("errors", errors);
        INFO.replace("new_line", new_line);
        INFO.replace("close_fd", close_fd);
    }

    public FileWorker(String file_path, String mode, String buffering, String encoding, String errors, String new_line, String close_fd, String opener) {
        INFO.replace("file_path", file_path);
        INFO.replace("mode", mode);
        INFO.replace("buffering", buffering);
        INFO.replace("encoding", encoding);
        INFO.replace("errors", errors);
        INFO.replace("new_line", new_line);
        INFO.replace("close_fd", close_fd);
        INFO.replace("opener", opener);
    }

    public String read() {
        String result = "";
        try {
            result = Files.readString(Paths.get(INFO.get("file_path")), Charsets.get(INFO.get("encoding")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public String read(Integer size) {
        String result = "";
        try {
            result = Files.readString(Paths.get(INFO.get("file_path")), Charsets.get(INFO.get("encoding")));
            result = String.format("%-" + size + "s", result);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public String readline() {
        String result = "";
        try {
            List<String> list = Files.readAllLines(Paths.get(INFO.get("file_path")), Charsets.get(INFO.get("encoding")));
            result = list.getFirst();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public String readline(Integer size) {
        String result = "";
        try {
            List<String>  list = Files.readAllLines(Paths.get(INFO.get("file_path")), Charsets.get(INFO.get("encoding")));
            result = list.getFirst();
            result = String.format("%-" + size + "s", result);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public List<String> readlines() {
        List<String> list = new ArrayList<>();
        try {
            list = Files.readAllLines(Paths.get(INFO.get("file_path")), Charsets.get(INFO.get("encoding")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    public Void write(String text) {
        try {
            Files.write(Paths.get(INFO.get("file_path")), text.getBytes(), StandardOpenOption.CREATE, OpenOptions.get(INFO.get("mode")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Void writelines(List<String> list) {
        try {
            Files.write(Paths.get(INFO.get("file_path")), list, StandardOpenOption.CREATE, OpenOptions.get(INFO.get("mode")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Void flush() {
        return null;
    }

    public Void close() {
        return null;
    }

}
