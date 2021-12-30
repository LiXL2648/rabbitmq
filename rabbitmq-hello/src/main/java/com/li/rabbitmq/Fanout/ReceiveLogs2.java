package com.li.rabbitmq.Fanout;

import com.li.rabbitmq.util.RabbitMQUtil;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

public class ReceiveLogs2 {

    private static final String EXCHANGE_NAME = "logs";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMQUtil.getChannel();

        /*
         * 声明交换机
         * exchange：交换机名称
         * type：交换机类型
         */
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.FANOUT);
        // 声明临时队列，当消费者与队列断开连接时自动删除队列
        String queue = channel.queueDeclare().getQueue();
        /*
         * 交换机绑定队列
         * queue：队列名称
         * exchange：交换机名称
         * routingKey：路由键
         */
        channel.queueBind(queue, EXCHANGE_NAME, "");
        System.out.println("ReceiveLogs2 等待接收消息……");
        DeliverCallback deliverCallback = (consumerTag, message) -> {
            System.out.println("ReceiveLogs2 接收到的消息："+ new String(message.getBody()));
        };
        channel.basicConsume(queue, true, deliverCallback, consumerTag ->{});
    }
}
