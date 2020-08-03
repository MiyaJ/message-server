package com.ezy.message.controller;

import com.alibaba.fastjson.JSONObject;
import com.ezy.message.common.RabbitQueueEnum;
import com.ezy.message.model.callback.approval.ApprovalInfo;
import com.ezy.message.model.callback.approval.third.AppravalCallbackMessage;
import com.ezy.message.model.callback.approval.third.ApprovalNode;
import com.ezy.message.model.callback.approval.third.ApprovalNodeItem;
import com.ezy.message.model.callback.approval.third.ApprovalNotifyNode;
import com.ezy.message.model.callback.contact.Contact;
import com.ezy.message.model.callback.contact.ExtAttr;
import com.ezy.message.model.callback.contact.Item;
import com.ezy.message.model.callback.contact.ItemText;
import com.ezy.message.reception.Producer;
import com.ezy.message.utils.crypto.WxCryptUtil;
import com.thoughtworks.xstream.XStream;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Caixiaowei
 * @ClassName CallBackController.java
 * @Description 企微callback 控制层
 * @createTime 2020年07月31日 10:06:00
 */
@RestController
@RequestMapping("/v1/qywx/callback")
@Slf4j
public class CallBackController {

    @Autowired
    private Producer producer;

    private String token;
    private String encodingAesKey;
    private String corpid;

    /**
     * 审批状态变更回调通知
     * @description 
     * @author Caixiaowei
     * @param
     * @updateTime 2020/8/3 15:24 
     * @return 
     */
    @RequestMapping("/approval")
    public void approval(HttpServletRequest request, HttpServletResponse response) {
        // 加密签名
        String msgSignature = request.getParameter("msg_signature");
        String timestamp = request.getParameter("timestamp");
        String nonce = request.getParameter("nonce");
        // 加密的字符串。需要解密得到消息内容明文，解密后有random、msg_len、msg、receiveid四个字段，其中msg即为消息内容明文
        String echostr = request.getParameter("echostr");

        try {

            WxCryptUtil wxCryptUtil = new WxCryptUtil(token, encodingAesKey, corpid);
            String msgXmlStr = wxCryptUtil.decrypt(msgSignature, timestamp, nonce, echostr);
            //必须要返回解密之后的明文
            response.getWriter().write(msgXmlStr);

            log.info("msgXmlStr --->{}", msgXmlStr);

            XStream xstream = new XStream();
            //使用注解修改对象名称
            xstream.processAnnotations(new Class[]{AppravalCallbackMessage.class, ApprovalInfo.class, ApprovalNode.class,
                    ApprovalNodeItem.class, ApprovalNotifyNode.class});
            //将字符串类型的xml转换为对象
            AppravalCallbackMessage callbackMessage = (AppravalCallbackMessage) xstream.fromXML(msgXmlStr);

            // TODO: 2020/7/1 回调信息返回调用方: mq?
            producer.send(RabbitQueueEnum.APPROVAL.getName(), JSONObject.toJSONString(callbackMessage));
        } catch (Exception e) {

        }

    }

    /**
     * 通讯录回调通知
     * @description
     * @author Caixiaowei
     * @param
     * @updateTime 2020/8/3 15:24
     * @return
     */
    @RequestMapping("/contact")
    public void contact(HttpServletRequest request, HttpServletResponse response) {
        // 加密签名
        String msgSignature = request.getParameter("msg_signature");
        String timestamp = request.getParameter("timestamp");
        String nonce = request.getParameter("nonce");
        // 加密的字符串。需要解密得到消息内容明文，解密后有random、msg_len、msg、receiveid四个字段，其中msg即为消息内容明文
        String echostr = request.getParameter("echostr");

        try {

            WxCryptUtil wxCryptUtil = new WxCryptUtil(token, encodingAesKey, corpid);
            String msgXmlStr = wxCryptUtil.decrypt(msgSignature, timestamp, nonce, echostr);
            //必须要返回解密之后的明文
            response.getWriter().write(msgXmlStr);

            log.info("msgXmlStr --->{}", msgXmlStr);

            XStream xstream = new XStream();
            xstream.processAnnotations(new Class[]{Contact.class, ExtAttr.class, Item.class, ItemText.class});
            // 解析通讯类
            Contact contact = (Contact) xstream.fromXML(msgXmlStr);

            // TODO: 2020/7/1 回调信息返回调用方: mq?
            producer.send(RabbitQueueEnum.CONTACT.getName(), JSONObject.toJSONString(contact));
        } catch (Exception e) {

        }

    }
}
