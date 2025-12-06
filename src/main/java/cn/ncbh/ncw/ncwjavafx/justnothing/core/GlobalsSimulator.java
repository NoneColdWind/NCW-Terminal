package cn.ncbh.ncw.ncwjavafx.justnothing.core;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

public class GlobalsSimulator {

    // 示例静态变量（"全局变量"）
    public static int version = 1;
    public static final String AUTHOR = "John Doe";
    private static boolean debug = false;

    public static Map<String, Object> globals() {
        Map<String, Object> globalMap = new HashMap<>();

        // 获取当前类的所有字段
        Field[] fields = GlobalsSimulator.class.getDeclaredFields();

        for (Field field : fields) {
            // 只处理静态字段（最接近"全局变量"）
            if (Modifier.isStatic(field.getModifiers())) {
                try {
                    // 使私有字段可访问
                    field.setAccessible(true);

                    // 获取字段名和当前值
                    String name = field.getName();
                    Object value = field.get(null); // 静态字段，传入null实例

                    // 添加到映射
                    globalMap.put(name, value);
                } catch (IllegalAccessException e) {
                    // 忽略无法访问的字段
                }
            }
        }

        return globalMap;
    }

    public static void main(String[] args) {
        // 添加或修改一些变量以演示
        version = 2;

        Map<String, Object> globalVars = globals();
        for (Map.Entry<String, Object> entry : globalVars.entrySet()) {
            System.out.println(entry.getKey() + " = " + entry.getValue());
        }
    }
}