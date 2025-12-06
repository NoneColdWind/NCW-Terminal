package cn.ncbh.ncw.ncwjavafx.base;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class StringSplitter {
    public static List<Character> splitToCharList(String input) {
        if (input == null || input.isEmpty()) {
            return Collections.emptyList();
        }

        List<Character> result = new ArrayList<>();
        for (char c : input.toCharArray()) {
            result.add(c);
        }
        return result;
    }
}

