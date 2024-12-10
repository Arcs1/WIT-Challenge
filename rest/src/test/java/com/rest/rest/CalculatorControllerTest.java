package com.rest.rest;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class CalculatorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private KafkaProducerService kafkaProducerService;

    @Test
    void testAdd() throws Exception {
        Mockito.when(kafkaProducerService.sendArithmeticRequest(
                        Mockito.eq("sum"),
                        Mockito.anyString(),
                        Mockito.eq("1.5"),
                        Mockito.eq("2.5")))
                .thenReturn("4");

        mockMvc.perform(get("/api/calculator/sum")
                        .param("a", "1.5")
                        .param("b", "2.5"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"result\":4,\"error\":\"None\"}"));
    }

    @Test
    void testAddLargeNumbers() throws Exception {
        Mockito.when(kafkaProducerService.sendArithmeticRequest(
                        Mockito.eq("sum"),
                        Mockito.anyString(),
                        Mockito.eq("1000000000000"),
                        Mockito.eq("2000000000000")))
                .thenReturn("3000000000000");

        mockMvc.perform(get("/api/calculator/sum")
                        .param("a", "1000000000000")
                        .param("b", "2000000000000"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"result\":3000000000000,\"error\":\"None\"}"));
    }

    @Test
    void testSubtract() throws Exception {
        Mockito.when(kafkaProducerService.sendArithmeticRequest(
                        Mockito.eq("subtract"),
                        Mockito.anyString(),
                        Mockito.eq("5"),
                        Mockito.eq("3")))
                .thenReturn("2");

        mockMvc.perform(get("/api/calculator/subtract")
                        .param("a", "5")
                        .param("b", "3"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"result\":2,\"error\":\"None\"}"));
    }

    @Test
    void testSubtractNegativeResult() throws Exception {
        Mockito.when(kafkaProducerService.sendArithmeticRequest(
                        Mockito.eq("subtract"),
                        Mockito.anyString(),
                        Mockito.eq("3"),
                        Mockito.eq("5")))
                .thenReturn("-2");

        mockMvc.perform(get("/api/calculator/subtract")
                        .param("a", "3")
                        .param("b", "5"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"result\":-2,\"error\":\"None\"}"));
    }

    @Test
    void testMultiply() throws Exception {
        Mockito.when(kafkaProducerService.sendArithmeticRequest(
                        Mockito.eq("multiply"),
                        Mockito.anyString(),
                        Mockito.eq("2"),
                        Mockito.eq("3")))
                .thenReturn("6");

        mockMvc.perform(get("/api/calculator/multiply")
                        .param("a", "2")
                        .param("b", "3"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"result\":6,\"error\":\"None\"}"));
    }

    @Test
    void testMultiplyWithZero() throws Exception {
        Mockito.when(kafkaProducerService.sendArithmeticRequest(
                        Mockito.eq("multiply"),
                        Mockito.anyString(),
                        Mockito.eq("0"),
                        Mockito.eq("100")))
                .thenReturn("0");

        mockMvc.perform(get("/api/calculator/multiply")
                        .param("a", "0")
                        .param("b", "100"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"result\":0,\"error\":\"None\"}"));
    }

    @Test
    void testMultiplyWithLargeNumbers() throws Exception {
        Mockito.when(kafkaProducerService.sendArithmeticRequest(
                        Mockito.eq("multiply"),
                        Mockito.anyString(),
                        Mockito.eq("1234567890123456789"),
                        Mockito.eq("0.0000000000000000000987654321")))
                .thenReturn("0.1219326311248285321112635269");

        mockMvc.perform(get("/api/calculator/multiply")
                        .param("a", "1234567890123456789")
                        .param("b", "0.0000000000000000000987654321"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"result\":0.1219326311248285321112635269,\"error\":\"None\"}"));
    }

    @Test
    void testDivide() throws Exception {
        Mockito.when(kafkaProducerService.sendArithmeticRequest(
                        Mockito.eq("divide"),
                        Mockito.anyString(),
                        Mockito.eq("10"),
                        Mockito.eq("2")))
                .thenReturn("5");

        mockMvc.perform(get("/api/calculator/divide")
                        .param("a", "10")
                        .param("b", "2"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"result\":5,\"error\":\"None\"}"));
    }

    @Test
    void testDivideByZero() throws Exception {
        mockMvc.perform(get("/api/calculator/divide")
                        .param("a", "10")
                        .param("b", "0"))
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"result\":null,\"error\":\"Division by zero is not allowed\"}"));
    }

    @Test
    void testDivideDecimalResult() throws Exception {
        Mockito.when(kafkaProducerService.sendArithmeticRequest(
                        Mockito.eq("divide"),
                        Mockito.anyString(),
                        Mockito.eq("10"),
                        Mockito.eq("3")))
                .thenReturn("3.333333333333333333333333333333333");

        mockMvc.perform(get("/api/calculator/divide")
                        .param("a", "10")
                        .param("b", "3"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"result\":3.333333333333333333333333333333333,\"error\":\"None\"}"));
    }
}
