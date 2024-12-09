package com.rest.rest;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Service
public class KafkaProducerService {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private static final String REQUEST_TOPIC = "calculator-operations";
    private static final String RESULT_TOPIC = "calculator-results";

    // To correlate request IDs with CompletableFuture results
    private final ConcurrentHashMap<String, CompletableFuture<String>> responseMap = new ConcurrentHashMap<>();

    public KafkaProducerService(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public String sendArithmeticRequest(String operation, String requestId, String a, String b) {
        String message = String.format("%s:%s:%s:%s", requestId, operation, a, b);

        // Create a CompletableFuture and store it with the requestId
        CompletableFuture<String> future = new CompletableFuture<>();
        responseMap.put(requestId, future);

        // Send the request message to Kafka
        kafkaTemplate.send(REQUEST_TOPIC, message);

        try {
            // Wait for the result (timeout after 120 seconds)
            return future.get(120, TimeUnit.SECONDS);
        } catch (Exception e) {
            throw new RuntimeException("Failed to get response from Kafka", e);
        } finally {
            responseMap.remove(requestId); // Clean up after completing
        }
    }

    @KafkaListener(topics = RESULT_TOPIC, groupId = "rest-consumer-group")
    public void listenForResult(ConsumerRecord<String, String> record) {
        // Parse the response message (assumes format: requestId:result)
        String[] parts = record.value().split(":");
        String requestId = parts[0];
        String result = parts[1];

        // Complete the corresponding CompletableFuture
        CompletableFuture<String> future = responseMap.remove(requestId);
        if (future != null) {
            future.complete(result);
        }
    }
}
