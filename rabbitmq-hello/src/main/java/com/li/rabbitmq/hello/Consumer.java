package com.li.rabbitmq.hello;

import com.li.rabbitmq.util.RabbitMQUtil;
import com.rabbitmq.client.*;

/**
 * 消费者，接收消息
 */
public class Consumer {

    private static final String QUEUE_NAME = "hello";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMQUtil.getChannel();
        /*
         * 消费者消费消息
         * queue 队列名称
         * autoAck 是否自动应答
         * deliverCallback 消费者接收消息的回调
         * cancelCallback 消费者取消消费的回调
         */
        DeliverCallback deliverCallback = (consumerTag, message)-> {
            String msg = new String(message.getBody());
            System.out.println(msg);
        };

        CancelCallback cancelCallback = consumerTag -> System.out.println("消息消费中断");
        channel.basicConsume(QUEUE_NAME, true, deliverCallback, cancelCallback);
    }
}
