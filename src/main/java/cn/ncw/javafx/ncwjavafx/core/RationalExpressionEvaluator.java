package cn.ncw.javafx.ncwjavafx.core;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class RationalExpressionEvaluator {

    // 高性能高精度有理数类
    public static final class Rational {
        private final BigInteger numerator;
        private final BigInteger denominator;

        // 常用常量缓存
        public static final Rational ZERO = new Rational(BigInteger.ZERO, BigInteger.ONE);
        public static final Rational ONE = new Rational(BigInteger.ONE, BigInteger.ONE);

        private Rational(BigInteger numerator, BigInteger denominator) {
            if (denominator.equals(BigInteger.ZERO)) {
                throw new ArithmeticException("分母不能为零");
            }

            // 确保分母为正
            if (denominator.signum() < 0) {
                numerator = numerator.negate();
                denominator = denominator.negate();
            }

            // 约分
            BigInteger gcd = numerator.gcd(denominator);
            if (!gcd.equals(BigInteger.ONE)) {
                numerator = numerator.divide(gcd);
                denominator = denominator.divide(gcd);
            }

            this.numerator = numerator;
            this.denominator = denominator;
        }

        // ================== 工厂方法 ==================

        public static Rational of(long numerator, long denominator) {
            return of(BigInteger.valueOf(numerator), BigInteger.valueOf(denominator));
        }

        public static Rational of(BigInteger numerator, BigInteger denominator) {
            if (numerator.equals(BigInteger.ZERO)) return ZERO;
            if (numerator.equals(denominator)) return ONE;
            return new Rational(numerator, denominator);
        }

        public static Rational of(long value) {
            return of(BigInteger.valueOf(value), BigInteger.ONE);
        }

        public static Rational of(BigInteger value) {
            return of(value, BigInteger.ONE);
        }

        public static Rational of(BigDecimal decimal) {
            // 将BigDecimal转换为分数
            int scale = decimal.scale();
            BigInteger unscaled = decimal.unscaledValue();
            BigInteger denominator = BigInteger.TEN.pow(Math.abs(scale));
            return of(unscaled, denominator);
        }

        // 支持从字符串创建有理数
        public static Rational of(String s) {
            s = s.trim();

            // 处理分数形式 (如 "1/2", "-3/4")
            if (s.contains("/")) {
                String[] parts = s.split("/");
                if (parts.length != 2) {
                    throw new NumberFormatException("无效的分数格式: " + s);
                }

                BigInteger num = new BigInteger(parts[0].trim());
                BigInteger den = new BigInteger(parts[1].trim());
                return of(num, den);
            }

            // 处理小数形式 (如 "0.5", "-3.14")
            if (s.contains(".")) {
                try {
                    return of(new BigDecimal(s));
                } catch (NumberFormatException e) {
                    throw new NumberFormatException("无效的小数格式: " + s);
                }
            }

            // 处理整数形式 (如 "123", "-456")
            try {
                return of(new BigInteger(s));
            } catch (NumberFormatException e) {
                throw new NumberFormatException("无效的数字格式: " + s);
            }
        }

        // ================== 算术运算 ==================

        public Rational add(Rational other) {
            if (this.isZero()) return other;
            if (other.isZero()) return this;

            BigInteger num = this.numerator.multiply(other.denominator)
                    .add(other.numerator.multiply(this.denominator));
            BigInteger den = this.denominator.multiply(other.denominator);
            return of(num, den);
        }

        public Rational subtract(Rational other) {
            if (other.isZero()) return this;
            return this.add(other.negate());
        }

        public Rational multiply(Rational other) {
            if (this.isZero() || other.isZero()) return ZERO;
            if (this.isOne()) return other;
            if (other.isOne()) return this;

            BigInteger num = this.numerator.multiply(other.numerator);
            BigInteger den = this.denominator.multiply(other.denominator);
            return of(num, den);
        }

        public Rational divide(Rational other) {
            if (other.isZero()) {
                throw new ArithmeticException("不能除以零");
            }
            if (this.isZero()) return ZERO;
            if (other.isOne()) return this;

            return this.multiply(other.reciprocal());
        }

        // ================== 一元运算 ==================

        public Rational negate() {
            if (this.isZero()) return ZERO;
            return of(numerator.negate(), denominator);
        }

        public Rational reciprocal() {
            if (this.isZero()) {
                throw new ArithmeticException("零没有倒数");
            }
            return of(denominator, numerator);
        }

        public Rational abs() {
            return numerator.signum() >= 0 ? this : this.negate();
        }

        // ================== 辅助方法 ==================

        public boolean isZero() {
            return numerator.equals(BigInteger.ZERO);
        }

        public boolean isOne() {
            return numerator.equals(BigInteger.ONE) && denominator.equals(BigInteger.ONE);
        }

        @Override
        public String toString() {
            if (denominator.equals(BigInteger.ONE)) {
                return numerator.toString();
            }
            return numerator + "/" + denominator;
        }

        public String toDecimalString() {
            return new BigDecimal(numerator)
                    .divide(new BigDecimal(denominator), 10, RoundingMode.HALF_UP)
                    .stripTrailingZeros()
                    .toPlainString();
        }
    }

    // 表达式求值器
    public static Rational evaluateExpression(List<Object> tokens) {
        // 将中缀表达式转换为后缀表达式（逆波兰表示法）
        List<Object> rpn = toRPN(tokens);

        // 使用栈计算后缀表达式
        Stack<Rational> stack = new Stack<>();

        for (Object token : rpn) {
            if (token instanceof Rational) {
                stack.push((Rational) token);
            } else if (token instanceof Character) {
                char op = (Character) token;
                if (stack.size() < 2) {
                    throw new IllegalArgumentException("无效表达式：操作数不足");
                }

                Rational right = stack.pop();
                Rational left = stack.pop();

                switch (op) {
                    case '+':
                        stack.push(left.add(right));
                        break;
                    case '-':
                        stack.push(left.subtract(right));
                        break;
                    case '*':
                        stack.push(left.multiply(right));
                        break;
                    case '/':
                        stack.push(left.divide(right));
                        break;
                    default:
                        throw new IllegalArgumentException("未知运算符: " + op);
                }
            }
        }

        if (stack.size() != 1) {
            throw new IllegalArgumentException("无效表达式：表达式不完整");
        }

        return stack.pop();
    }

    // 将中缀表达式转换为后缀表达式（逆波兰表示法）
    private static List<Object> toRPN(List<Object> tokens) {
        List<Object> output = new ArrayList<>();
        Stack<Character> operators = new Stack<>();

        for (Object token : tokens) {
            if (token instanceof Rational) {
                output.add(token);
            } else if (token instanceof Character) {
                char c = (Character) token;

                if (c == '(') {
                    operators.push(c);
                } else if (c == ')') {
                    while (!operators.isEmpty() && operators.peek() != '(') {
                        output.add(operators.pop());
                    }
                    if (operators.isEmpty() || operators.peek() != '(') {
                        throw new IllegalArgumentException("括号不匹配");
                    }
                    operators.pop(); // 弹出 '('
                } else if (isOperator(c)) {
                    while (!operators.isEmpty() && precedence(operators.peek()) >= precedence(c)) {
                        output.add(operators.pop());
                    }
                    operators.push(c);
                } else {
                    throw new IllegalArgumentException("未知符号: " + c);
                }
            } else {
                throw new IllegalArgumentException("无效的token类型: " + token.getClass());
            }
        }

        while (!operators.isEmpty()) {
            char op = operators.pop();
            if (op == '(') {
                throw new IllegalArgumentException("括号不匹配");
            }
            output.add(op);
        }

        return output;
    }

    // 检查是否是运算符
    private static boolean isOperator(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/';
    }

    // 获取运算符优先级
    private static int precedence(char op) {
        return switch (op) {
            case '+', '-' -> 1;
            case '*', '/' -> 2;
            default -> 0;
        };
    }

    // 解析表达式字符串为token列表
    public static List<Object> parseExpression(String expression) {
        List<Object> tokens = new ArrayList<>();
        StringBuilder currentToken = new StringBuilder();

        for (int i = 0; i < expression.length(); i++) {
            char c = expression.charAt(i);

            if (Character.isWhitespace(c)) {
                // 忽略空格
                if (!currentToken.isEmpty()) {
                    tokens.add(Rational.of(currentToken.toString()));
                    currentToken.setLength(0);
                }
            } else if (isOperator(c) || c == '(' || c == ')') {
                // 处理运算符和括号
                if (!currentToken.isEmpty()) {
                    tokens.add(Rational.of(currentToken.toString()));
                    currentToken.setLength(0);
                }
                tokens.add(c);
            } else {
                // 处理数字部分
                currentToken.append(c);
            }
        }

        // 处理最后一个token
        if (!currentToken.isEmpty()) {
            tokens.add(Rational.of(currentToken.toString()));
        }

        return tokens;
    }

    // 测试方法
    public static void main(String[] args) {
        // 测试1: 简单表达式
        String expr1 = "1/2 + 1/3 * 1/4";
        List<Object> tokens1 = parseExpression(expr1);
        Rational result1 = evaluateExpression(tokens1);
        System.out.println("表达式: " + expr1);
        System.out.println("结果: " + result1 + " ≈ " + result1.toDecimalString());
        System.out.println();

        // 测试2: 带括号的表达式
        String expr2 = "(1/2 + 1/3) * (1/4 - 1/5) / (1/6)";
        List<Object> tokens2 = parseExpression(expr2);
        Rational result2 = evaluateExpression(tokens2);
        System.out.println("表达式: " + expr2);
        System.out.println("结果: " + result2 + " ≈ " + result2.toDecimalString());
        System.out.println();

        // 测试3: 混合表示法
        String expr3 = "0.5 + 1/3 * (0.25 - 1/5)";
        List<Object> tokens3 = parseExpression(expr3);
        Rational result3 = evaluateExpression(tokens3);
        System.out.println("表达式: " + expr3);
        System.out.println("结果: " + result3 + " ≈ " + result3.toDecimalString());
        System.out.println();

        // 测试4: 高精度计算
        String expr4 = "12345678901234567890 * 98765432109876543210 / 1234567890";
        List<Object> tokens4 = parseExpression(expr4);
        Rational result4 = evaluateExpression(tokens4);
        System.out.println("表达式: " + expr4);
        System.out.println("结果: " + result4);
        System.out.println("小数表示: " + result4.toDecimalString());
        System.out.println();

        // 测试5: 复杂表达式
        String expr5 = "(1.5 + 2.5) * (3/4 - 1/2) / (0.25)";
        List<Object> tokens5 = parseExpression(expr5);
        Rational result5 = evaluateExpression(tokens5);
        System.out.println("表达式: " + expr5);
        System.out.println("结果: " + result5 + " ≈ " + result5.toDecimalString());
        System.out.println();

    }
}