package com.bosch.coding.exceptions;

import com.bosch.coding.listener.EventListener;
import com.bosch.coding.service.InventoryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.support.ListenerExecutionFailedException;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler exceptionHandler = new GlobalExceptionHandler();

    @Test
    void testHandleInventoryException() {
        InventoryException ex = new InventoryException("Invalid command");
        ResponseEntity<String> response = exceptionHandler.handleInventoryException(ex);

        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Inventory error: Invalid command", response.getBody());
    }

    @Test
    void testHandleListenerExecutionFailedException() {
        // Simulate an error cause for the listener
        Exception cause = new Exception("Invalid JSON format");
        ListenerExecutionFailedException ex = new ListenerExecutionFailedException("Error processing RabbitMQ message", cause);

        ResponseEntity<String> response = exceptionHandler.handleListenerException(ex);

        // Update expected message to match the new exception handler logic
        assertEquals(500, response.getStatusCodeValue());
        assertEquals("Error processing RabbitMQ message", response.getBody());
    }

    @Test
    void testHandleGenericException() {
        Exception ex = new Exception("Unexpected error");
        ResponseEntity<String> response = exceptionHandler.handleGenericException(ex);

        assertEquals(500, response.getStatusCodeValue());
        assertEquals("An unexpected error occurred: Unexpected error", response.getBody());
    }

    @Test
    void testHandleMessageInvalidJson() throws Exception {
        // Mock dependencies
        InventoryService inventoryService = mock(InventoryService.class);
        ObjectMapper objectMapper = new ObjectMapper();
        EventListener eventListener = new EventListener(inventoryService, objectMapper);

        String invalidMessage = "{fruit:apple}"; // Invalid JSON format

        // Assert the exception is thrown when invalid message is handled
        Exception exception = assertThrows(ListenerExecutionFailedException.class, () -> eventListener.handleMessage(invalidMessage));

        assertEquals("Oops, something went wrong with the RabbitMQ message", exception.getMessage());
        assertNotNull(exception.getCause());
    }
}