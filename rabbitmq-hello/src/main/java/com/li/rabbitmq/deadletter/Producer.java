package com.li.rabbitmq.deadletter;

import com.li.rabbitmq.util.RabbitMQUtil;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;

import java.util.Scanner;

public class Producer {

    // 普通交换机
    private static final String NORMAL_EXCHANGE_NAME = "normal_exchange";

    public static void main(String[] args) throws Exception {

        Channel channel = RabbitMQUtil.getChannel();
        channel.exchangeDeclare(NORMAL_EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
        System.out.println("生产者准备发消息……");

        // 死信消息，设置TTL时间
        AMQP.BasicProperties properties = null;
                // new AMQP.BasicProperties().builder().expiration("10000").build();

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String message = scanner.next();
            channel.basicPublish(NORMAL_EXCHANGE_NAME, "normal", properties, message.getBytes());
        }
    }
}
