package com.bosch.coding.service;

import com.bosch.coding.entity.Inventory;
import com.bosch.coding.utility.EventGenerator;
import com.bosch.coding.config.RabbitMQConfig;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class EventProducerService {
    private static final Logger logger = LoggerFactory.getLogger(EventProducerService.class);

    private final RabbitTemplate rabbitTemplate;
    private final EventGenerator eventGenerator;
    private final RabbitMQConfig rabbitMQConfig;
    private final ObjectMapper objectMapper;

    @Autowired
    public EventProducerService(RabbitTemplate rabbitTemplate, EventGenerator eventGenerator,
                                RabbitMQConfig rabbitMQConfig, ObjectMapper objectMapper) {
        this.rabbitTemplate = rabbitTemplate;
        this.eventGenerator = eventGenerator;
        this.rabbitMQConfig = rabbitMQConfig;
        this.objectMapper = objectMapper;
    }

    @Scheduled(fixedRate = 100)
    public void produceEvent() {
        try {
            // Generate a random inventory event
            Inventory event = eventGenerator.generateEvent();

            // Convert the event to JSON format
            String jsonEvent = objectMapper.writeValueAsString(event);

            // Define the routing key based on the event command
            String routingKey = "inventory." + event.getCommand();

            // Send the event to the RabbitMQ exchange
            rabbitTemplate.convertAndSend(rabbitMQConfig.getExchangeName(), routingKey, jsonEvent);
            logger.info("Published: {}", jsonEvent);
        } catch (JsonProcessingException e) {
            // Log error if there’s an issue with JSON processing
            logger.error("Error processing JSON for event: {}", e.getMessage(), e);
        } catch (Exception e) {
            // Log a general error if something else goes wrong
            logger.error("Failed to produce event", e);
        }
    }
}