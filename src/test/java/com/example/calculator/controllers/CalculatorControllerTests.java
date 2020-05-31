package com.example.calculator.controllers;

import com.example.calculator.services.CalculatorService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static com.example.calculator.common.CommonWebTest.runCommonWebTest;

@WebMvcTest(CalculatorController.class)
public class CalculatorControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CalculatorService calculatorService;

    @Test
    public void calcShouldReturnValueFromService() throws Exception {
        runCommonWebTest(this.mockMvc, this.calculatorService);
    }
}
