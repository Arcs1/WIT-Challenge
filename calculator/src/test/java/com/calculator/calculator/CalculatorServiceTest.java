package com.calculator.calculator;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class CalculatorServiceTest {

    private final CalculatorService calculatorService = new CalculatorService();

    @Test
    void testSum() {
        BigDecimal result = calculatorService.sum(new BigDecimal("1.5"), new BigDecimal("2.5"));
        assertEquals(new BigDecimal("4"), result);
    }

    @Test
    void testSumLargeNumbers() {
        BigDecimal result = calculatorService.sum(new BigDecimal("1000000000000"), new BigDecimal("2000000000000"));
        assertEquals(new BigDecimal("3000000000000"), result);
    }

    @Test
    void testSubtract() {
        BigDecimal result = calculatorService.subtract(new BigDecimal("5"), new BigDecimal("3.5"));
        assertEquals(new BigDecimal("1.5"), result);
    }

    @Test
    void testSubtractNegativeResult() {
        BigDecimal result = calculatorService.subtract(new BigDecimal("3"), new BigDecimal("5"));
        assertEquals(new BigDecimal("-2"), result);
    }

    @Test
    void testMultiply() {
        BigDecimal result = calculatorService.multiply(new BigDecimal("2"), new BigDecimal("3"));
        assertEquals(new BigDecimal("6"), result);
    }

    @Test
    void testMultiplyWithZero() {
        BigDecimal result = calculatorService.multiply(new BigDecimal("0"), new BigDecimal("3"));
        assertEquals(new BigDecimal("0"), result);
    }

    @Test
    void testMultiplyWithLargeNumbers() {
        BigDecimal result = calculatorService.multiply(new BigDecimal("1234567890123456789"), new BigDecimal("0.0000000000000000000987654321"));
        assertEquals(new BigDecimal("0.1219326311248285321112635269"), result);
    }

    @Test
    void testDivide() throws DivisionByZeroException {
        BigDecimal result = calculatorService.divide(new BigDecimal("10"), new BigDecimal("4"));
        assertEquals(new BigDecimal("2.5"), result);
    }

    @Test
    void testDivideDecimalResult() {
        BigDecimal result = calculatorService.divide(new BigDecimal("10"), new BigDecimal("3"));
        assertEquals(new BigDecimal("3.333333333333333333333333333333333"), result);
    }

    @Test
    void testDivideByZero() {
        DivisionByZeroException exception = assertThrows(DivisionByZeroException.class, () ->
                calculatorService.divide(new BigDecimal("10"), BigDecimal.ZERO)
        );
        assertEquals("Division by zero is not allowed", exception.getMessage());
    }

}
