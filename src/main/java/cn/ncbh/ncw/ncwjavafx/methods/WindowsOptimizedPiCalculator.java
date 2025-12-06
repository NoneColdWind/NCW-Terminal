package cn.ncbh.ncw.ncwjavafx.methods;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class WindowsOptimizedPiCalculator {

    // Windows特化核心绑定实现
    private static class WindowsCoreBinder {
        static void bindToCore(Thread thread, int coreId) {
            // 在Windows上使用Java进程处理器关联API
            long processHandle = ProcessHandle.current().pid();
            try {
                // 使用Windows命令行实现核心绑定
                String[] commands = {
                        "cmd.exe", "/c",
                        "wmic process where \"ProcessId=" + processHandle +
                                "\" call setaffinity " + (1 << coreId)
                };

                Runtime.getRuntime().exec(commands).waitFor();
            } catch (Exception ex) {
                System.err.println("核心绑定失败: " + ex.getMessage());
            }
        }
    }

    // Windows特化的线程工厂
    private static class WindowsThreadFactory implements ThreadFactory {
        private final AtomicInteger threadNumber = new AtomicInteger(1);
        private final int coreCount;

        public WindowsThreadFactory() {
            this.coreCount = Runtime.getRuntime().availableProcessors();
        }

        @Override
        public Thread newThread(Runnable r) {
            int coreToBind = threadNumber.getAndIncrement() % coreCount;
            Thread t = new Thread(() -> {
                try {
                    // 在运行前绑定核心
                    WindowsCoreBinder.bindToCore(Thread.currentThread(), coreToBind);
                } catch (Exception ex) {
                    System.err.println("核心绑定失败: " + ex.getMessage());
                }
                r.run();
            }, "PiWorker-" + threadNumber);

            t.setDaemon(true);
            return t;
        }
    }

    // Windows优化的执行器服务
    private static ExecutorService createWindowsOptimizedExecutor() {
        int physicalCores = Runtime.getRuntime().availableProcessors();
        return new ThreadPoolExecutor(
                physicalCores, physicalCores,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(physicalCores * 2),
                new WindowsThreadFactory(),
                new ThreadPoolExecutor.CallerRunsPolicy()
        );
    }

    // 计算π的核心方法
    public static BigDecimal computePi(int decimalPlaces) throws Exception {
        final MathContext mc = new MathContext(decimalPlaces + 20, RoundingMode.HALF_EVEN);

        // 1. 计算迭代范围
        final int kMax = (int) (decimalPlaces / 14.18) + 10;
        System.out.println("迭代次数: " + kMax);

        // 2. 创建物理核心优化的执行器
        ExecutorService executor = createWindowsOptimizedExecutor();

        // 3. 划分任务
        int physicalCores = Runtime.getRuntime().availableProcessors();
        List<Future<BigDecimal>> futures = new ArrayList<>();
        int chunkSize = (kMax + physicalCores - 1) / physicalCores;

        for (int i = 0; i < physicalCores; i++) {
            int start = i * chunkSize;
            int end = Math.min(start + chunkSize, kMax);
            if (start >= end) break;

            futures.add(executor.submit(new PiChunkCalculator(start, end, mc)));
        }

        // 4. 收集结果
        BigDecimal sum = BigDecimal.ZERO;
        for (Future<BigDecimal> future : futures) {
            sum = sum.add(future.get(), mc);
        }

        executor.shutdown();

        // 5. 最终计算
        BigDecimal sqrt = computeSqrt(10005, decimalPlaces + 20);
        BigDecimal c = new BigDecimal(426980).multiply(sqrt, mc);

        return c.divide(sum, mc).setScale(decimalPlaces, RoundingMode.HALF_UP);
    }

    // 计算平方根
    private static BigDecimal computeSqrt(int value, int precision) {
        MathContext mc = new MathContext(precision, RoundingMode.HALF_EVEN);
        BigDecimal x = new BigDecimal(value);
        BigDecimal guess = new BigDecimal(Math.sqrt(value));

        // 牛顿迭代法
        for (int i = 0; i < 20; i++) {
            BigDecimal next = x.divide(guess, mc)
                    .add(guess)
                    .divide(BigDecimal.valueOf(2), mc);

            if (next.subtract(guess).abs().compareTo(
                    BigDecimal.ONE.scaleByPowerOfTen(-precision/2)) < 0) {
                return next;
            }
            guess = next;
        }
        return guess;
    }

    // π计算块
    private static class PiChunkCalculator implements Callable<BigDecimal> {
        private final int startK;
        private final int endK;
        private final MathContext mc;

        public PiChunkCalculator(int startK, int endK, MathContext mc) {
            this.startK = startK;
            this.endK = endK;
            this.mc = mc;
        }

        @Override
        public BigDecimal call() {
            return computeChunk();
        }

        private BigDecimal computeChunk() {
            BigDecimal sum = BigDecimal.ZERO;
            double logCubed = 3 * Math.log(640320);
            double sign = (startK % 2 == 0) ? 1.0 : -1.0;

            // 预计算阶乘对数缓存
            double[] logFactCache = precomputeLogFactorials();

            for (int k = startK; k < endK; k++) {
                // 使用对数避免大数计算
                try {
                    double numerator = logFactCache[6 * k - startK]
                            + Math.log(545140134.0 * k + 13591409.0);

                    double denominator = 3 * logFactCache[k - startK]
                            + logFactCache[3 * k - startK]
                            + k * logCubed;

                    double logTerm = numerator - denominator;
                    double termValue = sign * Math.exp(logTerm);

                    if (Double.isFinite(termValue)) {
                        // 使用高效加法
                        sum = sum.add(BigDecimal.valueOf(termValue), mc);
                    }
                } catch (Exception ex) {
                    System.err.printf("项k=%d计算失败: %s%n", k, ex.getMessage());
                }

                sign = -sign;
            }

            return sum;
        }

        // 预计算对数阶乘
        private double[] precomputeLogFactorials() {
            int max = 6 * (endK - 1) + 1;
            double[] cache = new double[max];

            cache[0] = 0; // log(1) = 0

            for (int n = 1; n < cache.length; n++) {
                if (n <= 20000) {
                    // 使用累加法计算精确的对数阶乘
                    cache[n] = cache[n - 1] + Math.log(n);
                } else {
                    // 大值使用Stirling近似公式
                    cache[n] = stirlingApprox(n);
                }
            }
            return cache;
        }

        private double stirlingApprox(int n) {
            return n * Math.log(n) - n + 0.5 * Math.log(2 * Math.PI * n);
        }
    }

    // Windows高性能时间计数器
    private static double windowsTickCount() {
        return System.nanoTime() / 1e9;
    }

    // 主方法
    public static void main(String[] args) {
        try {
            int digits = 1000000; // 计算1000位

            System.out.println("Windows优化版 π 计算器");
            System.out.println("检测到处理器: " + Runtime.getRuntime().availableProcessors() + " 核心");

            // 预热
            System.out.println("预热处理中...");
            computePi(100); // 小规模计算预热

            System.out.println("开始 " + digits + " 位计算...");
            double startTime = windowsTickCount();

            BigDecimal pi = computePi(digits);

            double endTime = windowsTickCount();
            double duration = endTime - startTime;

            System.out.println("计算完成!");
            System.out.println("Pi ≈ " + pi.toString().substring(0, 50) + "...");
            System.out.printf("总耗时: %.3f 秒%n", duration);

            // 保存结果到文件
            java.nio.file.Files.writeString(
                    java.nio.file.Path.of("pi_result.txt"),
                    pi.toString()
            );
            System.out.println("结果已保存到 pi_result.txt");

        } catch (Exception ex) {
            ex.printStackTrace();
            System.err.println("计算错误: " + ex.getMessage());
        }
    }
}