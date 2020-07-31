package com.ezy.message.controller;

import com.ezy.message.reception.Producer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Caixiaowei
 * @ClassName RabbitController.java
 * @Description TODO
 * @createTime 2020年07月30日 17:54:00
 */
@RestController
@RequestMapping("/rabbit")
public class RabbitController {

    @Autowired
    private Producer producer;

    @GetMapping("/send")
    public Object send() {
        producer.send();
        return "sucess";
    }
}
