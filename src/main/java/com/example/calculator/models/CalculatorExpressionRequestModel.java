package com.example.calculator.models;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class CalculatorExpressionRequestModel {
    @NotNull(message = Constants.NOT_NULL_MSG)
    @Pattern(regexp = Constants.EXPRESSION_PATTERN, message = Constants.NOT_MATCH_MSG)
    private String expression;

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }
}
