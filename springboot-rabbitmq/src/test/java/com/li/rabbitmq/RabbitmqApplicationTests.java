package com.li.rabbitmq;

import com.rabbitmq.client.AMQP;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
public class RabbitmqApplicationTests {

    @Resource
    private RabbitTemplate rabbitTemplate;

    @Test
    public void testPriority() {

        for (int i = 1; i < 11; i++) {
            if (i % 5 == 0) {
                rabbitTemplate.convertAndSend("", "priority_queue", i + "", message -> {
                    message.getMessageProperties().setPriority(10);
                    return message;
                });
            } else {
                rabbitTemplate.convertAndSend("", "priority_queue", i + "");
            }
        }
    }
}
