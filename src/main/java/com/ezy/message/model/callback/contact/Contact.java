package com.ezy.message.model.callback.contact;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.Data;

import java.io.Serializable;

/**
 * @author Caixiaowei
 * @ClassName Contact.java
 * @Description 通讯录回调解析类
 * @createTime 2020年08月03日 14:04:00
 */
@Data
@XStreamAlias("xml")
public class Contact implements Serializable {
    private static final long serialVersionUID = 875529955182563340L;

    /******************** 部门变更 ***************************/
    /**
     *
     */
    @XStreamAlias("ToUserName")
    private String toUserName;

    /**
     *
     */
    @XStreamAlias("FromUserName")
    private String fromUserName;

    /**
     *
     */
    @XStreamAlias("CreateTime")
    private String createTime;

    /**
     *
     */
    @XStreamAlias("MsgType")
    private String msgType;

    /**
     *
     */
    @XStreamAlias("Event")
    private String event;

    /**
     *
     */
    @XStreamAlias("ChangeType")
    private String changeType;

    /**
     *
     */
    @XStreamAlias("Id")
    private String id;

    /**
     *
     */
    @XStreamAlias("Name")
    private String name;

    /**
     *
     */
    @XStreamAlias("ParentId")
    private String parentId;

    /**
     *
     */
    @XStreamAlias("Order")
    private String order;

    /******************* 标签变更 *********************/
    /**
     *
     */
    @XStreamAlias("TagId")
    private String tagId;

    /**
     *
     */
    @XStreamAlias("AddUserItems")
    private String addUserItems;

    /**
     *
     */
    @XStreamAlias("DelUserItems")
    private String delUserItems;

    /**
     *
     */
    @XStreamAlias("AddPartyItems")
    private String addPartyItems;

    /**
     *
     */
    @XStreamAlias("DelPartyItems")
    private String delPartyItems;

    /********************** 成员变更 ***************************/

    /**
     *
     */
    @XStreamAlias("UserID")
    private String userID;

    /**
     *
     */
    @XStreamAlias("NewUserID")
    private String newUserID;

    /**
     *
     */
    @XStreamAlias("Department")
    private String department;

    /**
     *
     */
    @XStreamAlias("IsLeaderInDept")
    private String isLeaderInDept;

    /**
     *
     */
    @XStreamAlias("Position")
    private String position;

    /**
     *
     */
    @XStreamAlias("Mobile")
    private String mobile;

    /**
     *
     */
    @XStreamAlias("Gender")
    private String gender;

    /**
     *
     */
    @XStreamAlias("Email")
    private String email;

    /**
     *
     */
    @XStreamAlias("Status")
    private String status;

    /**
     *
     */
    @XStreamAlias("Avatar")
    private String avatar;

    /**
     *
     */
    @XStreamAlias("Alias")
    private String alias;

    /**
     *
     */
    @XStreamAlias("Telephone")
    private String telephone;

    /**
     *
     */
    @XStreamAlias("Address")
    private String address;

    /**
     *
     */
    @XStreamAlias("ExtAttr")
    private ExtAttr extAttr;

}
