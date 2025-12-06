package cn.ncbh.ncw.ncwjavafx.justnothing;

import static cn.ncbh.ncw.ncwjavafx.justnothing.PyFunction.dir;
import static cn.ncbh.ncw.ncwjavafx.justnothing.PyFunction.open;

public class Test {

    public static void main(String[] args) {

        FileWorker f = open("input.txt", "w");
        f.write("114514");
        f.close();
        System.out.println(dir(Integer.class));

    }

}
