package com.calculator.calculator;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class KafkaConsumerService {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final CalculatorService calculatorService;

    private static final String RESULT_TOPIC = "calculator-results";

    public KafkaConsumerService(KafkaTemplate<String, String> kafkaTemplate, CalculatorService calculatorService) {
        this.kafkaTemplate = kafkaTemplate;
        this.calculatorService = calculatorService;
    }

    @KafkaListener(topics = "calculator-operations", groupId = "calculator-group")
    public void listenForOperations(ConsumerRecord<String, String> record) {
        // Parse the operation from the Kafka message (assumes format: requestId:operation:a:b)
        String message = record.value();
        String[] parts = message.split(":");
        String requestId = parts[0];
        String operation = parts[1];
        BigDecimal a = new BigDecimal(parts[2]);
        BigDecimal b = new BigDecimal(parts[3]);

        BigDecimal result = processOperation(operation, a, b);

        // Send the result back to Kafka with the requestId
        String resultMessage = String.format("%s:%s", requestId, result.toString());
        kafkaTemplate.send(RESULT_TOPIC, resultMessage);
    }

    private BigDecimal processOperation(String operation, BigDecimal a, BigDecimal b) {
        return switch (operation) {
            case "sum" -> calculatorService.sum(a, b);
            case "subtract" -> calculatorService.subtract(a, b);
            case "multiply" -> calculatorService.multiply(a, b);
            case "divide" -> calculatorService.divide(a, b);
            default -> throw new UnsupportedOperationException("Operation not supported");
        };
    }
}
