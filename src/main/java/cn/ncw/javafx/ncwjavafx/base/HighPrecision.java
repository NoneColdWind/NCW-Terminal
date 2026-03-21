package cn.ncw.javafx.ncwjavafx.base;

import cn.ncw.javafx.ncwjavafx.methods.*;

import java.math.BigInteger;

import static cn.ncw.javafx.ncwjavafx.base.CommonVariable.DEFAULT_FLOAT_PART_LENGTH;

public class HighPrecision {
    public String basic_num;

    public HighPrecision(String var) {
        basic_num = var;
    }

    public String add(String other_num) {
        return ManualAddition.add(basic_num, other_num);
    }

    public String sub(String other_num) {
        return ManualSubtraction.sub(basic_num, other_num);
    }

    public String mul(String other_num) {
        return ManualMultiplication.mul(basic_num, other_num);
    }

    public String div(String other_num) {
        return ManualDivision.div(basic_num, other_num, 64);
    }

    public String div(String other_num, Integer float_part_length) {
        float_part_length = (float_part_length == null) ? 64 : float_part_length;
        return ManualDivision.div(basic_num, other_num, float_part_length);
    }

    public String pow(String other_num) {
        if (other_num.startsWith("-")) {
            return new HighPrecision("1").div(ManualExponentiation.pow(new BigInteger(basic_num), new BigInteger(other_num.replace("-", ""))).toString(), Integer.valueOf(DEFAULT_FLOAT_PART_LENGTH));
        } else {
            return ManualExponentiation.pow(new BigInteger(basic_num), new BigInteger(other_num)).toString();
        }
    }

    public String sqrt() {
        return ManualSquareRoot.sqrt(basic_num, 64);
    }

    public String sqrt(Integer float_part_length) {
        float_part_length = (float_part_length == null) ? 64 : float_part_length;
        return ManualSquareRoot.sqrt(basic_num, float_part_length);
    }

}