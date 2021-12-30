package com.li.rabbitmq.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class DelayedConfig {

    // 延迟交换机
    private static final String DELAYED_EXCHANGE_NAME = "delayed_exchange";
    // 延迟队列
    private static final String DELAYED_QUEUE_NAME = "delayed_queue";
    // 延迟路由
    private static final String DELAYED_ROUTING_KEY = "delayed";

    // 声明延迟交换机，基于延迟插件
    @Bean("delayedExchange")
    public CustomExchange delayedExchange() {
        Map<String, Object> arguments = new HashMap<>();
        // 定义延迟类型
        arguments.put("x-delayed-type", "direct");
        /*
         * name：交换机类型
         * type：交换机类型
         * durable：是否需要持久化
         * autoDelete：是否自动删除交换机
         * arguments：其他参数
         */
        return new CustomExchange(DELAYED_EXCHANGE_NAME, "x-delayed-message", false, false, arguments);
    }

    // 声明延迟队列，基于延迟插件
    @Bean("delayedQueue")
    public Queue delayedQueue() {
        return QueueBuilder.nonDurable(DELAYED_QUEUE_NAME).build();
    }

    // 延迟队列绑定到延迟交换机
    @Bean("bindingDelayedExchange")
    public Binding bindingDelayedExchange() {
        return BindingBuilder.bind(delayedQueue()).to(delayedExchange()).with(DELAYED_ROUTING_KEY).noargs();
    }
}
