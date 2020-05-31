package com.example.calculator.services;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class CalculatorServiceTests {
    // we can mock-spy CalculatorServiceUtils and track the calls and the args of its methods
    // but it will test what we have written (literally), not the logic!

    @Test
    public void calculateMethodGeneralTest() {
        String expr = "1 + 2 -3*4.25- 10 /8";
        double result = new CalculatorService().calculate(expr);
        assertThat(result).isEqualTo(-11.0);
    }

    @Test
    public void calculateMethodSingleNumberExpressionTest() {
        String expr = "100.500";
        double result = new CalculatorService().calculate(expr);
        assertThat(result).isEqualTo(100.500);
    }

    @Test
    public void calculateMethodResultNormalizationTest() {
        String expr = "2/3";
        double result = new CalculatorService().calculate(expr);
        assertThat(result).isEqualTo(0.667);
    }
}
