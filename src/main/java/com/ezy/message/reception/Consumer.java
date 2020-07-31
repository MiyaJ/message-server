package com.ezy.message.reception;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author Caixiaowei
 * @ClassName Consumer.java
 * @Description TODO
 * @createTime 2020年07月30日 17:31:00
 */
@Component
@RabbitListener(queues = {"hello"})
public class Consumer {

    @RabbitHandler
    public void process(String hello) {
        System.out.println("Receiver : " + hello);
    }
}
