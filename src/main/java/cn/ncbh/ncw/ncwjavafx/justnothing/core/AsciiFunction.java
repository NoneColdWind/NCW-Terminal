package cn.ncbh.ncw.ncwjavafx.justnothing.core;

import java.util.HashMap;
import java.util.Map;

public class AsciiFunction {
    private static final Map<Character, String> ESCAPE_SEQUENCES = new HashMap<>();
    static {
        // 常见控制字符的转义序列
        ESCAPE_SEQUENCES.put('\b', "\\b");
        ESCAPE_SEQUENCES.put('\t', "\\t");
        ESCAPE_SEQUENCES.put('\n', "\\n");
        ESCAPE_SEQUENCES.put('\f', "\\f");
        ESCAPE_SEQUENCES.put('\r', "\\r");
    }

    public static String ascii(String input) {
        if (input == null) return "null";

        StringBuilder result = new StringBuilder();
        result.append('"');

        for (int i = 0; i < input.length();) {
            int codePoint = input.codePointAt(i);
            int charCount = Character.charCount(codePoint);
            i += charCount;

            if (codePoint == '"' || codePoint == '\\') {
                // 转义引号和反斜杠
                result.append('\\').append((char) codePoint);
            } else if (codePoint >= 32 && codePoint <= 126) {
                // ASCII 可打印字符直接添加
                result.append((char) codePoint);
            } else {
                // 处理控制字符和 Unicode 字符
                if (ESCAPE_SEQUENCES.containsKey((char) codePoint)) {
                    result.append(ESCAPE_SEQUENCES.get((char) codePoint));
                } else {
                    // 根据不同范围的字符使用不同的转义格式
                    if (codePoint < 256) {
                        result.append(String.format("\\x%02X", codePoint));
                    } else if (codePoint < 0x10000) {
                        result.append(String.format("\\u%04X", codePoint));
                    } else {
                        result.append(String.format("\\U%08X", codePoint));
                    }
                }
            }
        }

        result.append('"');
        return result.toString();
    }

    public static void main(String[] args) {
        System.out.println(ascii("Hello!"));        // "Hello!"
        System.out.println(ascii("Line\nBreak"));   // "Line\nBreak"
        System.out.println(ascii("\u00E5 and \u03A9")); // "\xE5 and \u03A9"
        System.out.println(ascii("Quote: \"\u201C\"")); // "Quote: \"\u201C\""
        System.out.println(ascii("\uD83D\uDE00"));     // "\U0001F600"（笑脸表情）
        System.out.println(ascii(null));             // "null"
        System.out.println(ascii("Tab\tChar!"));     // "Tab\tChar!"
        System.out.println(ascii("\u007F"));         // "\x7F"（DEL字符）
    }
}