package com.bosch.coding.listener;

import com.bosch.coding.entity.InventoryEvent;
import com.bosch.coding.service.InventoryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.amqp.rabbit.support.ListenerExecutionFailedException;

@Component
public class EventListener {

    private final InventoryService inventoryService;
    private final ObjectMapper objectMapper;

    public EventListener(InventoryService inventoryService, ObjectMapper objectMapper) {
        this.inventoryService = inventoryService;
        this.objectMapper = objectMapper;
    }

    @RabbitListener(queues = "#{@queue.name}")  // Listen for messages on the configured queue
    public void handleMessage(String message) throws Exception {
        try {
            // Convert the message (JSON) into an InventoryEvent object
            InventoryEvent event = objectMapper.readValue(message, InventoryEvent.class);

            // Update the inventory based on the event data
            inventoryService.updateInventory(event.getFruit(), event.getQuantity(), event.getCommand());

            // Print a message when the event is successfully processed
            System.out.println("Event processed: " + message);
        } catch (Exception ex) {
            // If something goes wrong, log the error and throw a ListenerExecutionFailedException
            throw new ListenerExecutionFailedException("Oops, something went wrong with the RabbitMQ message", ex);
        }
    }
}