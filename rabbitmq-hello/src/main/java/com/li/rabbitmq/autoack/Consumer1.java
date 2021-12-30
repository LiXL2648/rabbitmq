package com.li.rabbitmq.autoack;

import com.li.rabbitmq.util.RabbitMQUtil;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

public class Consumer1 {

    private static final String QUEUE_NAME = "hello";

    public static void main(String[] args) throws Exception {

        Channel channel = RabbitMQUtil.getChannel();
        System.out.println("C1 等待接收消息，处理消息时间较短");
        DeliverCallback deliverCallback = (consumerTag, message) -> {
            // 睡眠1秒钟
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ignored) {}
            System.out.println("C1接收消息：" + new String(message.getBody()));
            // 手动应答
            /*
             * deliveryTag：消息标记
             * multiple：是否批量应答，一般是不批量，即处理一条应答一次
             */
            channel.basicAck(message.getEnvelope().getDeliveryTag(), false);
        };

        CancelCallback cancelCallback = consumerTag -> System.out.println("C1消息接收中断");
        int prefetchCount = 2;
        channel.basicQos(prefetchCount);
        channel.basicConsume(QUEUE_NAME, false, deliverCallback, cancelCallback);
    }
}
