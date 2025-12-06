package cn.ncbh.ncw.ncwjavafx.methods;

public class ManualAddition {

    /**
     * 手动实现任意精度加法
     * @param num1 字符串形式的数字
     * @param num2 字符串形式的数字
     * @return 加法结果字符串
     */
    public static String add(String num1, String num2) {
        // 预处理：处理符号和小数点
        boolean isNegative1 = num1.startsWith("-");
        boolean isNegative2 = num2.startsWith("-");
        boolean resultNegative = false;

        // 处理负数情况（简化版）
        if (isNegative1 && isNegative2) {
            // 都是负数：相加后结果为负
            num1 = num1.substring(1);
            num2 = num2.substring(1);
            resultNegative = true;
        } else if (isNegative1) {
            // 减法的逻辑，简化处理为抛出异常
            throw new UnsupportedOperationException("减法逻辑需单独实现");
        } else if (isNegative2) {
            throw new UnsupportedOperationException("减法逻辑需单独实现");
        }

        // 分离整数和小数部分
        String[] parts1 = num1.split("\\.");
        String[] parts2 = num2.split("\\.");

        // 获取整数和小数部分
        String intPart1 = parts1[0];
        String decPart1 = (parts1.length > 1) ? parts1[1] : "";

        String intPart2 = parts2[0];
        String decPart2 = (parts2.length > 1) ? parts2[1] : "";

        // 对齐小数部分
        while (decPart1.length() < decPart2.length()) decPart1 += "0";
        while (decPart2.length() < decPart1.length()) decPart2 += "0";

        // 分别处理整数和小数部分的加法
        String resultDec = addCore(decPart1, decPart2);
        String resultInt = addCore(intPart1, intPart2);

        // 小数进位处理
        if (resultDec.length() > decPart1.length()) {
            // 小数进位到整数
            String carryStr = resultDec.substring(0, 1);
            resultInt = addCore(resultInt, carryStr);
            resultDec = resultDec.substring(1);
        }

        // 构造最终结果
        String result = (resultDec.isEmpty())
                ? resultInt
                : resultInt + "." + resultDec;

        return resultNegative ? "-" + result : result;
    }

    /**
     * 核心加法逻辑（两个字符串数字相加）
     * @param s1 数字字符串1
     * @param s2 数字字符串2
     * @return 结果字符串
     */
    private static String addCore(String s1, String s2) {
        // 字符串反转以便从右向左计算
        StringBuilder a = new StringBuilder(s1).reverse();
        StringBuilder b = new StringBuilder(s2).reverse();

        // 保证长度一致
        while (a.length() < b.length()) a.append('0');
        while (b.length() < a.length()) b.append('0');

        int n = a.length();
        int carry = 0;
        StringBuilder res = new StringBuilder();

        // 逐位相加
        for (int i = 0; i < n; i++) {
            int digit1 = a.charAt(i) - '0';
            int digit2 = b.charAt(i) - '0';
            int sum = digit1 + digit2 + carry;
            carry = sum / 10;
            int digit = sum % 10;
            res.append(digit);
        }

        // 处理最高位的进位
        if (carry > 0) {
            res.append(carry);
        }

        return res.reverse().toString();
    }

    public static void main(String[] args) {
        // 测试整数加法
        System.out.println(add("1234567890123456789", "9876543210"));
        // 输出：1234567899999999999

        // 测试小数加法
        System.out.println(add("123.456", "789.12345"));
        // 输出：912.57945

        // 测试负数加法
        System.out.println(add("-999999999999.99", "-0.01"));
        // 输出：-1000000000000.00
    }
}