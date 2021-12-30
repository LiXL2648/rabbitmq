package com.li.rabbitmq.direct;

import com.li.rabbitmq.util.RabbitMQUtil;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

public class ReceiveLogsDirect1 {

    private static final String EXCHANGE_NAME = "direct_logs";

    public static void main(String[] args) throws Exception {

        Channel channel = RabbitMQUtil.getChannel();

        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
        String queue = "console";
        channel.queueDeclare(queue, false, false, false, null);
        channel.queueBind(queue, EXCHANGE_NAME, "info");
        channel.queueBind(queue, EXCHANGE_NAME, "warning");
        DeliverCallback deliverCallback = (consumerTag, message) -> {
            System.out.println("ReceiveLogsDirect1 接收到的消息："+ new String(message.getBody()));
        };
        channel.basicConsume(queue, deliverCallback, consumerTag -> {});
    }
}
