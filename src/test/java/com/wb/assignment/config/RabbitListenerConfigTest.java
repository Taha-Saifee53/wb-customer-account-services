package com.wb.assignment.config;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;

import static org.junit.jupiter.api.Assertions.*;

class RabbitListenerConfigTest {

    @Test
    void rabbitListenerContainerFactory_shouldBeConfiguredCorrectly() {
        // given
        RabbitListenerConfig config = new RabbitListenerConfig();

        ConnectionFactory connectionFactory = Mockito.mock(ConnectionFactory.class);
        JacksonJsonMessageConverter messageConverter =
                new JacksonJsonMessageConverter();

        // when
        SimpleRabbitListenerContainerFactory factory =
                config.rabbitListenerContainerFactory(connectionFactory, messageConverter);

        // then
        assertNotNull(factory);
    }
}
