package com.li.rabbitmq.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TTLQueueConfig {

    // 普通交换机
    private static final String EXCHANGE_X= "X";
    // 死信交换机
    private static final String DATE_LETTER_EXCHANGE_Y = "Y";
    // 普通队列A
    private static final String QUEUE_A = "QA";
    // 普通队列B
    private static final String QUEUE_B = "QB";
    // 普通队列C
    private static final String QUEUE_C = "QC";
    // 死信队列
    private static final String DATE_LETTER_QUEUE_D = "QD";

    // 声明普通交换机 X
    @Bean("xExchange")
    public DirectExchange xExchange() {
        return new DirectExchange(EXCHANGE_X);
    }

    // 声明私信交换机 Y
    @Bean("yExchange")
    public DirectExchange yExchange() {
        return new DirectExchange(DATE_LETTER_EXCHANGE_Y);
    }

    // 声明普通队列 A，TTL 为 10s
    @Bean("queueA")
    public Queue queueA() {
        return QueueBuilder.nonDurable(QUEUE_A)
                .ttl(10 * 1000)
                .deadLetterExchange(DATE_LETTER_EXCHANGE_Y)
                .deadLetterRoutingKey("YD")
                .build();
    }

    // 声明普通队列 B，TTL 为 40s
    @Bean("queueB")
    public Queue queueB() {
        return QueueBuilder.nonDurable(QUEUE_B)
                .ttl(40 * 1000)
                .deadLetterExchange(DATE_LETTER_EXCHANGE_Y)
                .deadLetterRoutingKey("YD")
                .build();
    }

    // 声明普通队列 B，不设置 TTL
    @Bean("queueC")
    public Queue queueC() {
        return QueueBuilder.nonDurable(QUEUE_C)
                .deadLetterExchange(DATE_LETTER_EXCHANGE_Y)
                .deadLetterRoutingKey("YD")
                .build();
    }

    // 声明死信队列 D
    @Bean("queueD")
    public Queue queueD() {
        return QueueBuilder.nonDurable(DATE_LETTER_QUEUE_D).build();
    }

    // 普通队列 A 绑定普通交换机 X
    @Bean("queueABindingX")
    public Binding queueABindingX() {
        return BindingBuilder.bind(queueA())
                .to(xExchange())
                .with("XA");
    }

    // 普通队列 B 绑定普通交换机 X
    @Bean("queueBBindingX")
    public Binding queueBBindingX() {
        return BindingBuilder.bind(queueB())
                .to(xExchange())
                .with("XB");
    }

    // 普通队列 C 绑定普通交换机 X
    @Bean("queueCBindingX")
    public Binding queueCBindingX() {
        return BindingBuilder.bind(queueC())
                .to(xExchange())
                .with("XC");
    }

    // 死信队列 D 绑定死信交换机 Y
    @Bean("queueDBindingY")
    public Binding queueDBindingY() {
        return BindingBuilder.bind(queueD())
                .to(yExchange())
                .with("YD");
    }
}
