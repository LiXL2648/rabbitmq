package com.li.rabbitmq.autoack;

import com.li.rabbitmq.util.RabbitMQUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;

import java.util.Scanner;

public class Producer {

    private static final String QUEUE_NAME = "hello";

    public static void main(String[] args) throws Exception {

        Channel channel = RabbitMQUtil.getChannel();
        // 开启发布确认
        channel.confirmSelect();
        // 声明队列持久化
        channel.queueDeclare(QUEUE_NAME, true, false, false, null);
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String message = scanner.next();
            // 设置消息持久化
            channel.basicPublish("", QUEUE_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes());
            System.out.println("生产者发送消息：" + message);
        }
    }
}
