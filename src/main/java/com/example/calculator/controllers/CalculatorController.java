package com.example.calculator.controllers;

import com.example.calculator.models.CalculatorExpressionRequestModel;
import com.example.calculator.models.GenericResult;
import com.example.calculator.services.CalculatorService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.io.Serializable;

@RestController
public class CalculatorController {
    private final CalculatorService calculatorService;

    public CalculatorController(CalculatorService _calculatorService) {
        calculatorService = _calculatorService;
    }

    @PostMapping("/calc")
    public Serializable calc(@Valid @RequestBody CalculatorExpressionRequestModel requestExpressionModel) {
        Double result = calculatorService.calculate(requestExpressionModel.getExpression());
        return new GenericResult(result);
    }
}
