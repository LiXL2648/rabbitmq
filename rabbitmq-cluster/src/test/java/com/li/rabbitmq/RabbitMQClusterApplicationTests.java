package com.li.rabbitmq;

import com.li.rabbitmq.config.MirrorConfig;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
public class RabbitMQClusterApplicationTests {

    @Resource
    private RabbitTemplate rabbitTemplate;

    @Test
    public void producer() {
        String msg = "hello world";
        rabbitTemplate.convertAndSend(MirrorConfig.MIRROR_EXCHANGE_NAME, MirrorConfig.MIRROR_ROUTING_KEY, msg.getBytes());
    }
}
