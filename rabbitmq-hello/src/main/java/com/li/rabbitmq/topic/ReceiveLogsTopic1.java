package com.li.rabbitmq.topic;

import com.li.rabbitmq.util.RabbitMQUtil;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

public class ReceiveLogsTopic1 {

    private static final String EXCHANGE_NAME = "topic_logs";

    public static void main(String[] args) throws Exception {

        Channel channel = RabbitMQUtil.getChannel();

        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);
        String queue = "Q1";
        channel.queueDeclare(queue, false, false, false, null);
        channel.queueBind(queue, EXCHANGE_NAME, "*.orange.*");
        DeliverCallback deliverCallback = (consumerTag, message) -> {
            System.out.println("ReceiveLogsTopic1 接收消息：" + new String(message.getBody()));
        };
        System.out.println("ReceiveLogsTopic1 准备接收消息……");
        channel.basicConsume(queue, true, deliverCallback, consumerTag -> {});
    }
}
