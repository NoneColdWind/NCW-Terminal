package cn.ncbh.ncw.ncwjavafx.methods;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;

public class ManualDivision {


    public static String div(String dividend, String divisor, int scale) {
        if (dividend.contains(".")) {
            return floatdiv(dividend, divisor, scale, RoundingMode.HALF_UP);
        } else if (divisor.contains(".")) {
            return floatdiv(dividend, divisor, scale, RoundingMode.HALF_UP);
        } else {
            return intdiv(dividend, divisor, scale);
        }
    }

    private static String intdiv(String dividend, String divisor, int scale) {
        // 处理除数为零
        if (new BigInteger(divisor).equals(BigInteger.ZERO)) {
            throw new ArithmeticException("Divisor cannot be zero");
        }

        // 处理正负号
        boolean negative = (dividend.startsWith("-") != divisor.startsWith("-"));
        dividend = dividend.replace("-", "");
        divisor = divisor.replace("-", "");


        // 整数部分除法
        BigInteger bigDividend = new BigInteger(dividend);
        BigInteger bigDivisor = new BigInteger(divisor);
        BigInteger integerPart = bigDividend.divide(bigDivisor);
        BigInteger remainder = bigDividend.remainder(bigDivisor);

        // 处理整除情况
        if (remainder.equals(BigInteger.ZERO) && scale == 0) {
            return (negative ? "-" : "") + integerPart.toString();
        }

        StringBuilder result = new StringBuilder(integerPart.toString());
        result.append(".");
        StringBuilder decimalPart = new StringBuilder();

        // 小数部分计算
        for (int i = 0; i < scale; i++) {
            if (remainder.equals(BigInteger.ZERO)) {
                break; // 余数为零时提前退出
            }
            remainder = remainder.multiply(BigInteger.TEN);
            BigInteger digit = remainder.divide(bigDivisor);
            decimalPart.append(digit);
            remainder = remainder.remainder(bigDivisor);
        }

        // 组合结果
        if (negative) result.insert(0, "-");
        return result.append(decimalPart.toString()).toString();
    }
    private static String floatdiv(String dividend, String divisor, int precision, RoundingMode roundingMode) {
        // 验证输入
        if (dividend == null || divisor == null) {
            throw new IllegalArgumentException("输入不能为 null");
        }

        // 创建 BigDecimal 对象
        BigDecimal num1 = new BigDecimal(dividend);
        BigDecimal num2 = new BigDecimal(divisor);

        // 检查除数是否为零
        if (num2.compareTo(BigDecimal.ZERO) == 0) {
            throw new ArithmeticException("除数不能为零");
        }

        // 创建数学上下文，指定精度和舍入模式
        MathContext mathContext = new MathContext(precision + 1, roundingMode);

        // 执行除法
        BigDecimal result = num1.divide(num2, mathContext);

        // 格式化输出
        return formatResult(result, precision);
    }

    /**
     * 格式化结果，移除不必要的尾随零
     *
     * @param result 计算结果
     * @param precision 精度
     * @return 格式化后的字符串
     */
    private static String formatResult(BigDecimal result, int precision) {
        // 转换为字符串
        String resultStr = result.toPlainString();

        // 如果不需要小数部分，直接返回整数形式
        if (precision == 0) {
            return resultStr.split("\\.")[0];
        }

        // 查找小数点的位置
        int decimalIndex = resultStr.indexOf('.');

        // 如果没有小数点，添加小数点并补零
        if (decimalIndex == -1) {
            return resultStr + "." + "0".repeat(precision);
        }

        // 获取整数部分和小数部分
        String integerPart = resultStr.substring(0, decimalIndex);
        String decimalPart = resultStr.substring(decimalIndex + 1);

        // 如果小数部分长度小于精度要求，补零
        if (decimalPart.length() < precision) {
            decimalPart = decimalPart + "0".repeat(precision - decimalPart.length());
        }
        // 如果小数部分长度大于精度要求，截断（但不应发生，因为MathContext已控制）
        else if (decimalPart.length() > precision) {
            decimalPart = decimalPart.substring(0, precision);
        }

        // 移除尾随零
        int lastNonZeroIndex = decimalPart.length() - 1;
        while (lastNonZeroIndex >= 0 && decimalPart.charAt(lastNonZeroIndex) == '0') {
            lastNonZeroIndex--;
        }

        // 如果所有小数位都是零，只返回整数部分
        if (lastNonZeroIndex < 0) {
            return integerPart;
        }

        // 返回格式化后的结果
        return integerPart + "." + decimalPart.substring(0, lastNonZeroIndex + 1);
    }

    public static void main(String[] args) {
        // 示例：计算22/7保留30位小数
        System.out.println(div("22", "7", 30));
        // 输出: 3.142857142857142857142857142857
    }
}
