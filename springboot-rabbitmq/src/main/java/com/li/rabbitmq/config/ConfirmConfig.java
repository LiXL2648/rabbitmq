package com.li.rabbitmq.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConfirmConfig {

    // 交换机
    private static final String CONFIRM_EXCHANGE_NAME = "confirm_exchange";

    // 队列
    private static final String CONFIRM_QUEUE_NAME = "confirm_queue";

    // 路由
    private static final String CONFIRM_ROUTING_KEY = "confirm";

    // 备份交换机
    private static final String BACKUP_EXCHANGE_NAME = "backup_exchange";

    // 备份队列
    private static final String BACKUP_QUEUE_NAME = "backup_queue";

    // 告警队列
    private static final String WARNING_QUEUE_NAME = "warning_queue";

    // 声明交换机
    @Bean("confirmExchange")
    public DirectExchange confirmExchange() {
        return ExchangeBuilder.directExchange(CONFIRM_EXCHANGE_NAME).alternate(BACKUP_EXCHANGE_NAME).build();
    }

    // 声明队列
    @Bean("confirmQueue")
    public Queue confirmQueue() {
        return QueueBuilder.nonDurable(CONFIRM_QUEUE_NAME).build();
    }

    // 绑定队列和交换机
    @Bean("bindingConfirmExchange")
    public Binding bindingConfirmExchange() {
        return BindingBuilder.bind(confirmQueue()).to(confirmExchange()).with(CONFIRM_ROUTING_KEY);
    }

    // 声明备份队列
    @Bean("backupExchange")
    public FanoutExchange backupExchange() {
        return new FanoutExchange(BACKUP_EXCHANGE_NAME);
    }

    // 声明备份队列
    @Bean("backupQueue")
    public Queue backupQueue() {
        return QueueBuilder.nonDurable(BACKUP_QUEUE_NAME).build();
    }

    // 声明告警队列
    @Bean("warningQueue")
    public Queue warningQueue() {
        return QueueBuilder.nonDurable(WARNING_QUEUE_NAME).build();
    }

    // 备份队列绑定到备份交换机
    @Bean("backupQueueBinding")
    public Binding backupQueueBinding() {
        return BindingBuilder.bind(backupQueue()).to(backupExchange());
    }

    // 告警队列绑定到备份交换机
    @Bean("warningQueueBinding")
    public Binding warningQueueBinding() {
        return BindingBuilder.bind(warningQueue()).to(backupExchange());
    }
}
