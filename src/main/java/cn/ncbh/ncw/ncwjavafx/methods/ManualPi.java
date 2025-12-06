package cn.ncbh.ncw.ncwjavafx.methods;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public class ManualPi {



    public static BigDecimal computePiWithChudnovsky(int decimalPlaces) {
        MathContext mc = new MathContext(decimalPlaces + 10, RoundingMode.HALF_EVEN);
        final BigDecimal C = new BigDecimal(426880).multiply(sqrt(new BigDecimal(10005), mc));

        BigDecimal sum = BigDecimal.ZERO;
        BigDecimal term;
        int k = 0;

        do {
            BigDecimal numerator = factorial(6 * k).multiply(
                    new BigDecimal(13591409).add(new BigDecimal(545140134).multiply(new BigDecimal(k)))
            );
            BigDecimal denominator = factorial(k).pow(3)
                    .multiply(factorial(3 * k))
                    .multiply(new BigDecimal(-262537412640768000L).pow(k));

            term = numerator.divide(denominator, mc);
            sum = sum.add(term);
            k++;
        } while (term.abs().compareTo(BigDecimal.ONE.divide(BigDecimal.TEN.pow(decimalPlaces + 5))) > 0);

        return C.divide(sum, mc).setScale(decimalPlaces, RoundingMode.HALF_UP);
    }

    // 计算平方根（牛顿迭代法）
    private static BigDecimal sqrt(BigDecimal x, MathContext mc) {
        BigDecimal guess = x.divide(BigDecimal.valueOf(2), mc);
        for (int i = 0; i < 10; i++) { // 迭代10次足够收敛
            guess = guess.add(x.divide(guess, mc)).divide(BigDecimal.valueOf(2), mc);
        }
        return guess;
    }

    // 计算阶乘（优化：避免重复计算）
    private static BigDecimal factorial(int n) {
        if (n == 0) return BigDecimal.ONE;
        BigDecimal result = BigDecimal.ONE;
        for (int i = 2; i <= n; i++) {
            result = result.multiply(BigDecimal.valueOf(i));
        }
        return result;
    }

    public static void main(String[] args) {
        int digits = 1145; // 设置计算精度（小数位数）
        long startTime = System.currentTimeMillis();
        BigDecimal pi = computePiWithChudnovsky(digits);
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        System.out.println("result = " + pi);
        System.out.println("Usage Time = " + duration);
    }
}
