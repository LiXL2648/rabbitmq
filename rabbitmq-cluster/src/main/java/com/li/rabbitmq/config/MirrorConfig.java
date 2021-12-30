package com.li.rabbitmq.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MirrorConfig {

    // 镜像交换机
    public static final String MIRROR_EXCHANGE_NAME = "mirror_exchange";

    // 镜像队列
    public static final String MIRROR_QUEUE_NAME = "mirror_queue";

    // 路由
    public static final String MIRROR_ROUTING_KEY = "mirror";

    @Bean("mirrorExchange")
    public DirectExchange mirrorExchange() {
        return ExchangeBuilder.directExchange(MIRROR_EXCHANGE_NAME).durable(true).build();
    }

    @Bean("mirrorQueue")
    public Queue mirrorQueue() {
        return QueueBuilder.durable(MIRROR_QUEUE_NAME).build();
    }

    @Bean("bindingMirrorExchange")
    public Binding bindingMirrorExchange() {
        return BindingBuilder.bind(mirrorQueue()).to(mirrorExchange()).with(MIRROR_ROUTING_KEY);
    }
}
