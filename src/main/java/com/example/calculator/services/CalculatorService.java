package com.example.calculator.services;

import org.springframework.stereotype.Service;

import java.util.Stack;
import java.util.function.Function;

import static com.example.calculator.services.CalculatorServiceUtils.OPERATOR_PRIORITIES;

@Service
public class CalculatorService {
    private CalculatorServiceUtils utils = new CalculatorServiceUtils();

    public double calculate(String expr) {
        // We don't need to validate expr here, because it was validated already on the controller's layer
        // I.e. in our REST service, validation is not the service responsibility and should be done outside and separately
        // But in the case of using this service outside of the controller, we need to check the input

        Stack<Character> opsStack = new Stack<>();
        Stack<Double> numsStack = new Stack<>();

        StringBuilder currentNumBuilder = new StringBuilder();

        for (int i = 0; i < expr.length(); i++) {
            char nextChar = expr.charAt(i);
            if (nextChar == ' ') {
                // if there was a number being collected, then finalize it
                utils.checkPushNum(currentNumBuilder, numsStack);
                continue;
            }
            if (OPERATOR_PRIORITIES.containsKey(nextChar)) {
                // if there was a number being collected, then finalize it
                utils.checkPushNum(currentNumBuilder, numsStack);
                utils.checkPushOp(nextChar, opsStack, numsStack, utils.calcLastOpFunction);
                continue;
            }

            currentNumBuilder.append(nextChar);
        }

        utils.checkPushNum(currentNumBuilder, numsStack);
        if (opsStack.isEmpty() && numsStack.size() == 1) {
            return utils.normalize(numsStack.peek());
        }

        while(!opsStack.isEmpty()) {
            utils.calcLastOp(numsStack, opsStack);
        }

        return utils.normalize(numsStack.peek());
    }
}

