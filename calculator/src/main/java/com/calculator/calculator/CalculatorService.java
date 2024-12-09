package com.calculator.calculator;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.MathContext;

@Service
public class CalculatorService {

    public BigDecimal sum(BigDecimal a, BigDecimal b) {
        String result = a.add(b).stripTrailingZeros().toPlainString();
        return new BigDecimal(result);
    }

    public BigDecimal subtract(BigDecimal a, BigDecimal b) {
        String result = a.subtract(b).stripTrailingZeros().toPlainString();
        return new BigDecimal(result);
    }

    public BigDecimal multiply(BigDecimal a, BigDecimal b) {
        String result = a.multiply(b).stripTrailingZeros().toPlainString();
        return new BigDecimal(result);
    }

    public BigDecimal divide(BigDecimal a, BigDecimal b) {
        if (b.compareTo(BigDecimal.ZERO) == 0)
            throw new DivisionByZeroException("Division by zero is not allowed");

        String result = a.divide(b, MathContext.DECIMAL128).stripTrailingZeros().toPlainString();
        return new BigDecimal(result);
    }

}
