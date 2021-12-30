package com.li.rabbitmq.consumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ConfirmConsumer {

    @RabbitListener(queues = "confirm_queue")
    public void receiveConfirmMsg(Message message) {
        log.info("接收到消息：{}", new String(message.getBody()));
    }

    @RabbitListener(queues = "warning_queue")
    public void receiveWarningMsg(Message message) {
        String msg = new String(message.getBody());
        log.info("报警发现不可路由消息：{}", msg);
    }

    @RabbitListener(queues = "priority_queue")
    public void receivePriorityMeg(Message message) {
        System.out.println(new String(message.getBody()));
    }
}
