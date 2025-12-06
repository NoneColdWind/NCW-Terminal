package cn.ncbh.ncw.ncwjavafx.base.hash;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public class FileCheckMethod {

    public static String check_md5_with_sha(String path) throws IOException, NoSuchAlgorithmException {
        return file_check(path);
    }

    protected static String file_check(String path) throws IOException, NoSuchAlgorithmException {
        return SHA256Utils.sha256Hash(FileMD5Hasher.calculateFileMD5(path));
    }

    public static void main(String[] args) throws IOException, NoSuchAlgorithmException {
        System.out.println(check_md5_with_sha("check\\ncw.json"));
    }
}
