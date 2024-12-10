package com.calculator.calculator;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class KafkaConsumerService {

    private static final Logger logger = LoggerFactory.getLogger(KafkaConsumerService.class);

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final CalculatorService calculatorService;

    private static final String RESULT_TOPIC = "calculator-results";

    public KafkaConsumerService(KafkaTemplate<String, String> kafkaTemplate, CalculatorService calculatorService) {
        this.kafkaTemplate = kafkaTemplate;
        this.calculatorService = calculatorService;
    }

    @KafkaListener(topics = "calculator-operations", groupId = "calculator-group")
    public void listenForOperations(ConsumerRecord<String, String> record) {
        String message = record.value();

        // Parse the operation from the Kafka message (assumes format: requestId:operation:a:b)
        String[] parts = message.split(":");
        String requestId = parts[0];
        String operation = parts[1];
        BigDecimal a = new BigDecimal(parts[2]);
        BigDecimal b = new BigDecimal(parts[3]);

        MDC.put("requestId", requestId);

        logger.info("Received Kafka message: {}", message);

        logger.info("Processing operation '{}' with operands a = {}, b = {}", operation, a, b);
        try {
            BigDecimal result = processOperation(operation, a, b);
            logger.info("Operation '{}' completed successfully. Result: {}", operation, result);

            String resultMessage = String.format("%s:%s", requestId, result.toString());
            kafkaTemplate.send(RESULT_TOPIC, resultMessage);
            logger.info("Result sent to Kafka topic '{}': {}", RESULT_TOPIC, resultMessage);

        } catch (Exception e) {
            logger.error("Failed to process Kafka message: {}", message, e);
            kafkaTemplate.send(RESULT_TOPIC, "Error processing Kafka message for requestID:" + requestId + ":" + e.getMessage());
        } finally {
            MDC.clear();
        }
    }

    private BigDecimal processOperation(String operation, BigDecimal a, BigDecimal b) {
        try {
            return switch (operation) {
                case "sum" -> calculatorService.sum(a, b);
                case "subtract" -> calculatorService.subtract(a, b);
                case "multiply" -> calculatorService.multiply(a, b);
                case "divide" -> calculatorService.divide(a, b);
                default -> throw new UnsupportedOperationException("Operation not supported: " + operation);
            };
        } catch (Exception e) {
            logger.error("Error while performing operation '{}' with operands a = {}, b = {}", operation, a, b, e);
            throw e;
        }
    }
}
