package com.li.rabbitmq.hello;

import com.li.rabbitmq.util.RabbitMQUtil;
import com.rabbitmq.client.Channel;

/**
 * 生产者，发送消息
 */
public class Producer {

    private static final String QUEUE_NAME = "hello";

    public static void main(String[] args) throws Exception {

        Channel channel = RabbitMQUtil.getChannel();
        /*
         * 声明一个队列
         * queue: 队列名称
         * durable：是否持久化
         * exclusive：是否进行消息共享，true：多个消费者消费，false：单个消费者消费
         * autoDelete：是否自动删除
         * arguments：其他参数
         */
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        String message = "Hello World";
        /*
         * 发送消息
         * exchange：交换机
         * routingKey：路由键，本次是队列名称
         * props：其他参数
         * body：消息
         */
        channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
        System.out.println("消息发送完毕");
    }
}
