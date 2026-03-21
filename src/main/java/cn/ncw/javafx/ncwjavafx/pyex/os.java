package cn.ncw.javafx.ncwjavafx.pyex;

import cn.ncw.javafx.ncwjavafx.base.PyRandom;
import cn.ncw.javafx.ncwjavafx.custom_error.FileExistsError;
import cn.ncw.javafx.ncwjavafx.custom_error.FileNotFoundError;
import cn.ncw.javafx.ncwjavafx.custom_error.OSError;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class os {

    public static int system(String command) {
        try {
            // 执行命令
            command = "cmd /c " + command;

            Process process = Runtime.getRuntime().exec(command.split(" "));
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream(), "GBK")  // Windows中文环境用GBK编码
            );
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
            // 等待命令执行完成
            return process.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return PyRandom.randint(1, 114514);
        }
    }
    public static int system(String command, Boolean is_print) {
        try {

            // 执行命令
            command = "cmd /c " + command;

            Process process = Runtime.getRuntime().exec(command.split(" "));
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream(), "GBK")  // Windows中文环境用GBK编码
            );
            if (is_print) {
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println(line);
                }
            }
            // 等待命令执行完成
            return process.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return PyRandom.randint(1, 114514);
        }
    }

    public static String getcwd() {
        return System.getProperty("user.dir");
    }

    public static void chdir(String path) {
        System.setProperty("user.dir", path);
    }

    public static List<String> listdir(String path) {

        File dir = new File(path);

        if (!dir.exists() || !dir.isDirectory()) {
            return Collections.emptyList();
        }

        String[] items = dir.list();
        if (items == null) {
            return Collections.emptyList();
        }
        return Arrays.asList(items);
    }

    public static void mkdir(String path) throws FileExistsError {
        File deepDirectory = new File(path);

        // 使用mkdirs()创建所有不存在的父目录
        if (deepDirectory.mkdirs()) {
            String result = "success";
        } else {
            throw new FileExistsError();
        }
    }

    public static void rmdir(String path) throws OSError {

        File dir = new File(path);

        // 检查目录是否存在且是目录
        if (dir.exists() && dir.isDirectory()) {
            // 检查目录是否为空
            if (listdir(path).isEmpty()) {
                boolean isDeleted = dir.delete();
                if (isDeleted) {
                    Boolean i = true;
                } else {
                    throw new OSError("目录删除失败。");
                }
            } else {
                throw new OSError("目录不为空，无法删除。");
            }
        } else {
            throw new OSError("目录不存在或不是一个目录。");
        }
    }
    public static void remove(String filePath) throws OSError, FileNotFoundError {
        File file = new File(filePath);

        if (file.exists()) {
            if (file.delete()) {
                String a = "文件删除成功";
            } else {
                throw new OSError("文件删除失败");
            }
        } else {
            throw new FileNotFoundError();
        }
    }

    public static void rename(String old_name, String new_name) {
        Path source = Path.of(old_name);
        Path target = Paths.get(new_name);
        try {
            // 重命名或移动文件，如果目标文件存在则替换
            Files.move(source, target, StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getenv(String key) {
        return System.getenv(key);
    }

}
