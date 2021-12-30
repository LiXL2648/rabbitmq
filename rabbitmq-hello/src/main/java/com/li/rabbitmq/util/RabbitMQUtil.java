package com.li.rabbitmq.util;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class RabbitMQUtil {

    public static Channel getChannel() throws Exception {
        // 创建一个连接工厂
        ConnectionFactory connectionFactory = new ConnectionFactory();
        // 设置连接 RabbitMQ 的 IP
        connectionFactory.setHost("10.10.0.26");
        // 设置用户名
        connectionFactory.setUsername("admin");
        // 设置密码
        connectionFactory.setPassword("2648");
        // 创建连接
        Connection connection = connectionFactory.newConnection();
        // 获取信道
        return connection.createChannel();
    }
}
