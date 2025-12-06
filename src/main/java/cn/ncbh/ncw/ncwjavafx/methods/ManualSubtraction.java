package cn.ncbh.ncw.ncwjavafx.methods;

import java.util.Objects;

public class ManualSubtraction {

    /**
     * 执行任意精度浮点数减法
     * @param minuend 被减数（字符串形式）
     * @param subtrahend 减数（字符串形式）
     * @return 减法结果的字符串表示
     */
    public static String sub(String minuend, String subtrahend) {
        // 处理空值
        if (minuend == null || minuend.isEmpty()) {
            return negate(subtrahend);
        }
        if (subtrahend == null || subtrahend.isEmpty()) {
            return minuend;
        }

        // 处理科学计数法 (如 "1.23e+5" -> "123000")
        minuend = normalizeScientificNotation(minuend);
        subtrahend = normalizeScientificNotation(subtrahend);

        // 处理符号
        boolean isMinuendNegative = minuend.startsWith("-");
        boolean isSubtrahendNegative = subtrahend.startsWith("-");

        // 移除符号
        String absMinuend = stripSign(minuend);
        String absSubtrahend = stripSign(subtrahend);

        // 比较绝对值大小
        int comparison = compareAbsoluteFloat(absMinuend, absSubtrahend);

        // 异号情况：A - (-B) = A + B 或 -A - B = -(A + B)
        if (isMinuendNegative != isSubtrahendNegative) {
            String result = addAbsoluteFloat(absMinuend, absSubtrahend);
            return (isMinuendNegative) ? "-" + result : result;
        }

        // 同号情况
        boolean resultNegative = isMinuendNegative;
        String result;

        if (comparison >= 0) { // |minuend| >= |subtrahend|
            result = subtractAbsoluteFloat(absMinuend, absSubtrahend);
            // 如果被减数更大但两个数都是负数，需要反转符号
            resultNegative = isMinuendNegative && !result.equals("0");
        } else { // |minuend| < |subtrahend|
            result = subtractAbsoluteFloat(absSubtrahend, absMinuend);
            resultNegative = !isMinuendNegative;
        }

        // 处理结果符号和规范格式
        return formatResult(result, resultNegative);
    }

    /**
     * 浮点数绝对值相减 (保证minuend >= subtrahend)
     */
    private static String subtractAbsoluteFloat(String num1, String num2) {
        // 特殊情况处理
        if (num2.equals("0")) return num1;
        if (num1.equals(num2)) return "0";

        // 分离整数和小数部分
        String[] parts1 = parseNumber(num1);
        String[] parts2 = parseNumber(num2);

        String intPart1 = parts1[0];
        String decPart1 = parts1[1];

        String intPart2 = parts2[0];
        String decPart2 = parts2[1];

        // 对齐小数部分（填充尾部零）
        int maxDecimalPlaces = Math.max(decPart1.length(), decPart2.length());
        decPart1 = padRight(decPart1, maxDecimalPlaces, '0');
        decPart2 = padRight(decPart2, maxDecimalPlaces, '0');

        // 组合成整数处理
        String fullNum1 = intPart1 + decPart1;
        String fullNum2 = intPart2 + decPart2;

        // 执行整数减法
        String result = subtractAbsoluteIntegers(fullNum1, fullNum2);

        // 添加小数点
        if (maxDecimalPlaces > 0) {
            // 小数点的位置（从右向左数）
            int decimalPointPosition = result.length() - maxDecimalPlaces;

            // 处理整数部分为零的情况
            if (decimalPointPosition <= 0) {
                // 前导零不足，需要补齐
                result = "0." + padLeft(result, maxDecimalPlaces + 1, '0').substring(1);
            } else {
                // 插入小数点
                result = result.substring(0, decimalPointPosition) +
                        "." + result.substring(decimalPointPosition);
            }
        }

        // 去除尾随零
        return removeTrailingZeros(result);
    }

    /**
     * 两个大整数字符串相减（保证num1 >= num2）
     */
    private static String subtractAbsoluteIntegers(String num1, String num2) {
        // 对齐长度（左侧补零）
        int maxLength = Math.max(num1.length(), num2.length());
        char[] a = padLeft(num1, maxLength, '0').toCharArray();
        char[] b = padLeft(num2, maxLength, '0').toCharArray();

        int length = a.length;
        int[] result = new int[length];
        int borrow = 0;

        // 从右向左逐位相减
        for (int i = length - 1; i >= 0; i--) {
            int digit1 = a[i] - '0' - borrow;
            int digit2 = b[i] - '0';
            borrow = 0;

            // 处理借位
            if (digit1 < digit2) {
                digit1 += 10;
                borrow = 1;
            }

            result[i] = digit1 - digit2;
        }

        // 构建结果字符串（移除前导零）
        return buildResultString(result);
    }

    // ===== 辅助方法 =====

    /**
     * 解析数字为整数和小数部分
     * @return [整数部分, 小数部分]
     */
    private static String[] parseNumber(String num) {
        // 处理科学计数法
        num = normalizeScientificNotation(num);

        // 处理小数点
        int decimalPointIndex = num.indexOf('.');

        if (decimalPointIndex == -1) {
            return new String[] {num, "0"}; // 无小数部分
        }

        String integerPart = num.substring(0, decimalPointIndex);
        String decimalPart = num.substring(decimalPointIndex + 1);

        // 处理整数部分为空的情况
        if (integerPart.isEmpty()) {
            integerPart = "0";
        }

        return new String[] {integerPart, decimalPart};
    }

    /**
     * 标准化科学计数法表示
     */
    private static String normalizeScientificNotation(String num) {
        if (num == null || !num.toUpperCase().contains("E")) {
            return num;
        }

        // 解析科学计数法
        String[] parts = num.toUpperCase().split("E");
        String base = parts[0];
        int exponent = Integer.parseInt(parts[1]);

        // 分离底数的整数和小数部分
        String[] baseParts = base.split("\\.");
        String baseInteger = baseParts[0];
        String baseDecimal = (baseParts.length > 1) ? baseParts[1] : "";

        // 计算总有效数字
        StringBuilder digits = new StringBuilder(baseInteger + baseDecimal);

        // 计算小数点需要移动的位置
        int decimalMove = exponent + (baseDecimal.length() - baseInteger.length());

        // 构建结果字符串
        if (decimalMove >= 0) {
            // 添加尾随零
            for (int i = 0; i < decimalMove; i++) {
                digits.append('0');
            }
            return digits.toString();
        } else {
            // 插入小数点
            int decimalPosition = digits.length() + decimalMove;
            if (decimalPosition <= 0) {
                // 前导零不足，需要补齐
                digits.insert(0, padLeft("", -decimalPosition + 1, '0'));
                return "0." + digits.toString().substring(1);
            } else {
                // 插入小数点
                digits.insert(decimalPosition, '.');
                return digits.toString();
            }
        }
    }

    /**
     * 移除符号前缀
     */
    private static String stripSign(String number) {
        if (number.startsWith("-") || number.startsWith("+")) {
            return number.substring(1);
        }
        return number;
    }

    /**
     * 取反操作
     */
    private static String negate(String number) {
        if (number.startsWith("-")) {
            return number.substring(1);
        }
        if (number.startsWith("+")) {
            return number.substring(1);
        }
        return "0".equals(number) ? "0" : "-" + number;
    }

    /**
     * 比较两个浮点数的绝对值大小
     */
    private static int compareAbsoluteFloat(String num1, String num2) {
        // 分离整数和小数部分
        String[] parts1 = parseNumber(num1);
        String[] parts2 = parseNumber(num2);

        String int1 = parts1[0];
        String dec1 = parts1[1];

        String int2 = parts2[0];
        String dec2 = parts2[1];

        // 先比较整数部分
        int intCompare = compareIntegers(int1, int2);
        if (intCompare != 0) {
            return intCompare;
        }

        // 整数部分相同，比较小数部分
        int maxLength = Math.max(dec1.length(), dec2.length());
        dec1 = padRight(dec1, maxLength, '0');
        dec2 = padRight(dec2, maxLength, '0');

        return compareIntegers(dec1, dec2);
    }

    /**
     * 比较两个大整数字符串大小
     */
    private static int compareIntegers(String num1, String num2) {
        // 去除前导零
        num1 = num1.replaceFirst("^0+(?!$)", "");
        num2 = num2.replaceFirst("^0+(?!$)", "");

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

        return 0; // 相等
    }

    /**
     * 浮点数绝对值相加
     */
    private static String addAbsoluteFloat(String num1, String num2) {
        // 特殊情况处理
        if ("0".equals(num1)) return num2;
        if ("0".equals(num2)) return num1;

        // 分离整数和小数部分
        String[] parts1 = parseNumber(num1);
        String[] parts2 = parseNumber(num2);

        String intPart1 = parts1[0];
        String decPart1 = parts1[1];

        String intPart2 = parts2[0];
        String decPart2 = parts2[1];

        // 对齐小数部分
        int maxDecimalPlaces = Math.max(decPart1.length(), decPart2.length());
        decPart1 = padRight(decPart1, maxDecimalPlaces, '0');
        decPart2 = padRight(decPart2, maxDecimalPlaces, '0');

        // 执行整数加法（小数部分单独加）
        String integerResult = addIntegers(intPart1, intPart2);
        String decimalResult = addIntegers(decPart1, decPart2);

        // 处理小数部分进位
        if (decimalResult.length() > maxDecimalPlaces) {
            // 小数部分产生了进位
            String carry = decimalResult.substring(0, decimalResult.length() - maxDecimalPlaces);
            integerResult = addIntegers(integerResult, carry);
            decimalResult = decimalResult.substring(decimalResult.length() - maxDecimalPlaces);
        }

        // 组合结果
        return removeTrailingZeros(integerResult + "." + decimalResult);
    }

    /**
     * 两个大整数字符串相加
     */
    private static String addIntegers(String num1, String num2) {
        // 反转字符串以便计算
        StringBuilder a = new StringBuilder(num1).reverse();
        StringBuilder b = new StringBuilder(num2).reverse();

        // 确保长度一致
        int maxLength = Math.max(a.length(), b.length());
        while (a.length() < maxLength) a.append('0');
        while (b.length() < maxLength) b.append('0');

        // 逐位相加
        StringBuilder result = new StringBuilder();
        int carry = 0;

        for (int i = 0; i < maxLength; i++) {
            int digit1 = a.charAt(i) - '0';
            int digit2 = b.charAt(i) - '0';
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
     * 构建结果字符串（移除前导零）
     */
    private static String buildResultString(int[] digits) {
        StringBuilder result = new StringBuilder();
        boolean leadingZero = true;

        for (int digit : digits) {
            if (leadingZero && digit == 0) continue;
            leadingZero = false;
            result.append(digit);
        }

        return result.length() == 0 ? "0" : result.toString();
    }

    /**
     * 右侧填充字符
     */
    private static String padRight(String str, int length, char padChar) {
        if (str.length() >= length) {
            return str;
        }
        return str + String.valueOf(padChar).repeat(length - str.length());
    }

    /**
     * 左侧填充字符
     */
    private static String padLeft(String str, int length, char padChar) {
        if (str.length() >= length) {
            return str;
        }
        return String.valueOf(padChar).repeat(length - str.length()) + str;
    }

    /**
     * 移除尾随零
     */
    private static String removeTrailingZeros(String num) {
        // 没有小数点直接返回
        if (!num.contains(".")) return num;

        // 分离整数和小数部分
        String integerPart = num;
        String decimalPart = "";

        if (num.contains(".")) {
            String[] parts = num.split("\\.");
            integerPart = parts[0];
            decimalPart = parts.length > 1 ? parts[1] : "";
        }

        // 移除小数部分的尾随零
        int endIndex = decimalPart.length();
        while (endIndex > 0 && decimalPart.charAt(endIndex - 1) == '0') {
            endIndex--;
        }
        decimalPart = decimalPart.substring(0, endIndex);

        // 处理整数部分为零的情况
        if (decimalPart.isEmpty()) {
            return integerPart;
        }

        return integerPart + "." + decimalPart;
    }

    /**
     * 格式规范化
     */
    private static String formatResult(String num, boolean isNegative) {
        // 处理零
        if (num.equals("0") || num.equals("0.0")) {
            return "0";
        }

        // 移除整数部分的前导零
        String formatted = num;
        if (formatted.startsWith("0") && formatted.length() > 1 && Character.isDigit(formatted.charAt(1))) {
            formatted = formatted.replaceFirst("^0+(?!$)", "");
        }

        // 添加符号
        return isNegative ? "-" + formatted : formatted;
    }

    private static void testSubtract(String a, String b, String expected) {
        String actual = sub(a, b);
        if (!Objects.equals(actual, expected)) {
            System.err.printf("测试失败: %s - %s = %s (预期: %s)%n", a, b, actual, expected);
        } else {
            System.out.printf("测试通过: %s - %s = %s%n", a, b, actual);
        }
    }
    // ===== 测试方法 =====
    public static void main(String[] args) {
        // 基础测试
        testSubtract("10.5", "3.2", "7.3");
        testSubtract("5.75", "2.25", "3.5");
        testSubtract("1.1", "0.2", "0.9");

        // 小数测试
        testSubtract("0.5", "0.4", "0.1");
        testSubtract("0.123", "0.122", "0.001");
        testSubtract("100.0", "99.999", "0.001");

        // 负数测试
        testSubtract("5.5", "-2.5", "8");
        testSubtract("-10.25", "4.75", "-15");
        testSubtract("-3.75", "-1.25", "-2.5");

        // 大数测试
        testSubtract(
                "12345678901234567890.1234567890",
                "9876543210987654321.987654321",
                "2469135690246913568.135802468"
        );

        // 边界测试
        testSubtract("0", "0", "0");
        testSubtract("0", "1.5", "-1.5");
        testSubtract("3.0", "0", "3.0");
        testSubtract("999.999", "1000", "-0.001");
        testSubtract("0.1", "0.0000000001", "0.0999999999");

        // 长小数测试
        testSubtract(
                "1.000000000000000000000000000001",
                "0.000000000000000000000000000001",
                "1"
        );

        System.out.println("所有测试通过！");
    }

}
