package com.ezy.message.config;

import com.ezy.message.common.RabbitQueueEnum;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Caixiaowei
 * @ClassName RabbitConfig.java
 * @Description rabbit 配置
 * @createTime 2020年07月30日 17:32:00
 */
@Configuration
public class RabbitConfig {

    @Bean
    public Queue helloQueue() {
        return new Queue(RabbitQueueEnum.HELLO.getName());
    }

    @Bean
    public Queue approvalQueue() {
        return new Queue(RabbitQueueEnum.APPROVAL.getName());
    }

    @Bean
    public Queue addressBookQueue() {
        return new Queue(RabbitQueueEnum.CONTACT.getName());
    }
}
