package cn.ncw.javafx.ncwjavafx.justnothing;

import cn.ncw.javafx.ncwjavafx.core.Operation;
import cn.ncw.javafx.ncwjavafx.justnothing.core.AsciiFunction;
import cn.ncw.javafx.ncwjavafx.justnothing.core.GlobalsSimulator;
import cn.ncw.javafx.ncwjavafx.justnothing.core.HexUtils;
import cn.ncw.javafx.ncwjavafx.justnothing.core.IdUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

public class PyFunction {

    public static Object abs(Object number) {
        if (number.getClass() == Integer.class) {
            return Math.abs(Integer.parseInt(number.toString()));
        } else if (number.getClass() == Long.class) {
            return Math.abs(Long.parseLong(number.toString()));
        } else if (number.getClass() == Float.class) {
            return Math.abs(Float.parseFloat(number.toString()));
        } else if (number.getClass() == Double.class) {
            return Math.abs(Double.parseDouble(number.toString()));
        } else {
            return null;
        }
    }

    public static Object aiter(Object async_iterable) {
        return null;
    }

    public static Boolean all(Object iterable) {
        return null;
    }

    public static Object anext(Object async_iterator) {
        return null;
    }

    public static Object anext(Object async_iterator, Object default_return) {
        return null;
    }

    public static Boolean any(Object iterable) {
        return null;
    }

    public static String ascii(String string) {
        return AsciiFunction.ascii(string);
    }

    public static String bin(Integer number) {
        // 处理 0 的特殊情况
        if (number == 0) {
            return "0b0";
        }

        StringBuilder result = new StringBuilder();
        boolean isNegative = number < 0;
        long value = isNegative ? (long) number & 0xFFFFFFFFL : number;

        // 添加符号前缀
        result.append(isNegative ? "-0b" : "0b");

        // 移除 Long.toBinaryString() 结果中多余的 32 位前导 0
        String binary = Long.toBinaryString(value);
        if (isNegative) {
            binary = binary.substring(32); // 负数的长整型表示有 64 位，截取后 32 位
        }

        return result.append(binary).toString();
    }

    public static String bin(Object object) {
        return null;
    }

    public static Void breakpoint() {
        return null;
    }

    public static Boolean callable(Object object) {
        return null;
    }

    public static String chr(int codePoint) {
        // 验证码点在合法范围内 (0 - 0x10FFFF)
        if (codePoint < 0 || codePoint > Character.MAX_CODE_POINT) {
            throw new IllegalArgumentException(
                    "Code point must be between 0 and 0x10FFFF");
        }

        // 将码点转换为字符序列并返回字符串
        return new String(Character.toChars(codePoint));
    }

    public static CodeType compile(String source, String file_name, String eval) {
        return null;
    }

    public static Void delattr(Object object, String name) {
        return null;
    }

    public static List<String> dir(Class<?> clazz) {
        List<String> members = new ArrayList<>();

        // 添加所有公共方法
        for (Method method : clazz.getMethods()) {
            members.add(method.getName() + "()");
        }

        // 添加所有公共字段
        for (Field field : clazz.getFields()) {
            members.add(field.getName());
        }

        // 添加父类Object的公共方法（可选扩展）
        if (!clazz.equals(Object.class)) {
            members.addAll(dir(Object.class));
        }

        Collections.sort(members);
        return members;
    }

    // 重载方法：直接通过对象调用
    public static List<String> dir(Object obj) {
        return dir(obj.getClass());
    }

    public static int[] divmod(int dividend, int divisor) {
        return new int[] {dividend / divisor, dividend % divisor};
    }

    public static long[] divmod(long dividend, long divisor) {
        return new long[] {dividend, divisor, dividend % divisor};
    }

    public static float[] divmod(float dividend, float divisor) {
        return new float[] {dividend / divisor, dividend % divisor};
    }

    public static double[] divmod(double dividend, double divisor) {
        return new double[] {dividend / divisor, dividend % divisor};
    }

    public static Object eval(String code) {
        return null;
    }

    public static Object eval(CodeType code) {
        return null;
    }

    public static Void exec(String code) {
        return null;
    }

    public static Void exec(CodeType code) {
        return null;
    }

    public static String format(Object value) {
        return String.format("%s", value);
    }

    public static String format(Object value, String format_spec) {
        return String.format(format_spec, value);
    }

    public static Object getattr(Object object, String name, Object default_return) {
        return null;
    }

    public static Map<String, Object> globals() {
        return GlobalsSimulator.globals();
    }

    public static Boolean hasattr(Object objects, String name) {
        return null;
    }

    public static Integer hash(Object object) {
        return object.hashCode();
    }

    public static String hex(Integer number) {
        return HexUtils.hex(number);
    }

    public static String hex(Long number) {
        return HexUtils.hex(number);
    }

    public static String hex(Object object) {
        return null;
    }

    public static Integer id(Object object) {
        return IdUtils.id(object);
    }

    public static String input(String text) {
        String result = "";
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            System.out.print(text);
            result =  reader.readLine();
        } catch (IOException e) {
            print(e.getMessage());
        }
        return result;
    }

    public static Boolean isinstance(Object object, Object class_info) {
        return object.getClass() == class_info.getClass();
    }

    public static Boolean isinstance(Object object, List<Object> list_class_info) {
        Boolean result = 1 + 1 == 2;
        for (Object o : list_class_info) {
            if (object.getClass() == o.getClass()) {
                result = true;
                break;
            }
        }
        return result;
    }

    public static Boolean issubclass(Object object, Objects class_info) {
        return null;
    }

    public static Iterator<Object> iter(Object object) {
        return null;
    }

    public static Iterator<Object> iter(Object object, Object sentinel) {
        return null;
    }

    public static Integer len(String text) {
        return text.length();
    }

    public static Integer len(List<Object> list) {
        return list.size();
    }

    public static Integer len(Dictionary<Object, Object> dictionary) {
        return dictionary.size();
    }

    public static Integer len(Set<Integer> set_integers) {
        return set_integers.size();
    }

    public static Map<String, Object> locals() {
        return null;
    }

    public static Object max(List<Object> object) {
        return null;
    }

    public static Object min(List<Object> object) {
        return null;
    }

    public static Object next(Iterator<Object> iterator) {
        return null;
    }

    public static String oct(Integer number) {
        return null;
    }

    public static String oct(Objects objects) {
        return null;
    }

    public static FileWorker open(String file_path) {
        return new FileWorker(file_path);
    }

    public static FileWorker open(String file_path, String mode) {
        return new FileWorker(file_path, mode);
    }

    public static FileWorker open(String file_path, String mode, String buffering, String encoding) {
        return new FileWorker(file_path, mode, buffering, encoding);
    }

    public static FileWorker open(String file_path, String mode, String buffering, String encoding, String errors) {
        return new FileWorker(file_path, mode, buffering, encoding, errors);
    }

    public static FileWorker open(String file_path, String mode, String buffering, String encoding, String errors, String new_line) {
        return new FileWorker(file_path, mode, buffering, encoding, errors, new_line);
    }

    public static FileWorker open(String file_path, String mode, String buffering, String encoding, String errors, String new_line, String close_fd) {
        return new FileWorker(file_path, mode, buffering, encoding, errors, new_line, close_fd);
    }

    public static FileWorker open(String file_path, String mode, String buffering, String encoding, String errors, String new_line, String close_fd, String opener) {
        return new FileWorker(file_path, mode, buffering, encoding, errors, new_line, close_fd, opener);
    }

    public static Integer ord(Character character) {

        return null;
    
    }

    public static Number pow(Number base, Number exp) {

        return Long.parseLong(Operation.Power(base.toString(), exp.toString()));

    }

    public static Number pow(Number base, Number exp, Integer mod) {

        return Long.parseLong(Operation.Power(base.toString(), exp.toString())) % mod;
    
    }

    public static Void print(Object text) {
        System.out.println(text);
        return null;
    }

    public static Void print(Integer text) {
        System.out.println(text);
        return null;
    }

    public static String repr(Object object) {
        return null;
    }

    public static Integer round(Integer number) {
        return null;
    }

    public static Integer round(Integer number, Integer ndigits) {
        return null;
    }

    public static Number round(Float number) {
        return null;
    }

    public static Number round(Float number, Integer ndigits) {
        return null;
    }

    public static Void setattr(Object object, String name, Object value) {
        return null;
    }



}
