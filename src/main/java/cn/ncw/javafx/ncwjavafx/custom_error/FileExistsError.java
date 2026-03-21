package cn.ncw.javafx.ncwjavafx.custom_error;

public class FileExistsError extends RuntimeException {

    public FileExistsError() {
        super("目录已存在。");
    }

}
