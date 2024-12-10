package com.calculator.calculator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.MathContext;

@Service
public class CalculatorService {

    private static final Logger logger = LoggerFactory.getLogger(CalculatorService.class);

    public BigDecimal sum(BigDecimal a, BigDecimal b) {
        logger.info("Performing addition: a = {}, b = {}", a, b);
        try {
            String result = a.add(b).stripTrailingZeros().toPlainString();
            logger.info("Addition result: {}", result);
            return new BigDecimal(result);
        } catch (Exception e) {
            logger.error("Error during addition: a = {}, b = {}", a, b, e);
            throw e;
        }
    }

    public BigDecimal subtract(BigDecimal a, BigDecimal b) {
        logger.info("Performing subtraction: a = {}, b = {}", a, b);
        try {
            String result = a.subtract(b).stripTrailingZeros().toPlainString();
            logger.info("Subtraction result: {}", result);
            return new BigDecimal(result);
        } catch (Exception e) {
            logger.error("Error during subtraction: a = {}, b = {}", a, b, e);
            throw e;
        }
    }

    public BigDecimal multiply(BigDecimal a, BigDecimal b) {
        logger.info("Performing multiplication: a = {}, b = {}", a, b);
        try {
            String result = a.multiply(b).stripTrailingZeros().toPlainString();
            logger.info("Multiplication result: {}", result);
            return new BigDecimal(result);
        } catch (Exception e) {
            logger.error("Error during multiplication: a = {}, b = {}", a, b, e);
            throw e;
        }
    }

    public BigDecimal divide(BigDecimal a, BigDecimal b) {
        logger.info("Performing division: a = {}, b = {}", a, b);
        if (b.compareTo(BigDecimal.ZERO) == 0) {
            logger.error("Division by zero error: a = {}, b = {}", a, b);
            throw new DivisionByZeroException("Division by zero is not allowed");
        }

        try {
            String result = a.divide(b, MathContext.DECIMAL128).stripTrailingZeros().toPlainString();
            logger.info("Division result: {}", result);
            return new BigDecimal(result);
        } catch (Exception e) {
            logger.error("Error during division: a = {}, b = {}", a, b, e);
            throw e;
        }
    }

}
