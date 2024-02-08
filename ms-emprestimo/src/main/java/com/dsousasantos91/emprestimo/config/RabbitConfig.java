package com.dsousasantos91.emprestimo.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@EnableRabbit
@Configuration
public class RabbitConfig {

    private static final String EMPRESTIMO_QUEUE = "emprestimo.queue";
    private static final String EMPRESTIMO_QUEUE_DLQ = "emprestimo.queue-dlq";
    private static final String EMPRESTIMO_EXCHANGE = "emprestimo.exchange";
    private static final String EMPRESTIMO_EXCHANGE_DLX = "emprestimo.exchange-dlx";

    @Bean
    public Queue emprestimoQueue() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-dead-letter-exchante", EMPRESTIMO_EXCHANGE_DLX);
        return new Queue(EMPRESTIMO_QUEUE, true, false, false, args);
    }

    @Bean
    public Queue emprestimoQueueDLQ() {
        return new Queue(EMPRESTIMO_QUEUE_DLQ);
    }

    @Bean
    public FanoutExchange exchange() {
        return ExchangeBuilder.fanoutExchange(EMPRESTIMO_EXCHANGE).durable(true).build();
    }

    @Bean
    public FanoutExchange exchangeDLQ() {
        return ExchangeBuilder.fanoutExchange(EMPRESTIMO_EXCHANGE_DLX).durable(true).build();
    }

    @Bean
    public Binding binding(@Qualifier("emprestimoQueue") Queue notificationsQueue, FanoutExchange exchange) {
        return BindingBuilder.bind(notificationsQueue).to(exchange);
    }

    @Bean
    public Binding DLQbinding(@Qualifier("emprestimoQueueDLQ") Queue notificationsQueue, FanoutExchange exchange) {
        return BindingBuilder.bind(notificationsQueue).to(exchange);
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jackson2JsonMessageConverter());
        return rabbitTemplate;
    }

    @Bean
    public MessageConverter jackson2JsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
