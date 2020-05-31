package com.example.calculator.common;

import com.example.calculator.models.CalculatorExpressionRequestModel;
import com.example.calculator.models.GenericResult;
import com.example.calculator.services.CalculatorService;
import com.google.gson.Gson;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CommonWebTest {
    public static double RESULT = 6.5;
    public static void runCommonWebTest(MockMvc mockMvc, CalculatorService calculatorService) throws Exception {
        when(calculatorService.calculate(anyString())).thenReturn(CommonWebTest.RESULT);

        String expr = "1 + 2 * 3 - 0.5";
        CalculatorExpressionRequestModel requestModel = new CalculatorExpressionRequestModel();
        requestModel.setExpression(expr);

        Gson gson = new Gson();
        String content = gson.toJson(requestModel);
        MockHttpServletRequestBuilder requestBuilder = post("/calc")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);

        GenericResult expectedGenericResult = new GenericResult(RESULT);
        String expectedResult = gson.toJson(expectedGenericResult);

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo(expectedResult)));
    }
}
