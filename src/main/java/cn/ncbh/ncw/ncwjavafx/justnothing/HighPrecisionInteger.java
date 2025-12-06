package cn.ncbh.ncw.ncwjavafx.justnothing;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HighPrecisionInteger {

    private String basic_num = "";
    private String other_num = "";
    private String final_result = "";

    public HighPrecisionInteger(String value) {
        this.basic_num = value;
        this.other_num = "";
        this.final_result = "";
    }

    public void setBasicNum(String value) {
        this.basic_num = value;
    }

    public void setOtherNum(String value) {
        this.other_num = value;
    }

    public String getBasicNum() {
        return this.basic_num;
    }

    public String getOtherNum() {
        return this.other_num;
    }

    public HighPrecisionInteger add(HighPrecisionInteger value) {
        return privateAdd(value);
    }

    public HighPrecisionInteger __abs__() {
        HighPrecisionInteger var = new HighPrecisionInteger("0");
        if (this.basic_num.startsWith("-")) {
            var.setBasicNum(Arrays.toString(this.basic_num.split("-")));
        }
        return var;
    }

    public String toString() {
        return this.basic_num;
    }

    private HighPrecisionInteger privateAdd(HighPrecisionInteger value) {

        this.other_num = value.getBasicNum();

        String[] arr_1 = this.basic_num.split("");
        String[] arr_2 = this.other_num.split("");

        List<Integer> list_1 = new ArrayList<>();
        List<Integer> list_2 = new ArrayList<>();
        List<Integer> list_3 = new ArrayList<>();

        List<String> result = new ArrayList<>();

        for (String string : arr_1) {
            list_1.add(Integer.parseInt(string));
        }

        for (String string : arr_2) {
            list_2.add(Integer.parseInt(string));
        }

        if (list_1.size() < list_2.size()) {

            int continueTimes = list_2.size() - list_1.size();

            for (int i = 0; i < continueTimes; i++) {
                list_1.addFirst(0);
            }

            list_1.addFirst(0);
            list_2.addFirst(0);

        } else if (list_1.size() > list_2.size()) {

            int continueTimes = list_1.size() - list_2.size();

            for (int i = 0; i < continueTimes; i++) {
                list_2.addFirst(0);
            }

            list_1.addFirst(0);
            list_2.addFirst(0);

        } else {

            list_1.addFirst(0);
            list_2.addFirst(0);

        }

        for (int i = 0; i < list_1.size(); i++) {
            list_3.add(0);
        }

        for (int i = list_1.size() - 1; i > 0; i--) {

            int var = list_1.get(i) + list_2.get(i) + list_3.get(i);
            List<Integer> list = divMod(var, 10);
            list_3.remove(i - 1);
            list_3.add(i - 1, list.getFirst());
            result.addFirst(list.getLast().toString());

        }

        for (String string : result) {
            this.final_result += string;
        }

        return new HighPrecisionInteger(this.final_result);
    }

    private List<Integer> divMod(int var1, int var2) {
        return Arrays.asList(var1 / var2, var1 % var2);
    }

    public static void main(String[] args) {
        System.out.println(new HighPrecisionInteger("2147483647").add(new HighPrecisionInteger("1")));
    }

}
