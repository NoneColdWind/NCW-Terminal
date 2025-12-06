package cn.ncbh.ncw.ncwjavafx.base;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Objects;

public final class Rational extends Number implements Comparable<Rational> {
    private static final Rational ZERO = new Rational(BigInteger.ZERO, BigInteger.ONE);
    private static final Rational ONE = new Rational(BigInteger.ONE, BigInteger.ONE);

    private final BigInteger numerator;
    private final BigInteger denominator;

    // 私有构造函数，确保分母为正
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

    // 工厂方法创建有理数
    public static Rational valueOf(long numerator, long denominator) {
        return valueOf(BigInteger.valueOf(numerator), BigInteger.valueOf(denominator));
    }

    public static Rational valueOf(BigInteger numerator, BigInteger denominator) {
        return new Rational(numerator, denominator);
    }

    public static Rational valueOf(long value) {
        return valueOf(BigInteger.valueOf(value), BigInteger.ONE);
    }

    public static Rational valueOf(BigInteger value) {
        return valueOf(value, BigInteger.ONE);
    }

    public static Rational valueOf(BigDecimal decimal) {
        // 将BigDecimal转换为分数
        int scale = decimal.scale();
        BigInteger unscaled = decimal.unscaledValue();
        BigInteger denominator = BigInteger.TEN.pow(Math.abs(scale));
        return valueOf(unscaled, denominator);
    }

    public static Rational valueOf(String decimalStr) {
        return valueOf(new BigDecimal(decimalStr));
    }

    // 运算符重载方法
    public Rational add(Rational other) {
        BigInteger num = this.numerator.multiply(other.denominator)
                .add(other.numerator.multiply(this.denominator));
        BigInteger den = this.denominator.multiply(other.denominator);
        return valueOf(num, den);
    }

    public Rational subtract(Rational other) {
        return this.add(other.negate());
    }

    public Rational multiply(Rational other) {
        BigInteger num = this.numerator.multiply(other.numerator);
        BigInteger den = this.denominator.multiply(other.denominator);
        return valueOf(num, den);
    }

    public Rational divide(Rational other) {
        if (other.equals(ZERO)) {
            throw new ArithmeticException("不能除以零");
        }
        return this.multiply(other.reciprocal());
    }

    // 一元运算符
    public Rational negate() {
        return valueOf(numerator.negate(), denominator);
    }

    public Rational reciprocal() {
        return valueOf(denominator, numerator);
    }

    public Rational abs() {
        return numerator.signum() >= 0 ? this : this.negate();
    }

    // 比较方法
    @Override
    public int compareTo(Rational other) {
        BigInteger left = this.numerator.multiply(other.denominator);
        BigInteger right = other.numerator.multiply(this.denominator);
        return left.compareTo(right);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Rational other = (Rational) obj;
        return numerator.equals(other.numerator) && denominator.equals(other.denominator);
    }

    @Override
    public int hashCode() {
        return Objects.hash(numerator, denominator);
    }

    // 转换为其他数值类型
    @Override
    public int intValue() {
        return toBigDecimal().intValue();
    }

    @Override
    public long longValue() {
        return toBigDecimal().longValue();
    }

    @Override
    public float floatValue() {
        return toBigDecimal().floatValue();
    }

    @Override
    public double doubleValue() {
        return toBigDecimal().doubleValue();
    }

    public BigDecimal toBigDecimal() {
        return new BigDecimal(numerator).divide(new BigDecimal(denominator), MathContext.DECIMAL128);
    }

    public BigDecimal toBigDecimal(int scale, RoundingMode roundingMode) {
        return new BigDecimal(numerator).divide(new BigDecimal(denominator), scale, roundingMode);
    }

    // 获取分子分母
    public BigInteger getNumerator() {
        return numerator;
    }

    public BigInteger getDenominator() {
        return denominator;
    }

    // 字符串表示
    @Override
    public String toString() {
        if (denominator.equals(BigInteger.ONE)) {
            return numerator.toString();
        }
        return numerator + "/" + denominator;
    }

    public String toDecimalString(int precision) {
        return toBigDecimal(precision, RoundingMode.HALF_UP).toString();
    }

    // 运算符重载 - 使代码更自然
    public Rational plus(Rational other) {
        return add(other);
    }

    public Rational minus(Rational other) {
        return subtract(other);
    }

    public Rational times(Rational other) {
        return multiply(other);
    }

    public Rational div(Rational other) {
        return divide(other);
    }

    // 静态运算符方法，支持链式调用
    public static Rational add(Rational a, Rational b) {
        return a.add(b);
    }

    public static Rational subtract(Rational a, Rational b) {
        return a.subtract(b);
    }

    public static Rational multiply(Rational a, Rational b) {
        return a.multiply(b);
    }

    public static Rational divide(Rational a, Rational b) {
        return a.divide(b);
    }

    // 常量
    public static Rational zero() {
        return ZERO;
    }

    public static Rational one() {
        return ONE;
    }
}
