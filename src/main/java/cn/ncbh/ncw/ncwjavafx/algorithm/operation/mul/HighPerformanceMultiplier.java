package cn.ncbh.ncw.ncwjavafx.algorithm.operation.mul;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class HighPerformanceMultiplier {
    // 核心优化参数
    private static final int PARALLEL_THRESHOLD;
    private static final int BASE_THRESHOLD = 16; // 基础乘法位数阈值
    private static final int SIMD_UNIT = 8;       // SIMD仿真单元大小
    private static final int BLOCK_SIZE = 2048;    // 缓存块大小（字节）

    // 任务窃取优化的ForkJoinPool
    private static final ForkJoinPool pool;

    static {
        // 自动扩展并行阈值
        int cores = Runtime.getRuntime().availableProcessors();
        PARALLEL_THRESHOLD = Math.max(128, 10000 / cores);

        // 工作窃取优化配置
        pool = new ForkJoinPool(
                Math.max(2, cores * 2),   // 并行度 = 核心数 * 2
                ForkJoinPool.defaultForkJoinWorkerThreadFactory,
                null,
                true  // 启用异步模式
        );
    }

    // 多级缓存优化数据结构
    private static class BlockArray {
        long[][] blocks;
        int blockCount;
        int maxDigits;

        public BlockArray(String num) {
            // 自动确定块大小
            maxDigits = Math.min(BLOCK_SIZE / 8, 128);
            blockCount = (num.length() + maxDigits - 1) / maxDigits;
            blocks = new long[blockCount][];

            // 填充数据块
            int start = 0;
            for (int i = 0; i < blockCount; i++) {
                int end = Math.min(start + maxDigits, num.length());
                blocks[i] = parseBlock(num.substring(start, end));
                start = end;
            }
        }

        // 解析块为长整型数组
        private long[] parseBlock(String blockStr) {
            int len = blockStr.length();
            int parts = (len + SIMD_UNIT - 1) / SIMD_UNIT;
            long[] block = new long[parts];

            for (int i = 0; i < parts; i++) {
                int chunkStart = i * SIMD_UNIT;
                int chunkEnd = Math.min(chunkStart + SIMD_UNIT, len);
                block[i] = parseDigits(blockStr.substring(chunkStart, chunkEnd));
            }

            return block;
        }

        // 高效解析数字
        private long parseDigits(String digits) {
            long value = 0;
            for (int i = 0; i < digits.length(); i++) {
                value = value * 10 + (digits.charAt(i) - '0');
            }
            return value;
        }
    }

    // 主乘法方法
    public static String multiply(String num1, String num2) {
        // 特殊情况处理
        if ("0".equals(num1) || "0".equals(num2)) return "0";

        // 创建缓存友好的数据结构
        BlockArray a = new BlockArray(num1);
        BlockArray b = new BlockArray(num2);

        // 使用并行Karatsuba算法
        long[][] result = karatsubaMultiply(a.blocks, b.blocks);

        // 处理进位和组合结果
        return formatResult(result);
    }

    // 并行Karatsuba乘法
    private static long[][] karatsubaMultiply(long[][] a, long[][] b) {
        int aLen = a.length;
        int bLen = b.length;

        // 基础乘法
        if (aLen < BASE_THRESHOLD || bLen < BASE_THRESHOLD) {
            return baseMultiply(a, b);
        }

        // 自动选择最佳分割点
        int mid = Math.max(aLen, bLen) / 2;

        // 分割矩阵
        long[][] aLow = Arrays.copyOfRange(a, 0, Math.min(mid, aLen));
        long[][] aHigh = mid < aLen ? Arrays.copyOfRange(a, mid, aLen) : new long[0][];
        long[][] bLow = Arrays.copyOfRange(b, 0, Math.min(mid, bLen));
        long[][] bHigh = mid < bLen ? Arrays.copyOfRange(b, mid, bLen) : new long[0][];

        // 并行计算三个乘法
        KaratsubaTask lowTask = new KaratsubaTask(aLow, bLow);
        KaratsubaTask highTask = new KaratsubaTask(aHigh, bHigh);
        KaratsubaTask midTask = new KaratsubaTask(
                addArrays(aLow, aHigh),
                addArrays(bLow, bHigh)
        );

        lowTask.fork();
        highTask.fork();
        long[][] midResult = midTask.compute();

        long[][] lowResult = lowTask.join();
        long[][] highResult = highTask.join();

        // Karatsuba组合公式: z2 * 10^(2m) + (z1 - z2 - z0) * 10^m + z0
        long[][] z1 = subtractArrays(
                subtractArrays(midResult, lowResult), highResult
        );

        // 合并结果
        return addArrays(
                shiftDigits(highResult, 2 * mid),
                addArrays(
                        shiftDigits(z1, mid),
                        lowResult
                )
        );
    }

    // 基础乘法（使用SIMD仿真优化）
    private static long[][] baseMultiply(long[][] a, long[][] b) {
        int aLen = a.length;
        int bLen = b.length;
        long[][] result = new long[aLen + bLen][];

        for (int i = 0; i < aLen; i++) {
            if (a[i] == null) continue;

            for (int j = 0; j < bLen; j++) {
                if (b[j] == null) continue;

                long[] temp = new long[a[i].length + b[j].length];
                simdMultiply(a[i], b[j], temp);

                // 合并到结果
                if (result[i + j] == null) {
                    result[i + j] = temp;
                } else {
                    result[i + j] = addVectors(result[i + j], temp);
                }
            }
        }

        return result;
    }

    // SIMD仿真乘法
    private static void simdMultiply(long[] a, long[] b, long[] result) {
        // 手动循环展开优化
        int i = 0;
        for (; i <= a.length - 4; i += 4) {
            for (int j = 0; j < b.length; j++) {
                // 一次处理4个元素
                result[i + j] += a[i] * b[j];
                result[i + j + 1] += a[i + 1] * b[j];
                result[i + j + 2] += a[i + 2] * b[j];
                result[i + j + 3] += a[i + 3] * b[j];
            }
        }

        // 处理剩余元素
        for (; i < a.length; i++) {
            for (int j = 0; j < b.length; j++) {
                result[i + j] += a[i] * b[j];
            }
        }
    }

    // Karatsuba并行任务
    private static class KaratsubaTask extends RecursiveTask<long[][]> {
        private final long[][] a;
        private final long[][] b;

        KaratsubaTask(long[][] a, long[][] b) {
            this.a = a;
            this.b = b;
        }

        @Override
        protected long[][] compute() {
            // 小任务直接计算
            if (a.length <= BASE_THRESHOLD || b.length <= BASE_THRESHOLD) {
                return baseMultiply(a, b);
            }

            // 超过并行阈值则并行处理
            if (a.length * b.length > PARALLEL_THRESHOLD) {
                return karatsubaMultiply(a, b);
            }

            // 小任务使用串行Karatsuba
            return serialKaratsuba(a, b);
        }

        // 串行Karatsuba（递归但不并行）
        private long[][] serialKaratsuba(long[][] a, long[][] b) {
            int aLen = a.length;
            int bLen = b.length;

            // 基础情况
            if (aLen < BASE_THRESHOLD || bLen < BASE_THRESHOLD) {
                return baseMultiply(a, b);
            }

            int mid = Math.max(aLen, bLen) / 2;

            long[][] aLow = Arrays.copyOfRange(a, 0, Math.min(mid, aLen));
            long[][] aHigh = mid < aLen ? Arrays.copyOfRange(a, mid, aLen) : new long[0][];
            long[][] bLow = Arrays.copyOfRange(b, 0, Math.min(mid, bLen));
            long[][] bHigh = mid < bLen ? Arrays.copyOfRange(b, mid, bLen) : new long[0][];

            long[][] lowResult = serialKaratsuba(aLow, bLow);
            long[][] highResult = serialKaratsuba(aHigh, bHigh);

            long[][] midResult = serialKaratsuba(
                    addArrays(aLow, aHigh),
                    addArrays(bLow, bHigh)
            );

            long[][] z1 = subtractArrays(
                    subtractArrays(midResult, lowResult), highResult
            );

            return addArrays(
                    shiftDigits(highResult, 2 * mid),
                    addArrays(
                            shiftDigits(z1, mid),
                            lowResult
                    )
            );
        }
    }

    // --- 向量操作工具方法 ---

    // 向量加法
    private static long[][] addArrays(long[][] a, long[][] b) {
        int maxLen = Math.max(a.length, b.length);
        long[][] result = new long[maxLen][];

        for (int i = 0; i < maxLen; i++) {
            if (i < a.length && a[i] != null) {
                if (i < b.length && b[i] != null) {
                    result[i] = addVectors(a[i], b[i]);
                } else {
                    result[i] = a[i].clone();
                }
            } else if (i < b.length && b[i] != null) {
                result[i] = b[i].clone();
            }
        }
        return result;
    }

    // 单个向量加法
    private static long[] addVectors(long[] a, long[] b) {
        int maxLen = Math.max(a.length, b.length);
        long[] result = new long[maxLen];
        int i;

        // 向量化风格处理
        for (i = 0; i < Math.min(a.length, b.length); i++) {
            result[i] = a[i] + b[i];
        }

        // 剩余部分
        if (a.length > b.length) {
            System.arraycopy(a, i, result, i, a.length - i);
        } else if (b.length > a.length) {
            System.arraycopy(b, i, result, i, b.length - i);
        }

        return result;
    }

    // 向量减法
    private static long[][] subtractArrays(long[][] a, long[][] b) {
        int maxLen = Math.max(a.length, b.length);
        long[][] result = new long[maxLen][];

        for (int i = 0; i < maxLen; i++) {
            if (i < a.length && a[i] != null) {
                if (i < b.length && b[i] != null) {
                    result[i] = subtractVectors(a[i], b[i]);
                } else {
                    result[i] = a[i].clone();
                }
            } else if (i < b.length && b[i] != null) {
                result[i] = new long[b[i].length];
                for (int j = 0; j < b[i].length; j++) {
                    result[i][j] = -b[i][j];
                }
            }
        }
        return result;
    }

    // 单个向量减法
    private static long[] subtractVectors(long[] a, long[] b) {
        int maxLen = Math.max(a.length, b.length);
        long[] result = new long[maxLen];
        int i;

        // 向量化风格处理
        for (i = 0; i < Math.min(a.length, b.length); i++) {
            result[i] = a[i] - b[i];
        }

        // 剩余部分
        if (a.length > b.length) {
            System.arraycopy(a, i, result, i, a.length - i);
        } else if (b.length > a.length) {
            for (int j = i; j < b.length; j++) {
                result[j] = -b[j];
            }
        }

        return result;
    }

    // 位偏移（相当于 * 10^digits）
    private static long[][] shiftDigits(long[][] array, int digits) {
        if (digits == 0) return array;

        long[][] result = new long[array.length + digits][];
        System.arraycopy(array, 0, result, digits, array.length);
        return result;
    }

    // 格式化最终结果
    private static String formatResult(long[][] matrix) {
        // 合并所有向量
        List<Long> digits = new ArrayList<>();
        long maxValue = (long)Math.pow(10, SIMD_UNIT) - 1;

        for (long[] vec : matrix) {
            if (vec != null) {
                for (long value : vec) {
                    digits.add(value);
                }
            }
        }

        // 处理进位
        int size = digits.size();
        for (int i = 0; i < size; i++) {
            long carry = digits.get(i) / maxValue;
            digits.set(i, digits.get(i) % maxValue);

            if (carry > 0) {
                if (i + 1 < size) {
                    digits.set(i + 1, digits.get(i + 1) + carry);
                } else {
                    digits.add(carry);
                    size++;
                }
            }
        }

        // 转换为字符串
        StringBuilder sb = new StringBuilder();
        boolean leadingZero = true;

        for (int i = digits.size() - 1; i >= 0; i--) {
            long part = digits.get(i);
            String partStr = String.format("%0" + SIMD_UNIT + "d", part);

            if (leadingZero) {
                int firstNonZero = 0;
                while (firstNonZero < partStr.length() && partStr.charAt(firstNonZero) == '0') {
                    firstNonZero++;
                }
                if (firstNonZero < partStr.length()) {
                    sb.append(partStr.substring(firstNonZero));
                    leadingZero = false;
                }
            } else {
                sb.append(partStr);
            }
        }

        return sb.isEmpty() ? "0" : sb.toString();
    }

    private static String generateLargeNumber(int digits) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder(digits);
        sb.append(random.nextInt(9) + 1); // 第一位非零

        for (int i = 1; i < digits; i++) {
            sb.append(random.nextInt(10));
        }

        return sb.toString();
    }
}
