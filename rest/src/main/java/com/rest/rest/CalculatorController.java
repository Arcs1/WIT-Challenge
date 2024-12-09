package com.rest.rest;

import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.MDC;
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

    private final KafkaProducerService kafkaProducerService;

    @Autowired
    public CalculatorController(KafkaProducerService kafkaProducerService) {
        this.kafkaProducerService = kafkaProducerService;
    }

    @GetMapping("/sum")
    public ResponseEntity<OperationResult> sum(@RequestParam BigDecimal a, @RequestParam BigDecimal b, HttpServletResponse response) {
        String requestId = UUID.randomUUID().toString();
        MDC.put("requestId", requestId);

        try {
            String result = kafkaProducerService.sendArithmeticRequest("sum", requestId, a.toString(), b.toString());
            response.addHeader("X-Request-ID", requestId);
            return ResponseEntity.ok().body(new OperationResult(new BigDecimal(result)));
        } finally {
            MDC.clear();
        }
    }

    @GetMapping("/subtract")
    public ResponseEntity<OperationResult> subtract(@RequestParam BigDecimal a, @RequestParam BigDecimal b, HttpServletResponse response) {
        String requestId = UUID.randomUUID().toString();
        MDC.put("requestId", requestId);

        try {
            String result = kafkaProducerService.sendArithmeticRequest("subtract", requestId, a.toString(), b.toString());
            response.addHeader("X-Request-ID", requestId);
            return ResponseEntity.ok().body(new OperationResult(new BigDecimal(result)));
        } finally {
            MDC.clear();
        }
    }

    @GetMapping("/multiply")
    public ResponseEntity<OperationResult> multiply(@RequestParam BigDecimal a, @RequestParam BigDecimal b, HttpServletResponse response) {
        String requestId = UUID.randomUUID().toString();
        MDC.put("requestId", requestId);

        try {
            String result = kafkaProducerService.sendArithmeticRequest("multiply", requestId, a.toString(), b.toString());
            response.addHeader("X-Request-ID", requestId);
            return ResponseEntity.ok().body(new OperationResult(new BigDecimal(result)));
        } finally {
            MDC.clear();
        }
    }

    @GetMapping("/divide")
    public ResponseEntity<OperationResult> divide(@RequestParam BigDecimal a, @RequestParam BigDecimal b, HttpServletResponse response) {
        String requestId = UUID.randomUUID().toString();
        MDC.put("requestId", requestId);

        try {
            String result = kafkaProducerService.sendArithmeticRequest("divide", requestId, a.toString(), b.toString());
            response.addHeader("X-Request-ID", requestId);
            return ResponseEntity.ok().body(new OperationResult(new BigDecimal(result)));
        } finally {
            MDC.clear();
        }
    }

    public record OperationResult(BigDecimal result) {
    }
}
