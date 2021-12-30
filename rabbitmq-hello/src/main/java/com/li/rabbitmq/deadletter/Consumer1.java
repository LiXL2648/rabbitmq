package com.li.rabbitmq.deadletter;

import com.li.rabbitmq.util.RabbitMQUtil;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.util.HashMap;
import java.util.Map;

public class Consumer1 {

    // 普通交换机
    private static final String NORMAL_EXCHANGE_NAME = "normal_exchange";

    // 普通队列
    private static final String NORMAL_QUEUE_NAME = "normal_queue";

    // 死信交换机
    private static final String DEAD_EXCHANGE_NAME = "dead_exchange";

    // 死信队列
    private static final String DEAD_QUEUE_NAME = "dead_queue";

    public static void main(String[] args) throws Exception {

        Channel channel = RabbitMQUtil.getChannel();
        // 普通交换机
        channel.exchangeDeclare(NORMAL_EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
        // 死信交换机
        channel.exchangeDeclare(DEAD_EXCHANGE_NAME, BuiltinExchangeType.DIRECT);

        // 设置参数
        Map<String, Object> arguments = new HashMap<>();
        // 设置过期时间
        // arguments.put("x-message-ttl", 10000);
        // 正常队列设置死信交换机
        arguments.put("x-dead-letter-exchange", DEAD_EXCHANGE_NAME);
        // 设置死信 RoutingKey
        arguments.put("x-dead-letter-routing-key", "dead");
        // 正常队列的最大接收长度
        // arguments.put("x-max-length", 6);
        // 普通队列
        channel.queueDeclare(NORMAL_QUEUE_NAME, false, false, false, arguments);
        // 死信队列
        channel.queueDeclare(DEAD_QUEUE_NAME, false, false, false, null);

        // 绑定普通交换机和普通队列
        channel.queueBind(NORMAL_QUEUE_NAME, NORMAL_EXCHANGE_NAME, "normal");
        // 绑定死信交换机和死信队列
        channel.queueBind(DEAD_QUEUE_NAME, DEAD_EXCHANGE_NAME, "dead");

        System.out.println("Consumer1 等待接收消息……");
        DeliverCallback deliverCallback = (consumerTag, message) -> {
            String msg = new String(message.getBody());
            if (msg.equals("EE")) {
                System.out.println("Consumer1 拒绝的消息：" + msg);
                // 消息拒绝
                channel.basicReject(message.getEnvelope().getDeliveryTag(), false);
            } else {
                System.out.println("Consumer1 接收的消息：" + msg);
                // 消息应答
                channel.basicAck(message.getEnvelope().getDeliveryTag(), false);
            }
        };

        // 消息拒绝一定要开启手动应答
        channel.basicConsume(NORMAL_QUEUE_NAME, false, deliverCallback, consumerTag -> {});
    }
}
