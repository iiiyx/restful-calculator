package com.example.calculator.services;

import test.utils.TestUtils;
import org.junit.jupiter.api.Test;

import java.util.Stack;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class CalculatorServiceUtilsTests {
    CalculatorServiceUtils spyCalculatorServiceUtils = spy(CalculatorServiceUtils.class);

    @Test
    public void normalizeMethodTest() {
        double result = spyCalculatorServiceUtils.normalize(0d);
        assertThat(result).isEqualTo(0.0);

        result = spyCalculatorServiceUtils.normalize(1.2);
        assertThat(result).isEqualTo(1.2);

        result = spyCalculatorServiceUtils.normalize(66666.6666666);
        assertThat(result).isEqualTo(66666.667);

        result = spyCalculatorServiceUtils.normalize(9.99999999);
        assertThat(result).isEqualTo(10.0);
    }

    @Test
    public void checkPushNumMethodShouldDoNothing() {
        StringBuilder sb = new StringBuilder();
        Stack<Double> stack = new Stack<>();

        spyCalculatorServiceUtils.checkPushNum(sb, stack);

        assertThat(sb).isBlank();
        assertThat(stack).isEmpty();
    }

    @Test
    public void checkPushNumMethodShouldPushValue() {
        StringBuilder sb = new StringBuilder("123.45678");
        Stack<Double> stack = new Stack<>();

        spyCalculatorServiceUtils.checkPushNum(sb, stack);

        assertThat(sb).isBlank();
        assertThat(stack).containsExactly(123.45678);
    }

    @Test
    public void checkPushOpShouldOnlyPushValueIfOpStackIsEmpty() {
        Stack<Character> stack = new Stack<>();
        Stack<Double> dummy = new Stack<>();
        // since validation is a controller (or even mvc) responsibility, we can use any char in the test
        char op = '.';
        spyCalculatorServiceUtils.checkPushOp(op, stack, dummy, spyCalculatorServiceUtils.calcLastOpFunction);

        assertThat(stack).containsExactly(op);
        assertThat(dummy).isEmpty();
    }

    @Test
    public void checkPushOpShouldOnlyPushValueIfNewOpIsHigherPrio() {
        Stack<Character> stack = new Stack<>();
        Stack<Double> dummy = new Stack<>();
        char op = '*';
        char prevOp = '-';
        stack.push(prevOp);
        spyCalculatorServiceUtils.checkPushOp(op, stack, dummy, spyCalculatorServiceUtils.calcLastOpFunction);

        assertThat(stack).containsExactly(prevOp, op);
        assertThat(dummy).isEmpty();
    }

    @Test
    public void checkPushOpShouldCalcAllPrevOpsIfPossible() {
        Stack<Character> opsStack = new Stack<>();
        char op1 = '+';
        char op2 = '*';
        char op3 = '/';
        opsStack.push(op1);
        opsStack.push(op2);
        opsStack.push(op3);
        Stack<Double> numsStack = new Stack<>();
        double num1 = 2d;
        double num2 = 3d;
        double num3 = 4d;
        double num4 = 5d;
        numsStack.push(num1);
        numsStack.push(num2);
        numsStack.push(num3);
        numsStack.push(num4);

        char newOp = '-';
        Function<Stack<Character>, Void> osFoo = os -> {
            os.pop();
            return null;
        };
        Function<Stack<Character>, Void> spyOsFoo = TestUtils.spyLambda(osFoo);

        Function<Stack<Double>, Function<Stack<Character>, Void>> nsFoo = ns -> spyOsFoo;
        Function<Stack<Double>, Function<Stack<Character>, Void>> spyNsFoo = TestUtils.spyLambda(nsFoo);

        spyCalculatorServiceUtils.checkPushOp(newOp, opsStack, numsStack, spyNsFoo);
        verify(spyNsFoo, times(3)).apply(numsStack);
        verify(spyOsFoo, times(3)).apply(opsStack);

        assertThat(opsStack).containsExactly(newOp);
        assertThat(numsStack).containsExactly(num1, num2, num3, num4);
    }

    @Test
    public void calcLastOpMethodTest() {
        Stack<Double> numsStack = new Stack<>();
        double num1 = 125.0;
        double num2 = 0.0025;
        double num3 = 4.0;
        numsStack.push(num1);
        numsStack.push(num2);
        numsStack.push(num3);
        Stack<Character> opsStack = new Stack<>();
        char op1 = '/';
        char op2 = '*';
        opsStack.push(op1);
        opsStack.push(op2);

        double opExpectedResult = 0.01;
        when(spyCalculatorServiceUtils.calcOp(op2, num2, num3)).thenReturn(opExpectedResult);

        spyCalculatorServiceUtils.calcLastOp(numsStack, opsStack);

        verify(spyCalculatorServiceUtils).calcOp(op2, num2, num3);
        assertThat(numsStack).containsExactly(num1, opExpectedResult);
        assertThat(opsStack).containsExactly(op1);
    }

    @Test
    public void calcOpMethodTest() {
        double result = spyCalculatorServiceUtils.calcOp('*', 2, 3.4444444);
        assertThat(result).isEqualTo(6.8888888);

        result = spyCalculatorServiceUtils.calcOp('/', 2, 3.4444444);
        assertThat(result).isEqualTo(0.5806451687825183);

        result = spyCalculatorServiceUtils.calcOp('+', 2, 3.4444444);
        assertThat(result).isEqualTo(5.4444444);

        result = spyCalculatorServiceUtils.calcOp('-', 2, 3.4444444);
        assertThat(result).isEqualTo(-1.4444444);

        result = spyCalculatorServiceUtils.calcOp('%', 2, 3.4444444);
        assertThat(result).isEqualTo(-1.4444444);
    }
}
