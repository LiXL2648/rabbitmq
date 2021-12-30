package com.li.rabbitmq.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Slf4j
@RestController
@RequestMapping("/confirm")
public class ConfirmController {

    @Resource
    private RabbitTemplate rabbitTemplate;

    @GetMapping("/sendConfirmMsg/{msg}")
    public void sendConfirmMsg(@PathVariable("msg") String msg) {
        log.info("发送消息：{}", msg);
        CorrelationData correlationData = new CorrelationData("1");
        rabbitTemplate.convertAndSend("confirm_exchange", "confirm1", msg, correlationData);
    }
}
