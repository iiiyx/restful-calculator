package com.example.calculator;

import com.example.calculator.common.CommonWebTest;
import com.example.calculator.models.CalculatorExpressionRequestModel;
import com.example.calculator.models.Constants;
import com.example.calculator.models.GenericResult;
import com.example.calculator.services.CalculatorService;
import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = CalculatorApplication.class, webEnvironment = WebEnvironment.RANDOM_PORT)
public class HttpRequestTest {
    String url;
    HttpHeaders headers;
    Gson gson;

    @LocalServerPort
    private int port;

    @MockBean
    private CalculatorService calculatorService;

    @Autowired
    private TestRestTemplate restTemplate;

    @BeforeEach
    private void prepare() {
        url = "http://localhost:" + port + "/calc";
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        gson = new Gson();
    }

    @Test
    public void calcShouldReturnValue() {
        when(calculatorService.calculate(anyString())).thenReturn(CommonWebTest.RESULT);

        CalculatorExpressionRequestModel requestModel = new CalculatorExpressionRequestModel();
        requestModel.setExpression("1 + 2 * 3 - 0.5");

        String requestBody = gson.toJson(requestModel);
        HttpEntity<String> request =
                new HttpEntity<>(requestBody, headers);

        String result = this.restTemplate.postForObject(url, request, String.class);
        GenericResult expectedGenericResult = new GenericResult(CommonWebTest.RESULT);

        String expectedResult = gson.toJson(expectedGenericResult);

        assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    public void calcShouldReturnErrorOnNullExpression() {
        testExpressionNull();
    }

    @Test
    public void calcShouldReturnErrorOnEmptyExpression() {
        testExpressionMismatch("");
    }

    @Test
    public void calcShouldReturnErrorOnExpressionMismatch() {
        testExpressionMismatch("+");
        testExpressionMismatch("1+");
        testExpressionMismatch("+1");
        testExpressionMismatch("1.");
        testExpressionMismatch("1+1.");
        testExpressionMismatch("1. +1");
        testExpressionMismatch("1 .0+2");
        testExpressionMismatch("1+2*3-");
        testExpressionMismatch("(1+2)");
        testExpressionMismatch("1^2");
    }

    private void testExpressionNull() {
        testExpressionError(null, Constants.NOT_NULL_MSG);
    }

    private void testExpressionMismatch(String expr) {
        testExpressionError(expr, Constants.NOT_MATCH_MSG);
    }

    private void testExpressionError(String expr, String expectedError) {
        CalculatorExpressionRequestModel requestModel = new CalculatorExpressionRequestModel();
        String requestBody;
        HttpEntity<String> request;
        String result;
        ErrorsContainer errorsContainer;

        requestModel.setExpression(expr);
        requestBody = gson.toJson(requestModel);
        request = new HttpEntity<>(requestBody, headers);

        result = this.restTemplate.postForObject(url, request, String.class);
        errorsContainer = gson.fromJson(result, ErrorsContainer.class);

        assertThat(errorsContainer.errors).containsExactly(expectedError);
    }

    private static class ErrorsContainer {
        private String[] errors;

        public String[] getErrors() {
            return errors;
        }

        public void setErrors(String[] errors) {
            this.errors = errors;
        }
    }
}