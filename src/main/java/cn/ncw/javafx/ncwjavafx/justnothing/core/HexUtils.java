package cn.ncw.javafx.ncwjavafx.justnothing.core;

public class HexUtils {
    /**
     * 将整数转换为十六进制字符串（小写）
     * 等同于 Python 的 hex() 函数
     *
     * @param num 要转换的整数
     * @return 带有 "0x" 前缀的十六进制字符串
     */
    public static String hex(long num) {
        // 处理负数 - 转换为无符号长整型
        long value = num;
        if (num < 0) {
            value = (1L << 32) + num;  // 32位负数转换
        }

        // 转换并添加前缀
        String hexStr = Long.toHexString(value).toLowerCase();
        return "0x" + hexStr;
    }

    /**
     * 将整数转换为固定长度的十六进制字符串（包括前导零）
     *
     * @param num 要转换的整数
     * @param bits 位数（32或64）
     * @return 带有 "0x" 前缀的固定长度十六进制字符串
     */
    public static String hexFixed(long num, int bits) {
        if (bits != 32 && bits != 64) {
            throw new IllegalArgumentException("Bits must be 32 or 64");
        }

        // 64位处理
        if (bits == 64) {
            return "0x" + String.format("%016x", num).toLowerCase();
        }

        // 32位处理（截取后8个十六进制数字）
        String fullHex = String.format("%016x", num);
        return "0x" + fullHex.substring(fullHex.length() - 8).toLowerCase();
    }

    public static void main(String[] args) {
        // 测试用例（与Python行为一致）
        System.out.println(hex(10));        // 0xa
        System.out.println(hex(255));       // 0xff
        System.out.println(hex(0xDEADBEEF));// 0xdeadbeef
        System.out.println(hex(-10));       // 0xfffffff6
        System.out.println(hex(0));         // 0x0
        System.out.println(hex(-1));        // 0xffffffff

        // 测试固定长度版本
        System.out.println(hexFixed(10, 32));       // 0x0000000a
        System.out.println(hexFixed(-10, 32));      // 0xfffffff6
        System.out.println(hexFixed(0xDEADBEEF, 32)); // 0xdeadbeef
        System.out.println(hexFixed(1234567890123456L, 64)); // 0x000462d53c8abac0
    }
}