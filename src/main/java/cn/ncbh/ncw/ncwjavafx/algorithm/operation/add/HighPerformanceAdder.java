package cn.ncbh.ncw.ncwjavafx.algorithm.operation.add;

import java.util.Arrays;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class HighPerformanceAdder {
    // 自适应并行阈值
    private static final int PARALLEL_THRESHOLD;
    private static final int cores = 14;
    // SIMD模拟单元大小
    private static final int SIMD_UNIT = 4; // 模拟4元素向量处理
    // 缓存友好布局参数
    private static final int BLOCK_SIZE = 4096; // 适合L1缓存块
    private static final int VECTOR_SIZE = 64;  // 64字节缓存行

    // 自适应并行阈值计算
    static {
        int cores = Runtime.getRuntime().availableProcessors();
        // 动态阈值公式：基础值 / 核心数相关因子
        PARALLEL_THRESHOLD = Math.max(1024, 1000000 / (cores * 2));
    }

    // 任务窃取优化的线程池
    private static final ForkJoinPool pool = new ForkJoinPool(
            Math.max(2, cores * 2),
            ForkJoinPool.defaultForkJoinWorkerThreadFactory,
            null,
            true // 启用异步模式，优化任务窃取
    );

    // 优化的二维数组布局类
    private static class BlockArray {
        int[][] blocks;
        int blockCount;
        int maxDigits;

        public BlockArray(String num) {
            maxDigits = BLOCK_SIZE;
            blockCount = (num.length() + maxDigits - 1) / maxDigits;
            blocks = new int[blockCount][];

            // 从最高位开始处理
            int start = 0;
            for (int i = blockCount - 1; i >= 0; i--) {
                int end = Math.min(start + maxDigits, num.length());
                blocks[i] = parseBlock(num.substring(start, end));
                start = end;
            }
        }

        // 缓存友好的块处理
        private int[] parseBlock(String blockStr) {
            int len = blockStr.length();
            int vectorCount = (len + SIMD_UNIT - 1) / SIMD_UNIT;
            int[] block = new int[vectorCount];

            // SIMD风格的批处理
            for (int i = 0; i < vectorCount; i++) {
                int chunkStart = i * SIMD_UNIT;
                int chunkEnd = Math.min(chunkStart + SIMD_UNIT, len);
                block[i] = parseDigits(blockStr.substring(chunkStart, chunkEnd));
            }

            return block;
        }

        // 优化的数字解析
        private int parseDigits(String digits) {
            int value = 0;
            for (char c : digits.toCharArray()) {
                value = value * 10 + (c - '0');
            }
            return value;
        }
    }

    // 并行加法任务
    private static class AdditionTask extends RecursiveTask<int[]> {
        private final BlockArray a;
        private final BlockArray b;
        private final int startBlock;
        private final int endBlock;
        private final int maxDigits;

        public AdditionTask(BlockArray a, BlockArray b, int start, int end, int maxDigits) {
            this.a = a;
            this.b = b;
            this.startBlock = start;
            this.endBlock = end;
            this.maxDigits = maxDigits;
        }

        @Override
        protected int[] compute() {
            if (endBlock - startBlock <= PARALLEL_THRESHOLD / maxDigits) {
                return computeDirectly();
            }

            int mid = (startBlock + endBlock) / 2;
            AdditionTask left = new AdditionTask(a, b, startBlock, mid, maxDigits);
            AdditionTask right = new AdditionTask(a, b, mid, endBlock, maxDigits);

            left.fork();
            int[] rightResult = right.compute();
            int[] leftResult = left.join();

            // 合并结果
            return mergeResults(leftResult, rightResult);
        }

        private int[] computeDirectly() {
            int resultSize = (endBlock - startBlock) * maxDigits;
            int[] result = new int[resultSize];

            // 缓存预取变量
            int blockSize = maxDigits;

            for (int blockIdx = startBlock; blockIdx < endBlock; blockIdx++) {
                int[] aBlock = (blockIdx < a.blocks.length) ? a.blocks[blockIdx] : new int[0];
                int[] bBlock = (blockIdx < b.blocks.length) ? b.blocks[blockIdx] : new int[0];

                int offset = (blockIdx - startBlock) * blockSize;

                // 块内SIMD风格并行
                for (int vecStart = 0; vecStart < blockSize; vecStart += VECTOR_SIZE) {
                    int vecEnd = Math.min(vecStart + VECTOR_SIZE, blockSize);
                    addSegment(result, aBlock, bBlock, offset, vecStart, vecEnd);
                }
            }

            return result;
        }

        // 向量优化的段加法
        private void addSegment(int[] result, int[] aBlock, int[] bBlock,
                                int offset, int start, int end) {
            // 使用手动循环展开优化
            int i = start;
            int carry = 0;
            int baseDiv = (int)Math.pow(10, SIMD_UNIT);

            // 主向量循环
            for (; i <= end - 4; i += 4) {
                // 处理4个元素的向量
                for (int j = 0; j < 4; j++) {
                    int aVal = (i + j < aBlock.length) ? aBlock[i + j] : 0;
                    int bVal = (i + j < bBlock.length) ? bBlock[i + j] : 0;

                    // 同时计算四个位置的值
                    result[offset + i + j] = aVal + bVal + carry;

                    // 检查并处理进位
                    if (result[offset + i + j] >= baseDiv) {
                        result[offset + i + j] -= baseDiv;
                        carry = 1;
                    } else {
                        carry = 0;
                    }
                }
            }

            // 处理剩余元素
            for (; i < end; i++) {
                int aVal = (i < aBlock.length) ? aBlock[i] : 0;
                int bVal = (i < bBlock.length) ? bBlock[i] : 0;

                result[offset + i] = aVal + bVal + carry;

                if (result[offset + i] >= baseDiv) {
                    result[offset + i] -= baseDiv;
                    carry = 1;
                } else {
                    carry = 0;
                }
            }
        }

        private int[] mergeResults(int[] left, int[] right) {
            int[] combined = Arrays.copyOf(left, left.length + right.length);
            System.arraycopy(right, 0, combined, left.length, right.length);
            return combined;
        }
    }

    // 缓存友好的二维布局加法
    public static String staticAdd(String num1, String num2) {
        // 创建优化的块数组
        BlockArray a = new BlockArray(num1);
        BlockArray b = new BlockArray(num2);

        int maxBlocks = Math.max(a.blockCount, b.blockCount);

        // 执行并行加法
        int[] result = pool.invoke(new AdditionTask(a, b, 0, maxBlocks, a.maxDigits));

        // 处理进位传播
        propagateCarries(result, a.maxDigits);

        return formatResult(result, a.maxDigits);
    }

    // 统一进位传播算法
    private static void propagateCarries(int[] result, int blockSize) {
        int carry = 0;
        int baseDiv = (int)Math.pow(10, SIMD_UNIT);

        for (int i = 0; i < result.length; i++) {
            int total = result[i] + carry;

            if (total >= baseDiv) {
                carry = total / baseDiv;
                result[i] = total % baseDiv;
            } else {
                carry = 0;
                result[i] = total;
            }
        }

        // 处理最终进位
        if (carry > 0) {
            // 扩展数组
            int[] newResult = Arrays.copyOf(result, result.length + 1);
            newResult[result.length] = carry;
            result = newResult;
        }
    }

    // 格式化结果字符串
    private static String formatResult(int[] result, int blockSize) {
        StringBuilder sb = new StringBuilder(result.length * SIMD_UNIT);
        boolean leadingZero = true;

        for (int i = result.length - 1; i >= 0; i--) {
            String part = String.format("%0" + SIMD_UNIT + "d", result[i]);
            if (leadingZero) {
                // 跳过前导零
                int firstNonZero = 0;
                while (firstNonZero < part.length() && part.charAt(firstNonZero) == '0') {
                    firstNonZero++;
                }

                if (firstNonZero < part.length()) {
                    sb.append(part.substring(firstNonZero));
                    leadingZero = false;
                }
            } else {
                sb.append(part);
            }
        }

        return sb.isEmpty() ? "0" : sb.toString();
    }
}
