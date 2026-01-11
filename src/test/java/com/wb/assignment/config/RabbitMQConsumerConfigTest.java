package com.wb.assignment.config;

import com.wb.assignment.common.Constant;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;

import static org.junit.jupiter.api.Assertions.*;

class RabbitMQConsumerConfigTest {

    @Test
    void customerExchange_shouldBeCreatedCorrectly() {
        // given
        RabbitMQConsumerConfig config = new RabbitMQConsumerConfig();

        // when
        TopicExchange exchange = config.customerExchange();

        // then
        assertNotNull(exchange);
        assertEquals(Constant.ACCOUNT_EXCHANGE, exchange.getName());
        assertEquals("topic", exchange.getType());
        assertTrue(exchange.isDurable());
    }

    @Test
    void customerCreatedQueue_shouldBeDurable() {
        // given
        RabbitMQConsumerConfig config = new RabbitMQConsumerConfig();

        // when
        Queue queue = config.customerCreatedQueue();

        // then
        assertNotNull(queue);
        assertEquals(Constant.ACCOUNT_CREATE_QUEUE, queue.getName());
        assertTrue(queue.isDurable());
    }

    @Test
    void customerCreatedBinding_shouldBindQueueToExchangeWithRoutingKey() {
        // given
        RabbitMQConsumerConfig config = new RabbitMQConsumerConfig();

        // when
        Binding binding = config.customerCreatedBinding();

        // then
        assertNotNull(binding);
        assertEquals(Constant.ACCOUNT_CREATE_QUEUE, binding.getDestination());
        assertEquals(Binding.DestinationType.QUEUE, binding.getDestinationType());
        assertEquals(Constant.ACCOUNT_EXCHANGE, binding.getExchange());
        assertEquals(Constant.ACCOUNT_CREATE_ROUTING_KEY, binding.getRoutingKey());
    }
}
