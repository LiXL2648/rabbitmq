package com.li.rabbitmq.consumer;

import com.li.rabbitmq.config.MirrorConfig;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class MirrorConsumer {

    @RabbitListener(queues = MirrorConfig.MIRROR_QUEUE_NAME)
    public void receiveMirrorMsg (Message message) {
        String msg = new String(message.getBody());
        System.out.println("收到的消息：" + msg);
    }
}
