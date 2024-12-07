package com.bosch.coding.config;

import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(properties = {
        "rabbitmq.exchange.name=test-exchange",
        "rabbitmq.queue.name=test-queue",
        "rabbitmq.routing.key=test-routing-key"
})
class RabbitMQConfigTest {

    @Autowired
    private RabbitMQConfig rabbitMQConfig;

    @Test
    void testQueueConfiguration() {
        Queue queue = rabbitMQConfig.queue();
        assertNotNull(queue);
        assertEquals("test-queue", queue.getName());
    }

    @Test
    void testBindingConfiguration() {
        TopicExchange exchange = rabbitMQConfig.topicExchange();
        Queue queue = rabbitMQConfig.queue();
        Binding binding = rabbitMQConfig.binding(queue, exchange);

        assertNotNull(binding);
        assertEquals("test-queue", binding.getDestination());
        assertEquals("test-routing-key", binding.getRoutingKey());
        assertEquals("test-exchange", binding.getExchange());
    }
}