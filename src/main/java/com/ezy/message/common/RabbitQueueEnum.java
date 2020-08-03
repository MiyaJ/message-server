package com.ezy.message.common;

import org.apache.commons.lang3.EnumUtils;

import java.util.List;

/**
 * @author Caixiaowei
 * @ClassName RabbitQueueEnum.java
 * @Description rabbit 队列枚举
 * @createTime 2020年07月31日 10:42:00
 */
public enum RabbitQueueEnum {

    /**
     * hello
     */
    HELLO("hello", "测试"),

    /**
     * 审批
     */
    APPROVAL("approval", "审批"),

    /**
     * 审批
     */
    CONTACT("contact", "通讯录");

    private String name;

    private String desc;

    public static List<RabbitQueueEnum> list = EnumUtils.getEnumList(RabbitQueueEnum.class);

    RabbitQueueEnum(String name, String desc) {
        this.name = name;
        this.desc = desc;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
