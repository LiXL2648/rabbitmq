package com.li.rabbitmq.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

@Slf4j
@Component
public class MyCallBack implements RabbitTemplate.ConfirmCallback, RabbitTemplate.ReturnsCallback {

    @Resource
    private RabbitTemplate rabbitTemplate;

    @PostConstruct
    public void init() {
        // 将回调注入rabbit
        rabbitTemplate.setConfirmCallback(this);
        /*
         * true：交换机无法将消息进行路由时，会将该消息返回给生产者
         * false：如果发现消息无法进行路由，则直接丢弃
         */
        rabbitTemplate.setMandatory(true);
        rabbitTemplate.setReturnsCallback(this);
    }

    /**
     * 交换机确认回调方法
     * 发消息，交换机接收成功或者失败的回调
     *  correlationData：保存回调消息的ID及相关信息
     *  ack：交换机是否接收到消息，true：接收成功，false：接收失败
     *  cause：null/失败的原因
     */
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        String id = correlationData == null ? null : correlationData.getId();
        if (ack) {
            log.info("交换机已经收到Id为{}的消息", id);
        } else {
            log.info("交换机还未收到Id为{}的消息，原因为：{}", id, cause);
            // 在此可以进行消息缓存或者重新发送
        }
    }

    @Override
    public void returnedMessage(ReturnedMessage returnedMessage) {
        log.info("消息：{}被交换机{}退回，原因是{}",
                new String(returnedMessage.getMessage().getBody()), returnedMessage.getExchange(), returnedMessage.getReplyText());
    }
}
