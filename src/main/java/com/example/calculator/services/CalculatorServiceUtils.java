package com.example.calculator.services;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import java.util.function.Function;

public class CalculatorServiceUtils {
    protected static final Map<Character, Integer> OPERATOR_PRIORITIES = new HashMap<Character, Integer>(){{
        put('*', 1);
        put('/', 1);
        put('+', 0);
        put('-', 0);
    }};

    protected final Function<Stack<Double>, Function<Stack<Character>, Void>> calcLastOpFunction = ns -> os -> {
        calcLastOp(ns, os);
        return null;
    };

    // all the methods aren't static for testing purposes
    protected double normalize(Double num) {
        return Double.parseDouble(new DecimalFormat("##.###").format(num));
    }

    protected void checkPushNum(StringBuilder currentNumBuilder, Stack<Double> numsStack) {
        if (currentNumBuilder.length() == 0) {
            return;
        }
        double currentNum = Double.parseDouble(currentNumBuilder.toString());
        currentNumBuilder.setLength(0);
        numsStack.push(currentNum);
    }

    protected void checkPushOp(char nextOp, Stack<Character> opsStack, Stack<Double> numsStack, Function<Stack<Double>, Function<Stack<Character>, Void>> foo) {
        // op is validated by expression validation in a controller layer

        if (opsStack.isEmpty()) {
            // just store the first op in the stack and exit
            opsStack.push(nextOp);
            return;
        }

        // if the ops in the stack can be calculated (regarding there prios), then do it
        // else just store the new op
        while (!opsStack.isEmpty()) {
            int lastPrio = OPERATOR_PRIORITIES.get(opsStack.peek());
            int nextPrio = OPERATOR_PRIORITIES.get(nextOp);
            if (lastPrio < nextPrio) {
                // the op in the stack is less prio then the new one and, thus, it can't be calculated now
                break;
            }
            foo.apply(numsStack).apply(opsStack);
        }
        // store the new op anyway
        opsStack.push(nextOp);
    }

    // Void instead of void is for testing purposes
    protected Void calcLastOp(Stack<Double> numsStack, Stack<Character> opsStack) {
        char op = opsStack.pop();
        double num2 = numsStack.pop();
        double num1 = numsStack.pop();
        double result = calcOp(op, num1, num2);
        numsStack.push(result);
        return null;
    }

    protected double calcOp(char op, double num1, double num2) {
        switch (op) {
            case '*':
                return num1 * num2;
            case '/':
                return num1 / num2;
            case '+':
                return num1 + num2;
            case '-':
            default:
                return num1 - num2;
        }
    }

}
