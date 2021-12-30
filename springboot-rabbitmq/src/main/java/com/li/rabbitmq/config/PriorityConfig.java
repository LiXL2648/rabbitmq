package com.li.rabbitmq.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class PriorityConfig {

    // 优先级队列
    private static final String PRIORITY_QUEUE_NAME = "priority_queue";
    // 惰性队列
    private static final String LAZY_QUEUE_NAME = "lazy_queue";

    @Bean("priorityQueue")
    public Queue priorityQueue() {
        return QueueBuilder.nonDurable(PRIORITY_QUEUE_NAME).maxPriority(10).build();
    }

    @Bean("lazyQueue")
    public Queue lazyQueue() {
        return QueueBuilder.nonDurable(LAZY_QUEUE_NAME).lazy().build();
    }
}
