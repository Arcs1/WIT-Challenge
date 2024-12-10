package com.rest.rest;

import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.MDC;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.UUID;

@RestController
@RequestMapping("/api/calculator")
public class CalculatorController {

    private static final Logger logger = LoggerFactory.getLogger(CalculatorController.class);
    private final KafkaProducerService kafkaProducerService;

    @Autowired
    public CalculatorController(KafkaProducerService kafkaProducerService) {
        this.kafkaProducerService = kafkaProducerService;
    }

    @GetMapping("/sum")
    public ResponseEntity<OperationResult> sum(@RequestParam BigDecimal a, @RequestParam BigDecimal b, HttpServletResponse response) {
        String requestId = UUID.randomUUID().toString();
        MDC.put("requestId", requestId);
        response.addHeader("X-Request-ID", requestId);

        try {
            String opA = a.stripTrailingZeros().toPlainString();
            String opB = b.stripTrailingZeros().toPlainString();
            logger.info("Received sum request: a = {}, b = {}", opA, opB);
            String result = kafkaProducerService.sendArithmeticRequest("sum", requestId, opA, opB);
            logger.info("Sum result: {}", result);
            return ResponseEntity.ok().body(new OperationResult(new BigDecimal(result)));
        } catch (Exception e) {
            logger.error("Error processing sum request", e);
            return ResponseEntity.status(500).body(new OperationResult(null, "Error processing sum request: " + e.getMessage()));
        } finally {
            MDC.clear();
        }
    }

    @GetMapping("/subtract")
    public ResponseEntity<OperationResult> subtract(@RequestParam BigDecimal a, @RequestParam BigDecimal b, HttpServletResponse response) {
        String requestId = UUID.randomUUID().toString();
        MDC.put("requestId", requestId);
        response.addHeader("X-Request-ID", requestId);

        try {
            String opA = a.stripTrailingZeros().toPlainString();
            String opB = b.stripTrailingZeros().toPlainString();
            logger.info("Received subtract request: a = {}, b = {}", opA, opB);
            String result = kafkaProducerService.sendArithmeticRequest("subtract", requestId, opA, opB);
            logger.info("Subtract result: {}", result);
            return ResponseEntity.ok().body(new OperationResult(new BigDecimal(result)));
        } catch (Exception e) {
            logger.error("Error processing subtract request", e);
            return ResponseEntity.status(500).body(new OperationResult(null, "Error processing subtract request"));
        } finally {
            MDC.clear();
        }
    }

    @GetMapping("/multiply")
    public ResponseEntity<OperationResult> multiply(@RequestParam BigDecimal a, @RequestParam BigDecimal b, HttpServletResponse response) {
        String requestId = UUID.randomUUID().toString();
        MDC.put("requestId", requestId);
        response.addHeader("X-Request-ID", requestId);

        try {
            String opA = a.stripTrailingZeros().toPlainString();
            String opB = b.stripTrailingZeros().toPlainString();
            logger.info("Received multiply request: a = {}, b = {}", opA, opB);
            String result = kafkaProducerService.sendArithmeticRequest("multiply", requestId, opA, opB);
            logger.info("Multiply result: {}", result);
            return ResponseEntity.ok().body(new OperationResult(new BigDecimal(result)));
        } catch (Exception e) {
            logger.error("Error processing multiply request", e);
            return ResponseEntity.status(500).body(new OperationResult(null, "Error processing multiply request"));
        } finally {
            MDC.clear();
        }
    }

    @GetMapping("/divide")
    public ResponseEntity<OperationResult> divide(@RequestParam BigDecimal a, @RequestParam BigDecimal b, HttpServletResponse response) {
        String requestId = UUID.randomUUID().toString();
        MDC.put("requestId", requestId);
        response.addHeader("X-Request-ID", requestId);

        try {
            String opA = a.stripTrailingZeros().toPlainString();
            String opB = b.stripTrailingZeros().toPlainString();
            logger.info("Received divide request: a = {}, b = {}", opA, opB);
            if (b.equals(BigDecimal.ZERO)) {
                logger.warn("Attempted division by zero");
                return ResponseEntity.status(400).body(new OperationResult(null, "Division by zero is not allowed"));
            }
            String result = kafkaProducerService.sendArithmeticRequest("divide", requestId, opA, opB);
            logger.info("Divide result: {}", result);
            return ResponseEntity.ok().body(new OperationResult(new BigDecimal(result)));
        } catch (Exception e) {
            logger.error("Error processing divide request", e);
            return ResponseEntity.status(500).body(new OperationResult(null, "Error processing divide request"));
        } finally {
            MDC.clear();
        }
    }

    public record OperationResult(BigDecimal result, String error) {

        public OperationResult(BigDecimal result) {
            this(result, "None");
        }

        public OperationResult(BigDecimal result, String error) {
            this.result = result;
            this.error = error;
        }
    }
}
