package com.bosch.coding.service;

import com.bosch.coding.config.RabbitMQConfig;
import com.bosch.coding.entity.Inventory;
import com.bosch.coding.utility.EventGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import static org.mockito.Mockito.*;

class EventProducerServiceTest {

    @Mock
    private RabbitTemplate rabbitTemplate;

    @Mock
    private EventGenerator eventGenerator;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private EventProducerService eventProducerService;

    EventProducerServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testProduceEventJsonProcessingException() throws JsonProcessingException {
        Inventory event = new Inventory("apples", 5, "add");
        when(eventGenerator.generateEvent()).thenReturn(event);
        when(objectMapper.writeValueAsString(event)).thenThrow(new JsonProcessingException("Error") {});

        eventProducerService.produceEvent();

        verify(rabbitTemplate, never()).convertAndSend(anyString(), anyString(), anyString());
    }
}