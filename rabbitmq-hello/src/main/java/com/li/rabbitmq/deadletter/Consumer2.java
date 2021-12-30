package com.li.rabbitmq.deadletter;

import com.li.rabbitmq.util.RabbitMQUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

public class Consumer2 {

    // 死信队列
    private static final String DEAD_QUEUE_NAME = "dead_queue";

    public static void main(String[] args) throws Exception {

        Channel channel = RabbitMQUtil.getChannel();

        System.out.println("Consumer2 等待接收消息……");
        DeliverCallback deliverCallback = (consumerTag, message) -> {
            System.out.println("Consumer2 接收的消息：" + new String(message.getBody()));
        };
        channel.basicConsume(DEAD_QUEUE_NAME, true, deliverCallback, consumerTag -> {});
    }
}
