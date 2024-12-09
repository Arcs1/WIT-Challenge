package com.rest.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.math.MathContext;

@RestController
@RequestMapping("/api/calculator")
public class CalculatorController {

    @GetMapping("/sum")
    public ResponseEntity<?> sum(@RequestParam BigDecimal a, @RequestParam BigDecimal b) {
        BigDecimal result = a.add(b);
        return ResponseEntity.ok().body(new OperationResult(result));
    }

    @GetMapping("/subtract")
    public ResponseEntity<?> subtract(@RequestParam BigDecimal a, @RequestParam BigDecimal b) {
        BigDecimal result = a.subtract(b);
        return ResponseEntity.ok().body(new OperationResult(result));
    }

    @GetMapping("/multiply")
    public ResponseEntity<?> multiply(@RequestParam BigDecimal a, @RequestParam BigDecimal b) {
        BigDecimal result = a.multiply(b);
        return ResponseEntity.ok().body(new OperationResult(result));
    }

    @GetMapping("/divide")
    public ResponseEntity<?> divide(@RequestParam BigDecimal a, @RequestParam BigDecimal b) {
        if (b.compareTo(BigDecimal.ZERO) == 0) {
            return ResponseEntity.badRequest().body("Division by zero is not allowed");
        }
        BigDecimal result = a.divide(b, MathContext.DECIMAL128);
        return ResponseEntity.ok().body(new OperationResult(result));
    }

    private record OperationResult(BigDecimal result) {
    }
}
