package com.ezy.message.reception;

import com.ezy.message.config.RabbitConfig;
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
public class Producer {


    @Autowired
    private RabbitTemplate rabbitTemplate;

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
     * @param content string 消息数据
     * @updateTime 2020/7/31 10:45
     * @return
     */
    public void send(String queue, String content) {
        Message message = MessageBuilder.withBody(content.getBytes())
                .setContentType(MessageProperties.CONTENT_TYPE_JSON).setContentEncoding("utf-8")
                .setMessageId(UUID.randomUUID() + "").build();
        this.rabbitTemplate.convertAndSend(queue, message);
    }

    public void  sendMessage(String content){
        Message message = MessageBuilder.withBody(content.getBytes())
                .setContentType(MessageProperties.CONTENT_TYPE_JSON).setContentEncoding("utf-8")
                .setMessageId(UUID.randomUUID() + "").build();
//        this.rabbitTemplate.convertAndSend(queue, message);
        rabbitTemplate.convertAndSend(RabbitConfig.QUEUE_APPROVAL, message);
    }

    public void send(String exchange, String routingKey, String content) {
        rabbitTemplate.convertAndSend(exchange, routingKey, content);
    }
}
