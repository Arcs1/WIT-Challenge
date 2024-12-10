package com.rest.rest;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Service
public class KafkaProducerService {

    private static final Logger logger = LoggerFactory.getLogger(KafkaProducerService.class);

    private final KafkaTemplate<String, String> kafkaTemplate;
    private static final String REQUEST_TOPIC = "calculator-operations";
    private static final String RESULT_TOPIC = "calculator-results";

    // To correlate request IDs with CompletableFuture results
    private final ConcurrentHashMap<String, CompletableFuture<String>> responseMap = new ConcurrentHashMap<>();

    public KafkaProducerService(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public String sendArithmeticRequest(String operation, String requestId, String a, String b) {
        MDC.put("requestId", requestId);
        String message = String.format("%s:%s:%s:%s", requestId, operation, a, b);
        logger.info("Sending arithmetic request: operation = {}, a = {}, b = {}", operation, a, b);

        // Create a CompletableFuture and store it with the requestId
        CompletableFuture<String> future = new CompletableFuture<>();
        responseMap.put(requestId, future);

        try {
            kafkaTemplate.send(REQUEST_TOPIC, message);
            logger.debug("Message sent to Kafka topic '{}': {}", REQUEST_TOPIC, message);

            // Wait for the result (timeout after 120 seconds)
            String result = future.get(120, TimeUnit.SECONDS);

            if (result.startsWith("Error")) {
                result = result.substring(6);
                throw new RuntimeException(result);
            }

            logger.info("Received result: {}", result);
            return result;
        } catch (Exception e) {
            logger.error("Failed to get response", e);
            throw new RuntimeException(e.getMessage());
        } finally {
            responseMap.remove(requestId);
        }
    }

    @KafkaListener(topics = RESULT_TOPIC, groupId = "rest-consumer-group")
    public void listenForResult(ConsumerRecord<String, String> record) {
        logger.debug("Received message from Kafka topic '{}': {}", RESULT_TOPIC, record.value());

        try {
            // Parse the response message (assumes format: requestId:result)
            String[] parts = record.value().split(":");

            String requestId = parts[0];
            String result = parts[1];

            if (requestId.startsWith("Error")) {
                // Error:requestId:ErrorMessage
                requestId = parts[1];

                MDC.put("requestId", requestId);
                logger.warn("Failed to process Kafka message: {}", record.value());

                // Complete the corresponding CompletableFuture
                CompletableFuture<String> future = responseMap.remove(requestId);

                StringBuilder errorMsg = new StringBuilder();
                errorMsg.append("Error:");
                for (int i = 2; i < parts.length; i++) {
                    errorMsg.append(parts[i]);
                    if (i < parts.length - 1)
                        errorMsg.append(":");
                }

                future.complete(errorMsg.toString());
                return;
            }

            MDC.put("requestId", requestId);
            // Complete the corresponding CompletableFuture
            CompletableFuture<String> future = responseMap.remove(requestId);

            if (future != null) {
                future.complete(result);
                logger.info("Completed future: result={}", result);
            } else {
                logger.warn("No matching future found in responseMap");
            }
        } catch (Exception e) {
            logger.error("Error processing message from Kafka topic '{}'", RESULT_TOPIC, e);
        }
    }
}
