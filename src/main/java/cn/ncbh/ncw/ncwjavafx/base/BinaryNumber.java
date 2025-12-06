package cn.ncbh.ncw.ncwjavafx.base;

public class BinaryNumber {
    private final String binaryValue;

    public BinaryNumber(String binaryStr) {
        String trimmed = validateAndTrim(binaryStr);
        String normalized = removeLeadingZeros(trimmed);
        this.binaryValue = normalized;
    }

    private String validateAndTrim(String binaryStr) {
        if (binaryStr == null || binaryStr.isEmpty()) {
            throw new IllegalArgumentException("输入不能为空");
        }

        // 去除可能的0b前缀
        String trimmed = binaryStr.startsWith("0b") ? binaryStr.substring(2) : binaryStr;

        // 验证字符合法性
        for (char c : trimmed.toCharArray()) {
            if (c != '0' && c != '1') {
                throw new IllegalArgumentException("非法二进制字符: " + c);
            }
        }

        return trimmed;
    }

    private String removeLeadingZeros(String trimmedStr) {
        if (trimmedStr.isEmpty()) return "0"; // 处理空字符串（理论上不会发生）

        int startIndex = 0;
        // 找到第一个非零字符的位置
        while (startIndex < trimmedStr.length() - 1 && trimmedStr.charAt(startIndex) == '0') {
            startIndex++;
        }
        return trimmedStr.substring(startIndex);
    }

    public int compareTo(BinaryNumber other) {
        String thisValue = this.binaryValue;
        String otherValue = other.binaryValue;

        // 1. 比较有效长度
        if (thisValue.length() > otherValue.length()) {
            return 1;
        } else if (thisValue.length() < otherValue.length()) {
            return -1;
        }

        // 2. 长度相同，逐位比较
        for (int i = 0; i < thisValue.length(); i++) {
            char thisBit = thisValue.charAt(i);
            char otherBit = otherValue.charAt(i);
            if (thisBit != otherBit) {
                return thisBit > otherBit ? 1 : -1;
            }
        }

        return 0; // 完全相等
    }

    @Override
    public String toString() {
        return binaryValue;
    }
}
