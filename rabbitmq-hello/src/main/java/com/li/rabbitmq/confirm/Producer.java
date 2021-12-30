package com.li.rabbitmq.confirm;

import com.li.rabbitmq.util.RabbitMQUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmCallback;
import com.rabbitmq.client.MessageProperties;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

public class Producer {

    private static final String QUEUE_NAME = "hello";

    private static final int MESSAGE_COUNT = 1000;

    public static void main(String[] args) throws Exception {
        // singleConfirm(); // 725ms
        // multipleConfirm(); // 100ms
        asyncConfirm(); // 67ms
    }

    // 单个发布确认
    public static void singleConfirm() throws Exception {
        Channel channel = RabbitMQUtil.getChannel();
        // 发布确认
        channel.confirmSelect();
        // 声明队列为持久化
        channel.queueDeclare(QUEUE_NAME, true, false, false, null);
        long start = System.currentTimeMillis();
        for (int i = 0; i < MESSAGE_COUNT; i++) {
            String message = i + "";
            channel.basicPublish("", QUEUE_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes());
            // 单个确认
            boolean confirms = channel.waitForConfirms();
            System.out.println(confirms ? "发送成功" : "发送失败");
        }
        long end = System.currentTimeMillis();
        System.out.println("发布" + MESSAGE_COUNT + "个单独确认消息，耗时：" + (end- start) + "ms");
    }

    // 批量发布确认
    public static void multipleConfirm() throws Exception {
        Channel channel = RabbitMQUtil.getChannel();
        // 发布确认
        channel.confirmSelect();
        // 声明队列为持久化
        channel.queueDeclare(QUEUE_NAME, true, false, false, null);
        long start = System.currentTimeMillis();

        int batchSize = 100;
        for (int i = 1; i <= MESSAGE_COUNT; i++) {
            String message = i + "";
            channel.basicPublish("", QUEUE_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes());
            // 批量确认
            if (i % batchSize == 0) {
                channel.waitForConfirms();
            }
        }
        long end = System.currentTimeMillis();
        System.out.println("发布" + MESSAGE_COUNT + "个批量确认消息，耗时：" + (end- start) + "ms");
    }

    // 异步发布确认
    public static void asyncConfirm() throws Exception {
        Channel channel = RabbitMQUtil.getChannel();
        // 发布确认
        channel.confirmSelect();
        // 声明队列为持久化
        channel.queueDeclare(QUEUE_NAME, true, false, false, null);
        /*
         * 线程安全有序的哈希表，适用于高并发的情况下
         * 将序号与消息关联
         * 批量删除消息
         * 支持高并发（多线程）
         */
        ConcurrentSkipListMap<Long, String> outStandingConfirms = new ConcurrentSkipListMap<>();

        /*
         * deliveryTag：消息标记
         * multiple：是否批量
         */
        // 消息确认成功回调函数
        ConfirmCallback ackCallback = (deliveryTag, multiple) -> {
            System.out.println("确认的消息：" + deliveryTag);
            // 删除确认的消息，剩下的就是未确认的消息
            if (multiple) {
                ConcurrentNavigableMap<Long, String> confirmed = outStandingConfirms.headMap(deliveryTag);
                confirmed.clear();
            } else {
                outStandingConfirms.remove(deliveryTag);
            }
        };
        // 消息确认失败回调函数
        ConfirmCallback nackCallback = (deliveryTag, multiple) -> {
            String message = outStandingConfirms.get(deliveryTag);
            System.out.println("未确认的消息：" + deliveryTag + ", " + message);
        };;

        // 准备监听器，监听哪些消息成功，哪些消除失败，异步
        channel.addConfirmListener(ackCallback, nackCallback);
        long start = System.currentTimeMillis();
        for (int i = 0; i < MESSAGE_COUNT; i++) {
            String message = i + "";
            channel.basicPublish("", QUEUE_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes());
            // 记录发送的消息
            outStandingConfirms.put(channel.getNextPublishSeqNo() - 1 , message);
        }

        long end = System.currentTimeMillis();
        System.out.println("发布" + MESSAGE_COUNT + "个异步确认消息，耗时：" + (end- start) + "ms");
    }
}
