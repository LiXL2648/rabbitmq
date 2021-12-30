package com.li.rabbitmq.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@RestController
@RequestMapping("/ttl")
public class SendMessageController {

    @Resource
    private RabbitTemplate rabbitTemplate;

    @GetMapping("/sendMessage/{message}")
    public void sendMessage(@PathVariable("message") String message) {
        log.info("当前时间：{}，发送一条消息给两个TTL队列：{}", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")), message);

        rabbitTemplate.convertAndSend("X", "XA", "消息来自 ttl 为 10s 的队列：" + message);
        rabbitTemplate.convertAndSend("X", "XB", "消息来自 ttl 为 40s 的队列：" + message);
    }

    @GetMapping("/sendExpireMessage/{message}/{ttlTime}")
    public void sendExpireMessage(@PathVariable("message") String msg, @PathVariable("ttlTime") String ttlTime) {
        log.info("当前时间：{}，发送一条时长{}毫秒的信息给队列QC：{}", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")), ttlTime, msg);
        rabbitTemplate.convertAndSend("X", "XC", msg, message -> {
            // 设置过期时间
            message.getMessageProperties().setExpiration(ttlTime);
            return message;
        });
    }

    @GetMapping("/sendDelayedMessage/{message}/{delayedTime}")
    public void sendDelayedMessage(@PathVariable("message") String msg, @PathVariable("delayedTime") Integer delayedTime) {
        log.info("当前时间：{}，发送一条时长{}毫秒的信息给延迟队列delayed_queue：{}", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")), delayedTime, msg);
        rabbitTemplate.convertAndSend("delayed_exchange", "delayed", msg, message -> {
            // 设置延迟时间
            message.getMessageProperties().setDelay(delayedTime);
            return message;
        });
    }
}
