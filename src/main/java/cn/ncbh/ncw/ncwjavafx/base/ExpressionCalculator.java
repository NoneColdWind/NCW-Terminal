package cn.ncbh.ncw.ncwjavafx.base;

import cn.ncbh.ncw.ncwjavafx.core.Operation;

import java.util.*;

import static cn.ncbh.ncw.ncwjavafx.base.CommonVariable.DEFAULT_FLOAT_PART_LENGTH;

public class ExpressionCalculator {
    // 定义运算符优先级
    private static final Map<Character, Integer> PRIORITY = Map.of(
            '+', 1,
            '-', 1,
            '*', 2,
            '/', 2,
            '(', 0
    );

    public static String calculate(List<Object> tokens) {
        Deque<String> numStack = new ArrayDeque<>();
        Deque<Character> opStack = new ArrayDeque<>();

        for (Object token : tokens) {
            if (token instanceof String) {
                numStack.push((String) token);
            } else if (token instanceof Character) {
                char c = (Character) token;
                if (c == '(') {
                    opStack.push(c);
                } else if (c == ')') {
                    // 处理括号内所有运算
                    while (opStack.peek() != '(') {
                        processOperation(numStack, opStack);
                    }
                    opStack.pop(); // 弹出左括号 '('
                } else {
                    // 处理高优先级运算
                    while (!opStack.isEmpty() &&
                            PRIORITY.getOrDefault(opStack.peek(), 0) >= PRIORITY.get(c)) {
                        processOperation(numStack, opStack);
                    }
                    opStack.push(c);
                }
            }
        }

        while (!opStack.isEmpty()) {
            processOperation(numStack, opStack);
        }
        return numStack.pop();
    }

    private static void processOperation(Deque<String> numStack, Deque<Character> opStack) {
        String b = numStack.pop(), a = numStack.pop();
        char op = opStack.pop();
        switch (op) {
            case '+': numStack.push(Operation.Addition(a, b)); break;
            case '-': numStack.push(Operation.Subtraction(a, b)); break;
            case '*': numStack.push(Operation.Multiplication(a, b)); break;
            case '/':
                if (Objects.equals(b, "0")) throw new ArithmeticException("除数不能为0");
                numStack.push(Operation.Division(a, b, DEFAULT_FLOAT_PART_LENGTH));
                break;
        }
    }
}