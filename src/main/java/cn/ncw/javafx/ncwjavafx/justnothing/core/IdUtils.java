package cn.ncw.javafx.ncwjavafx.justnothing.core;

public class IdUtils {
    /**
     * 返回对象的唯一标识符（内存地址的整数表示）
     * 等同于 Python 的 id() 函数
     *
     * @param obj 要获取标识符的对象
     * @return 对象的唯一标识符
     */
    public static int id(Object obj) {
        // 对 null 的特殊处理
        if (obj == null) {
            return 0; // 与 Python 保持一致（CPython 中 id(None) 有固定值）
        }
        return System.identityHashCode(obj);
    }

    /**
     * 返回对象的唯一标识符字符串（内存地址的十六进制表示）
     * 格式类似于 Python 的默认对象表示
     *
     * @param obj 要获取标识符的对象
     * @return 十六进制格式的唯一标识符
     */
    public static String hexId(Object obj) {
        if (obj == null) {
            return "0x0";
        }
        // 格式化为 8 位十六进制数，与 JVM 默认对象表示一致
        return String.format("0x%08x", System.identityHashCode(obj));
    }

    public static void main(String[] args) {
        // 测试对象
        Object obj1 = new Object();
        Object obj2 = new Object();

        // 基本测试
        System.out.println(id(obj1));       // 整数标识符
        System.out.println(id(obj2));       // 不同的整数标识符
        System.out.println(hexId(obj1));    // 类似 Python 的十六进制格式

        // 测试字符串
        String s1 = "Hello";
        String s2 = "Hello";
        String s3 = new String("Hello");

        System.out.println(id(s1));  // 相同常量池地址
        System.out.println(id(s2));  // 同上
        System.out.println(id(s3));  // 新对象，不同地址

        // 空对象测试
        System.out.println(id(null));       // 0
        System.out.println(hexId(null));    // 0x0

        // 自引用测试
        System.out.println(id(obj1) == id(obj1)); // true
    }
}