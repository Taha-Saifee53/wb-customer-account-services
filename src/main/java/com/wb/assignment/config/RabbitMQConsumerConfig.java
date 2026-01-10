package com.wb.assignment.config;

import com.wb.assignment.common.Constant;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableRabbit
public class RabbitMQConsumerConfig {

    @Bean
    public TopicExchange customerExchange() {
        return new TopicExchange(Constant.ACCOUNT_EXCHANGE);
    }

    @Bean
    public Queue customerCreatedQueue() {
        return QueueBuilder.durable(Constant.ACCOUNT_CREATE_QUEUE).build();
    }

    @Bean
    public Binding customerCreatedBinding() {
        return BindingBuilder
                .bind(customerCreatedQueue())
                .to(customerExchange())
                .with(Constant.ACCOUNT_CREATE_ROUTING_KEY);
    }
}

