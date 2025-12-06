package cn.ncbh.ncw.ncwjavafx.methods;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class ManualFactorial {

    public static String factorial(int n) {
        // 处理边界情况
        if (n < 0) {
            throw new IllegalArgumentException("阶乘未定义负数");
        }
        if (n == 0 || n == 1) {
            return "1";
        }

        // 计算树形乘法结果
        BigInteger result = treeMultiply(1, n);

        // 优化输出格式：添加千位分隔符
        return formatWithCommas(result.toString());
    }

    /**
     * 树形乘法算法
     *
     * 将乘法操作组织为二叉树结构
     * 相比顺序乘法，将乘法次数从O(n)减少到O(log n)
     *
     * @param start 起始值
     * @param end 结束值
     * @return start * (start+1) * ... * end
     */
    private static BigInteger treeMultiply(int start, int end) {
        // 基线情况
        if (start == end) {
            return BigInteger.valueOf(start);
        }
        if (end - start == 1) {
            return BigInteger.valueOf((long) start * end);
        }

        // 递归分割
        int mid = (start + end) / 2;
        BigInteger left = treeMultiply(start, mid);
        BigInteger right = treeMultiply(mid + 1, end);

        return left.multiply(right);
    }

    /**
     * 使用BigInteger的树形乘法（备选方案）
     *
     * @param numbers 要相乘的数字列表
     * @return 乘积结果
     */
    private static BigInteger bigIntegerTreeMultiply(List<BigInteger> numbers) {
        int size = numbers.size();
        if (size == 1) {
            return numbers.get(0);
        }

        // 分割成两部分
        int mid = size / 2;
        List<BigInteger> leftPart = new ArrayList<>(numbers.subList(0, mid));
        List<BigInteger> rightPart = new ArrayList<>(numbers.subList(mid, size));

        // 递归计算左右部分
        return bigIntegerTreeMultiply(leftPart).multiply(bigIntegerTreeMultiply(rightPart));
    }

    /**
     * 格式化大数结果，添加千位分隔符
     *
     * @param numberStr 数字字符串
     * @return 添加千位分隔符后的字符串
     */
    private static String formatWithCommas(String numberStr) {
        StringBuilder formatted = new StringBuilder();
        int length = numberStr.length();

        // 从右向左每3位添加一个逗号
        for (int i = 0; i < length; i++) {
            if (i > 0 && (length - i) % 3 == 0) {
                formatted.append(',');
            }
            formatted.append(numberStr.charAt(i));
        }

        return formatted.toString();
    }

    /**
     * 估算阶乘结果的位数
     *
     * 使用Stirling近似公式：n! ≈ sqrt(2πn)(n/e)^n
     *
     * @param n 阶乘基数
     * @return 阶乘结果的位数估计值
     */
    public static int estimateDigits(int n) {
        if (n <= 1) return 1;

        // Stirling公式的对数形式
        double digits =
                Math.log10(2 * Math.PI * n) / 2 +
                        n * Math.log10(n / Math.E);

        return (int) Math.floor(digits) + 1;
    }

    /**
     * 计算阶乘的尾随零的个数
     *
     * @param n 阶乘基数
     * @return 阶乘结果中尾随零的数量
     */
    public static int trailingZeros(int n) {
        int count = 0;

        // 计算因子5的个数
        while (n > 0) {
            n /= 5;
            count += n;
        }

        return count;
    }


    private static void testFactorial(int n, String expected) {
        String result = factorial(n);
        if (!result.equals(expected)) {
            System.err.printf("测试失败: %d! = %s (预期: %s)%n", n, result, expected);
        } else {
            System.out.printf("测试通过: %d! = %s%n", n, result);
        }
    }

    private static void printDigitsEstimation(int n) {
        int digits = estimateDigits(n);
        System.out.printf("估计 %d! 的位数: %d%n", n, digits);
    }

    private static void printTrailingZeros(int n) {
        int zeros = trailingZeros(n);
        System.out.printf("%d! 的尾随零个数: %d%n", n, zeros);
    }

    private static void testPerformance(int n) {
        System.out.printf("%n计算 %d! 的性能测试:%n", n);

        // 估算位数
        int digits = estimateDigits(n);
        int zeros = trailingZeros(n);
        System.out.printf("结果位数估计: %,d 位, 尾随零: %,d%n", digits, zeros);

        // 树形乘法测试
        long start = System.nanoTime();
        String result = factorial(n);
        long time = System.nanoTime() - start;
        System.out.printf("树形乘法: %.3f ms (结果长度: %,d)%n", time / 1_000_000.0, result.length());
    }

    // ===== 测试与演示 =====
    public static void main(String[] args) {
        // 基本测试
        testFactorial(0, "1");
        testFactorial(1, "1");
        testFactorial(5, "120");
        testFactorial(10, "3,628,800");

        // 性能测试
        testPerformance(10);
        testPerformance(100);
        testPerformance(1000);
        testPerformance(5000);
        testPerformance(1919810); // 处理大约36,000位数字
    }
}
