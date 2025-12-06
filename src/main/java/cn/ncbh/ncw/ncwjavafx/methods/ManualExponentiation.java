package cn.ncbh.ncw.ncwjavafx.methods;

import java.math.BigInteger;

public class ManualExponentiation {

    /**
     * 迭代快速幂算法
     */
    private static BigInteger iterativeFastPower(BigInteger base, BigInteger exponent) {
        BigInteger result = BigInteger.ONE;

        while (exponent.compareTo(BigInteger.ZERO) > 0) {
            // 如果指数为奇数，乘一次底数
            if (exponent.testBit(0)) {
                result = result.multiply(base);
            }

            // 指数折半，底数平方
            base = base.multiply(base);
            exponent = exponent.shiftRight(1); // 等价于除以2
        }

        return result;
    }

    /**
     * 分治递归乘法（处理超大指数）
     */
    public static BigInteger pow(BigInteger base, BigInteger exponent) {
        // 指数为1：直接返回底数
        if (exponent.equals(BigInteger.ONE)) {
            return base;
        }

        // 指数为2：平方操作
        if (exponent.equals(BigInteger.valueOf(2))) {
            return base.multiply(base);
        }

        // 指数为3：优化特殊值
        if (exponent.equals(BigInteger.valueOf(3))) {
            return base.multiply(base).multiply(base);
        }

        // 二分递归：a^n = a^(n/2) * a^(n/2) * a^(n%2)
        BigInteger halfExponent = exponent.shiftRight(1); // n/2
        BigInteger remainder = exponent.remainder(BigInteger.valueOf(2));

        BigInteger halfPower = pow(base, halfExponent);
        BigInteger result = halfPower.multiply(halfPower);

        // 处理奇数次幂的余数部分
        if (remainder.equals(BigInteger.ONE)) {
            result = result.multiply(base);
        }

        return result;
    }

    private static String stripSign(String number) {
        if (number.startsWith("-") || number.startsWith("+")) {
            return number.substring(1);
        }
        return number;
    }

    // ===== 性能测试与使用示例 =====


    private static void testPerformance(String base, String exponent) {
        System.out.println("\n计算 " + base + "^" + exponent + " 的性能测试:");

        long start1 = System.currentTimeMillis();
        BigInteger iterative = iterativeFastPower(new BigInteger(base), new BigInteger(exponent));
        System.out.println("快速幂耗时: " + (System.currentTimeMillis() - start1) + "ms");

        long start2 = System.currentTimeMillis();
        BigInteger divideConquer = pow(new BigInteger(base), new BigInteger(exponent));
        System.out.println("分治算法耗时: " + (System.currentTimeMillis() - start2) + "ms");

        // 验证结果一致性
        if (!iterative.equals(divideConquer)) {
            System.out.println("结果不一致！");
        }
    }
    public static void main(String[] args) {
        // 基本幂运算
        long start2 = System.currentTimeMillis();
        pow(new BigInteger("114514"), new BigInteger("3145140"));
        System.out.println("分治算法耗时: " + (System.currentTimeMillis() - start2) + "ms");
    }
}
