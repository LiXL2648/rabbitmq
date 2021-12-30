package com.li.rabbitmq.workqueues;

import com.li.rabbitmq.util.RabbitMQUtil;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

public class Worker {

    private static final String QUEUE_NAME = "hello";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMQUtil.getChannel();

        DeliverCallback deliverCallback = (consumerTag, message) -> {
            String msg = new String(message.getBody());
            System.out.println("接收到的消息：" + msg);
        };
        CancelCallback cancelCallback =  consumerTag -> System.out.println("消息消费中断");
        System.out.println("Worker02 等待接收消息");
        channel.basicConsume(QUEUE_NAME, true, deliverCallback, cancelCallback);
    }
}
