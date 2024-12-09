package com.rest.rest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class CalculatorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testAdd() throws Exception {
        mockMvc.perform(get("/api/calculator/sum")
                        .param("a", "1.5")
                        .param("b", "2.5"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"result\":4}"));
    }

    @Test
    void testAddLargeNumbers() throws Exception {
        mockMvc.perform(get("/api/calculator/sum")
                        .param("a", "1000000000000")
                        .param("b", "2000000000000"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"result\":3000000000000}"));
    }

    @Test
    void testSubtract() throws Exception {
        mockMvc.perform(get("/api/calculator/subtract")
                        .param("a", "5")
                        .param("b", "3"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"result\":2}"));
    }

    @Test
    void testSubtractNegativeResult() throws Exception {
        mockMvc.perform(get("/api/calculator/subtract")
                        .param("a", "3")
                        .param("b", "5"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"result\":-2}"));
    }

    @Test
    void testMultiply() throws Exception {
        mockMvc.perform(get("/api/calculator/multiply")
                        .param("a", "2")
                        .param("b", "3"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"result\":6}"));
    }

    @Test
    void testMultiplyWithZero() throws Exception {
        mockMvc.perform(get("/api/calculator/multiply")
                        .param("a", "0")
                        .param("b", "100"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"result\":0}"));
    }

    @Test
    void testDivide() throws Exception {
        mockMvc.perform(get("/api/calculator/divide")
                        .param("a", "10")
                        .param("b", "2"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"result\":5}"));
    }

    @Test
    void testDivideByZero() throws Exception {
        mockMvc.perform(get("/api/calculator/divide")
                        .param("a", "10")
                        .param("b", "0"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Division by zero is not allowed"));
    }

    @Test
    void testDivideDecimalResult() throws Exception {
        mockMvc.perform(get("/api/calculator/divide")
                        .param("a", "10")
                        .param("b", "3"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"result\":3.333333333333333333333333333333333}"));
    }

}
