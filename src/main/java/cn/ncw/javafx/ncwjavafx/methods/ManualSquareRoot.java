package cn.ncw.javafx.ncwjavafx.methods;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;

public class ManualSquareRoot {

    /**
     * 计算任意精度的平方根
     * @param number 要开平方的数字（字符串形式）
     * @param scale 结果的小数位数（精度）
     * @return 平方根结果的字符串表示
     */
    public static String sqrt(String number, int scale) {
        // 验证输入有效性
        if (number == null || number.trim().isEmpty()) {
            throw new IllegalArgumentException("输入不能为空");
        }
        if (scale < 0) {
            throw new IllegalArgumentException("小数位数不能为负数");
        }

        BigDecimal n = new BigDecimal(number);
        // 处理负数输入
        if (n.signum() < 0) {
            throw new ArithmeticException("不能计算负数的实数平方根");
        }
        // 处理0和1的特殊情况
        if (n.compareTo(BigDecimal.ZERO) == 0) {
            return "0";
        }
        if (n.compareTo(BigDecimal.ONE) == 0) {
            return scale == 0 ? "1" : "1." + "0".repeat(scale);
        }

        // 1. 分离整数部分和小数部分
        BigInteger integerPart = n.toBigInteger();

        // 2. 计算整数平方根作为初始近似值
        BigDecimal initialGuess;
        if (integerPart.compareTo(BigInteger.ZERO) > 0) {
            BigInteger sqrtInt = integerSqrt(integerPart);
            initialGuess = new BigDecimal(sqrtInt);
        } else {
            // 对于小于1的数，初始猜测设为0.5
            initialGuess = BigDecimal.valueOf(0.5);
        }

        // 3. 应用牛顿迭代法进行精度优化
        return newtonRaphsonSqrt(n, initialGuess, scale + 2) // 多计算2位以确保精度
                .setScale(scale, RoundingMode.HALF_UP)
                .stripTrailingZeros()
                .toPlainString();
    }

    /**
     * 使用牛顿迭代法计算平方根
     *
     * 牛顿迭代公式：x_{n+1} = (x_n + S/x_n) / 2
     *
     * @param s 要计算平方根的数
     * @param guess 初始猜测值
     * @param scale 要求的小数精度
     * @return 平方根结果
     */
    private static BigDecimal newtonRaphsonSqrt(BigDecimal s, BigDecimal guess, int scale) {
        final BigDecimal two = BigDecimal.valueOf(2);
        final BigDecimal tolerance = BigDecimal.ONE.movePointLeft(scale + 1); // 容差值 10^(-scale-1)
        BigDecimal previous;

        // 设置高精度计算上下文
        MathContext mc = new MathContext(scale + 10, RoundingMode.HALF_UP);

        int iterationCount = 0;
        int maxIterations = 100; // 安全限制，防止无限循环

        do {
            iterationCount++;
            previous = guess;

            // 计算：guess = (guess + s/guess) / 2
            BigDecimal quotient = s.divide(guess, mc);
            guess = guess.add(quotient, mc)
                    .divide(two, mc);

            // 检查收敛情况
            if (iterationCount % 10 == 0) {
                // 使用相对误差检查收敛
                BigDecimal error = previous.subtract(guess).abs();
                if (error.compareTo(tolerance) < 0) {
                    break;
                }
            }
        } while (iterationCount < maxIterations);

        // 返回精度控制的结果
        return guess.round(new MathContext(scale + 2, RoundingMode.HALF_UP));
    }

    /**
     * 计算大整数的平方根（整数部分）
     * 使用位移优化的位算法
     *
     * @param n 要计算平方根的大整数
     * @return 平方根的整数部分
     */
    private static BigInteger integerSqrt(BigInteger n) {
        if (n.compareTo(BigInteger.ONE) <= 0) {
            return n;
        }

        // 使用比特长度估计初始值
        int bitLength = n.bitLength();
        BigInteger initial = BigInteger.ONE.shiftLeft((bitLength - 1) / 2);

        BigInteger result = n;
        BigInteger div;

        while (true) {
            div = n.divide(initial);
            if (div.compareTo(initial) < 0) {
                div = initial;
            }
            BigInteger newInitial = initial.add(div).shiftRight(1); // (x + n/x)/2
            if (newInitial.compareTo(initial) >= 0) {
                return initial;
            }
            initial = newInitial;
        }
    }

    // =============== 性能测试与使用示例 ===============


    // 辅助函数：测试平方根结果
    private static void testSqrt(String number, int scale, String expected) {
        String actual = sqrt(number, scale);
        if (!actual.equals(expected)) {
            System.err.printf("测试失败: √%s (精度%d)%n  预期: %s%n  实际: %s%n",
                    number, scale, expected, actual);
        } else {
            System.out.printf("测试通过: √%s (精度%d) = %s%n", number, scale, actual);
        }
    }

    // 辅助函数：比较不同规模数字的性能
    private static void comparePerformance(String number, int scale) {
        System.out.printf("%n性能测试: √%s (精度%d)%n", number, scale);

        long start1 = System.nanoTime();
        BigDecimal sqrtBD = new BigDecimal(sqrt(number, scale));
        long time1 = System.nanoTime() - start1;
        System.out.printf("自定义实现: %d 纳秒 (%.3f 毫秒)%n", time1, time1 / 1_000_000.0);

        // 与Math.sqrt比较（如果数字在double范围内）
        try {
            double d = Double.parseDouble(number);
            if (d > 0) {
                long start2 = System.nanoTime();
                double result = Math.sqrt(d);
                long time2 = System.nanoTime() - start2;
                System.out.printf("Math.sqrt: %d 纳秒 (%.3f 毫秒)", time2, time2 / 1_000_000.0);

                // 比较精度损失
                BigDecimal mathResult = new BigDecimal(result);
                BigDecimal diff = mathResult.subtract(sqrtBD).abs();
                System.out.printf(" 精度差异: %e%n", diff);
            }
        } catch (NumberFormatException e) {
            System.out.println("超出double范围 - 无法与Math.sqrt比较");
        }
    }
    public static void main(String[] args) {
        // 基本功能测试
        System.out.println(sqrt("114514.114", 2048));
    }
}
