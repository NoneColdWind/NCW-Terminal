package cn.ncbh.ncw.ncwjavafx.custom_error;

public class FileNotFoundError extends RuntimeException {
    public FileNotFoundError() {
        super("文件不存在。");
    }
}
