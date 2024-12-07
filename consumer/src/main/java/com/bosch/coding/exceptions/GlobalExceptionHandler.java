package com.bosch.coding.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.support.ListenerExecutionFailedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // Handle custom InventoryException
    @ExceptionHandler(InventoryException.class)
    public ResponseEntity<String> handleInventoryException(InventoryException ex) {
        logger.error("Inventory error: {}", ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Inventory error: " + ex.getMessage());
    }

    // Handle ListenerExecutionFailedException (RabbitMQ listener issues)
    @ExceptionHandler(ListenerExecutionFailedException.class)
    public ResponseEntity<String> handleListenerException(ListenerExecutionFailedException ex) {
        logger.error("RabbitMQ listener error: {}", ex.getMessage(), ex);
        // Modify the message to avoid duplication
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing RabbitMQ message");
    }

    // Handle all other general exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGenericException(Exception ex) {
        logger.error("Unexpected error occurred: {}", ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred: " + ex.getMessage());
    }
}