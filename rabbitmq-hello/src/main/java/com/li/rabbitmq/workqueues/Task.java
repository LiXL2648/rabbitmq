package com.li.rabbitmq.workqueues;

import com.li.rabbitmq.util.RabbitMQUtil;
import com.rabbitmq.client.Channel;

import java.util.Scanner;

public class Task {

    private static final String QUEUE_NAME = "hello";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMQUtil.getChannel();

        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String message = scanner.next();
            channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
            System.out.println("发送消息：" + message);
        }
    }
}
