package com.bosch.coding.listener;

import com.bosch.coding.service.InventoryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.support.ListenerExecutionFailedException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class EventListenerTest {

    private final InventoryService inventoryService = mock(InventoryService.class);
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final EventListener eventListener = new EventListener(inventoryService, objectMapper);

    @Test
    void testHandleMessageValidEvent() throws Exception {
        String message = "{\"fruit\":\"apple\",\"quantity\":10,\"command\":\"add\"}";

        eventListener.handleMessage(message);

        verify(inventoryService, times(1))
                .updateInventory("apple", 10, "add");
    }

    @Test
    void testHandleMessageInvalidJson() {
        String message = "{fruit:apple}";

        assertThrows(ListenerExecutionFailedException.class, () -> eventListener.handleMessage(message));
    }

    @Test
    void testHandleMessageEmptyMessage() {
        assertThrows(ListenerExecutionFailedException.class, () -> eventListener.handleMessage(""));
    }

    @Test
    void testHandleMessageUnhandledException() throws Exception {
        String message = "{\"fruit\":\"apple\",\"quantity\":10,\"command\":\"add\"}";
        doThrow(new RuntimeException("Unexpected error"))
                .when(inventoryService).updateInventory(anyString(), anyInt(), anyString());

        assertThrows(ListenerExecutionFailedException.class, () -> eventListener.handleMessage(message));
    }
}
