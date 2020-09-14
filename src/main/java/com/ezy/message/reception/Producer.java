package com.ezy.message.reception;

import com.ezy.message.config.RabbitConfig;
import com.ezy.message.entity.RabbitMessage;
import com.ezy.message.service.IRabbitMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.UUID;

/**
 * @author Caixiaowei
 * @ClassName Sender.java
 * @Description mq 生产者
 * @createTime 2020年07月30日 17:20:00
 */
@Component
@Slf4j
public class Producer {


    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private IRabbitMessageService rabbitMessageService;

    public void send() {
        String context = "hello " + new Date();
        System.out.println("Sender : " + context);
        this.rabbitTemplate.convertAndSend("hello", context);
    }

    /**
     * 写入mq
     * @description
     * @author Caixiaowei
     * @param queue string 队列名称
     * @param messageId Long 消息id
     * @param content string 消息数据
     * @updateTime 2020/7/31 10:45
     * @return
     */
    public void send(String queue, Long messageId, String content) {

        Message message = MessageBuilder.withBody(content.getBytes())
                .setContentType(MessageProperties.CONTENT_TYPE_JSON).setContentEncoding("utf-8")
                .setMessageId(String.valueOf(messageId)).build();

        // 构建重试confirm 内容
        CorrelationData correlationData = new CorrelationData();
        correlationData.setId(String.valueOf(messageId));
        correlationData.setReturnedMessage(message);
        try {
            this.rabbitTemplate.convertAndSend(queue, message, correlationData);
        } catch (Exception e) {
            // 发送消息异常, 更新消息发送状态: 未发送
            RabbitMessage rabbitMessage = new RabbitMessage();
            rabbitMessage.setId(messageId);
            rabbitMessage.setIsSend(false);
            boolean update = rabbitMessageService.updateById(rabbitMessage);
            log.error("消息写入MQ 异常, 消息id: {}", messageId);
        }

    }

    public void  sendMessage(String content){

        Message message = MessageBuilder.withBody(content.getBytes())
                .setContentType(MessageProperties.CONTENT_TYPE_JSON).setContentEncoding("utf-8")
                .setMessageId(UUID.randomUUID() + "").build();
        CorrelationData correlationData = new CorrelationData();
        correlationData.setId("111");
        correlationData.setReturnedMessage(message);
//        this.rabbitTemplate.convertAndSend(queue, message);
        rabbitTemplate.convertAndSend(RabbitConfig.QUEUE_APPROVAL, message, correlationData);
    }

    public void send(String exchange, String routingKey, String content) {
        rabbitTemplate.convertAndSend(exchange, routingKey, content);
    }
}
