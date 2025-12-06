package cn.ncbh.ncw.ncwjavafx.methods;

import cn.ncbh.ncw.ncwjavafx.base.StringSplitter;

import java.util.List;
import java.util.Objects;

public class ManualMultiplication {

    public static String mul(String num1, String num2) {
        if (num1.contains(".")) {
            return floatmul(num1, num2);
        } else if (num2.contains(".")) {
            return floatmul(num1, num2);
        } else {
            return intmul(num1, num2);
        }
    }


    public static String floatmul(String num1, String num2) {
        List<Character> list_1 = StringSplitter.splitToCharList(num1);
        List<Character> list_2 = StringSplitter.splitToCharList(num2);

        int fpl1 = list_1.size() - list_1.indexOf('.');
        int fpl2 = list_2.size() - list_2.indexOf('.');

        String result_wait = intmul(num1.replace(".", ""), num2.replace(".", ""));
        List<Character> list_result = StringSplitter.splitToCharList(result_wait);
        int dot_index = list_result.size() - fpl1 - fpl2;
        list_result.add(dot_index, '.');
        StringBuilder result = new StringBuilder();
        for (Character character : list_result) {
            result.append(character);
        }
        return result.toString();
    }

    // ===== 主乘法方法 =====
    public static String intmul(String num1, String num2) {
        // 处理空值或零值
        if (num1 == null || num2 == null ||
                num1.isEmpty() || num2.isEmpty() ||
                "0".equals(num1) || "0".equals(num2)) {
            return "0";
        }

        // 解析符号
        boolean isNegative1 = num1.startsWith("-");
        boolean isNegative2 = num2.startsWith("-");
        boolean resultNegative = isNegative1 != isNegative2;

        // 移除符号和前导零
        num1 = stripSignAndLeadingZeros(num1);
        num2 = stripSignAndLeadingZeros(num2);

        // 处理其中一个为零的情况
        if ("0".equals(num1) || "0".equals(num2)) {
            return "0";
        }

        // 使用Karatsuba算法进行高效乘法
        String result = karatsubaMultiply(num1, num2);

        // 返回带符号的结果
        return resultNegative ? "-" + result : result;
    }

    // ===== Karatsuba分治算法 =====
    private static String karatsubaMultiply(String num1, String num2) {
        int len1 = num1.length();
        int len2 = num2.length();

        // 当数字较小时，使用简单乘法
        if (len1 < 10 || len2 < 10) {
            return simpleMultiply(num1, num2);
        }

        // 确定分割点（取较大位数的一半）
        int n = Math.max(len1, len2);
        int half = (n + 1) / 2;

        // 分割num1为高位和低位
        String a = len1 <= half ? "0" : num1.substring(0, len1 - half);
        String b = num1.substring(Math.max(0, len1 - half));

        // 分割num2为高位和低位
        String c = len2 <= half ? "0" : num2.substring(0, len2 - half);
        String d = num2.substring(Math.max(0, len2 - half));

        // 计算三部分乘法
        String ac = karatsubaMultiply(a, c);      // a * c
        String bd = karatsubaMultiply(b, d);      // b * d

        // 计算(a+b)*(c+d) - ac - bd (即ad + bc)
        String abSum = add(a, b);
        String cdSum = add(c, d);
        String abcd = karatsubaMultiply(abSum, cdSum);
        String ad_bc = subtract(subtract(abcd, ac), bd);

        // 组合结果: ac * 10^(2*half) + (ad + bc) * 10^half + bd
        return add(
                add(
                        shiftLeft(ac, 2 * half),
                        shiftLeft(ad_bc, half)
                ),
                bd
        );
    }

    // ===== 基本运算功能 =====

    /**
     * 简单乘法实现（用于基础情况）
     */
    private static String simpleMultiply(String num1, String num2) {
        // 反转字符串便于计算
        num1 = new StringBuilder(num1).reverse().toString();
        num2 = new StringBuilder(num2).reverse().toString();

        // 创建结果数组
        int[] result = new int[num1.length() + num2.length()];

        // 逐位相乘
        for (int i = 0; i < num1.length(); i++) {
            int digit1 = num1.charAt(i) - '0';
            for (int j = 0; j < num2.length(); j++) {
                int digit2 = num2.charAt(j) - '0';
                result[i + j] += digit1 * digit2;
            }
        }

        // 处理进位
        int carry = 0;
        for (int i = 0; i < result.length; i++) {
            int sum = carry + result[i];
            result[i] = sum % 10;
            carry = sum / 10;
        }

        // 构建结果字符串
        StringBuilder sb = new StringBuilder();
        for (int digit : result) {
            sb.insert(0, digit);
        }

        // 去除前导零
        return trimLeadingZeros(sb.toString());
    }

    /**
     * 字符串加法
     */
    private static String add(String num1, String num2) {
        // 处理空值
        if (num1 == null || num1.isEmpty()) return num2;
        if (num2 == null || num2.isEmpty()) return num1;

        // 反转字符串便于计算
        num1 = new StringBuilder(num1).reverse().toString();
        num2 = new StringBuilder(num2).reverse().toString();

        // 确保相同长度
        int maxLength = Math.max(num1.length(), num2.length());
        num1 = padRight(num1, maxLength, '0');
        num2 = padRight(num2, maxLength, '0');

        // 逐位相加
        StringBuilder result = new StringBuilder();
        int carry = 0;

        for (int i = 0; i < maxLength; i++) {
            int digit1 = num1.charAt(i) - '0';
            int digit2 = num2.charAt(i) - '0';
            int sum = digit1 + digit2 + carry;

            carry = sum / 10;
            result.append(sum % 10);
        }

        // 处理最高位进位
        if (carry > 0) {
            result.append(carry);
        }

        return result.reverse().toString();
    }

    /**
     * 字符串减法（num1 - num2，假设num1 >= num2）
     */
    private static String subtract(String num1, String num2) {
        // 相等情况处理
        if (num1.equals(num2)) return "0";

        // 确保num1 >= num2
        if (compare(num1, num2) < 0) {
            throw new IllegalArgumentException("num1 must be >= num2 for subtraction");
        }

        // 反转字符串便于计算
        num1 = new StringBuilder(num1).reverse().toString();
        num2 = new StringBuilder(num2).reverse().toString();

        // 对齐长度
        num2 = padRight(num2, num1.length(), '0');

        // 逐位相减
        StringBuilder result = new StringBuilder();
        int borrow = 0;

        for (int i = 0; i < num1.length(); i++) {
            int digit1 = num1.charAt(i) - '0' - borrow;
            int digit2 = num2.charAt(i) - '0';

            borrow = 0; // 重置借位

            // 如果需要借位
            if (digit1 < digit2) {
                digit1 += 10;
                borrow = 1;
            }

            result.append(digit1 - digit2);
        }

        // 去除前导零
        return trimLeadingZeros(result.reverse().toString());
    }

    // ===== 辅助函数 =====

    /**
     * 移除符号和前导零
     */
    private static String stripSignAndLeadingZeros(String num) {
        // 移除符号
        if (num.startsWith("-") || num.startsWith("+")) {
            num = num.substring(1);
        }

        // 移除前导零
        return trimLeadingZeros(num);
    }

    /**
     * 去除前导零
     */
    private static String trimLeadingZeros(String num) {
        // 保留最后一位（如果是零）
        if (num.length() == 1) return num;

        // 找到第一个非零字符的位置
        int start = 0;
        while (start < num.length() - 1 && num.charAt(start) == '0') {
            start++;
        }

        return num.substring(start);
    }

    /**
     * 比较两个数字字符串
     * @return -1 if num1 < num2, 0 if equal, 1 if num1 > num2
     */
    private static int compare(String num1, String num2) {
        // 比较长度
        if (num1.length() != num2.length()) {
            return num1.length() > num2.length() ? 1 : -1;
        }

        // 长度相等，逐位比较
        for (int i = 0; i < num1.length(); i++) {
            char c1 = num1.charAt(i);
            char c2 = num2.charAt(i);

            if (c1 != c2) {
                return c1 > c2 ? 1 : -1;
            }
        }

        return 0;
    }

    /**
     * 数字左移（乘以10的n次方）
     */
    private static String shiftLeft(String num, int positions) {
        if ("0".equals(num) || positions == 0) {
            return num;
        }

        StringBuilder sb = new StringBuilder(num);
        for (int i = 0; i < positions; i++) {
            sb.append('0');
        }

        return sb.toString();
    }

    /**
     * 右侧填充字符
     */
    private static String padRight(String num, int length, char padChar) {
        if (num.length() >= length) {
            return num;
        }

        StringBuilder sb = new StringBuilder(num);
        while (sb.length() < length) {
            sb.append(padChar);
        }

        return sb.toString();
    }

    private static void testMultiply(String a, String b, String expected) {
        String result = mul(a, b);
        if (!Objects.equals(result, expected)) {
            System.err.printf("测试失败: %s * %s = %s (预期: %s)%n", a, b, result, expected);
        }
    }
    // ===== 测试方法 =====
    public static void main(String[] args) {
        testMultiply("123", "456", "56088");
        testMultiply("999", "999", "998001");
        testMultiply("2", "3", "6");
        testMultiply("10", "10", "100");
        testMultiply("0", "12345", "0");
        testMultiply("-5", "6", "-30");
        testMultiply("-7", "-8", "56");

        // 大数测试
        testMultiply("123456789", "987654321", "121932631112635269");
        testMultiply(
                "12345678901234567890",
                "98765432109876543210",
                "1219326311370217952237463801111263526900"
        );

        // Karatsuba边界测试
        testMultiply("1234", "5678", "7006652");

        System.out.println("所有测试通过!");
    }


}
