package cn.ncbh.ncw.ncwjavafx.methods;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class UltraFastPiCalculator {

    // 常数定义
    private static final BigInteger C1 = BigInteger.valueOf(545140134);
    private static final BigInteger C2 = BigInteger.valueOf(13591409);
    private static final BigInteger C3 = BigInteger.valueOf(640320);
    private static final BigInteger C4 = BigInteger.valueOf(426880);
    private static final BigInteger C5 = BigInteger.valueOf(10005);
    private static final BigInteger C6 = BigInteger.valueOf(-262537412640768000L);

    public static void main(String[] args) throws Exception {
        int decimalPlaces = 1000000; // 1000位精度
        int threads = Runtime.getRuntime().availableProcessors(); // 使用所有核心

        System.out.println("开始计算 π (" + decimalPlaces + "位) 使用 " + threads + " 线程...");
        long startTime = System.nanoTime();

        BigDecimal pi = computePi(decimalPlaces, threads);

        long duration = System.nanoTime() - startTime;
        System.out.println("计算完成！");
        System.out.println("Pi ≈ " + pi.toString().substring(0, 50) + "...");
        System.out.printf("总耗时：%.3f 秒%n", duration / 1e9);
        System.out.println(pi);
    }

    public static BigDecimal computePi(int decimalPlaces, int threads) throws Exception {
        // 设置计算精度
        MathContext mc = new MathContext(decimalPlaces + 20, RoundingMode.HALF_EVEN);

        // 估算需要的迭代次数 (每项约增加14位精度)
        int kMax = (int) ((decimalPlaces / 14) + 10);
        System.out.println("迭代次数: " + kMax);

        // 使用线程池并行计算级数项
        ExecutorService executor = Executors.newFixedThreadPool(threads);
        List<Future<BigDecimal>> futures = new ArrayList<>();

        // 将任务分成多个块
        int chunkSize = Math.max(1, kMax / (threads * 4));
        for (int start = 0; start < kMax; start += chunkSize) {
            int end = Math.min(start + chunkSize, kMax);
            futures.add(executor.submit(new PiTermCalculator(start, end, mc)));
        }

        // 收集结果并求和
        BigDecimal sum = BigDecimal.ZERO;
        for (Future<BigDecimal> future : futures) {
            sum = sum.add(future.get(), mc);
        }
        executor.shutdown();

        // 计算常量 C = 426980 * √10005
        BigDecimal sqrt10005 = sqrt(C5, decimalPlaces + 20);
        BigDecimal c = new BigDecimal(C4).multiply(sqrt10005, mc);

        // π = C / Σ
        return c.divide(sum, mc).setScale(decimalPlaces, RoundingMode.HALF_UP);
    }

    /**
     * 计算级数项的任务 - 使用对数避免除零问题
     */
    private static class PiTermCalculator implements Callable<BigDecimal> {
        private final int startK;
        private final int endK;
        private final MathContext mc;

        public PiTermCalculator(int startK, int endK, MathContext mc) {
            this.startK = startK;
            this.endK = endK;
            this.mc = mc;
        }

        @Override
        public BigDecimal call() {
            BigDecimal sum = BigDecimal.ZERO;

            // 计算常数项
            double logCubed = 3 * Math.log(C3.doubleValue());
            double signFactor = (startK % 2 == 0) ? 1.0 : -1.0;

            for (int k = startK; k < endK; k++) {
                double sign = signFactor * (k % 2 == 0 ? 1 : -1);
                signFactor = -signFactor; // 为下一次迭代准备

                // 分子部分对数: log(|(-1)^k|) + log((6k)!) + log(|545140134k + 13591409|)
                double numerator = logFactorial(6 * k)
                        + Math.log(545140134.0 * k + 13591409.0);

                // 分母部分对数: 3*log(k!) + log((3k)!) + 3k*log(640320)
                double denominator = 3 * logFactorial(k)
                        + logFactorial(3 * k)
                        + k * logCubed;

                // 整体对数值
                double logTerm = numerator - denominator;

                // 转换为实际值并添加符号
                double termValue = sign * Math.exp(logTerm);

                // 确保不会添加无穷大或NaN
                if (Double.isFinite(termValue)) {
                    // 如果值特别小，可能舍入为零，但仍添加到总和中
                    sum = sum.add(BigDecimal.valueOf(termValue), mc);
                }
            }

            return sum;
        }

        /**
         * 使用斯特林公式近似计算阶乘的对数
         * n! ≈ √(2πn) * (n/e)^n
         */
        private double logFactorial(int n) {
            if (n == 0) return 0; // log(0!) = log(1) = 0
            return 0.5 * Math.log(2 * Math.PI * n) + n * (Math.log(n) - 1);
        }
    }

    /**
     * 高性能平方根计算 (牛顿迭代法)
     */
    private static BigDecimal sqrt(BigInteger x, int precision) {
        return sqrt(new BigDecimal(x), precision);
    }

    private static BigDecimal sqrt(BigDecimal x, int precision) {
        MathContext mc = new MathContext(precision, RoundingMode.HALF_EVEN);

        // 处理边界情况
        if (x.compareTo(BigDecimal.ZERO) == 0) return BigDecimal.ZERO;
        if (x.compareTo(BigDecimal.ZERO) < 0)
            throw new ArithmeticException("负数平方根: " + x);

        // 初始估值使用double近似值
        double xDouble = x.doubleValue();
        BigDecimal guess = BigDecimal.ZERO;

        // 确保初始猜测不为零
        if (xDouble > 0) {
            guess = new BigDecimal(Math.sqrt(xDouble), mc);
            if (guess.compareTo(BigDecimal.ZERO) == 0) {
                // 如果因为太小而计算为零，使用更安全的方法
                guess = x.divide(BigDecimal.valueOf(2), mc);
            }
        } else {
            guess = x.divide(BigDecimal.valueOf(2), mc);
        }

        BigDecimal tolerance = BigDecimal.ONE.scaleByPowerOfTen(-precision);

        // 牛顿迭代
        for (int i = 0; i < 20; i++) {
            // 避免除以零 - 添加安全检查
            if (guess.compareTo(BigDecimal.ZERO) == 0) {
                guess = BigDecimal.ONE.scaleByPowerOfTen(-precision/2);
            }

            BigDecimal next = x.divide(guess, mc)
                    .add(guess)
                    .divide(BigDecimal.valueOf(2), mc);

            if (next.subtract(guess).abs().compareTo(tolerance) < 0) {
                return next;
            }
            guess = next;
        }
        return guess;
    }
}