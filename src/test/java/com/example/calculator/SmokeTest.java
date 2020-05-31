package com.example.calculator;

import com.example.calculator.controllers.CalculatorController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class SmokeTest {
    @Autowired
    private CalculatorController controller;

    @Test
    public void contextLoads() {
        assertThat(controller).isNotNull();
    }
}
