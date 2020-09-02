package com.ezy.message.controller;

import cn.hutool.http.Method;
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
import com.ezy.message.utils.QywxCallBackUtils;
import com.ezy.message.utils.aes.WXBizMsgCrypt;
import com.thoughtworks.xstream.XStream;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${qywx.approval:token}")
    private String APPROVAL_TOKEN;
    @Value("${qywx.approval:encodingAesKey}")
    private String APPROVAL_ENCODING_AES_KEY;
    @Value("${qywx.contact:token}")
    private String CONTACT_TOKEN;
    @Value("${qywx.contact:encodingAesKey}")
    private String CONTACT_ENCODING_AES_KEY;
    @Value("${qywx.corpid}")
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
    public String approval(HttpServletRequest request) {

        String msgSig = request.getParameter("msg_signature");
        String timeStamp = request.getParameter("timestamp");
        String nonce = request.getParameter("nonce");
        String echostr = request.getParameter("echostr");
        log.info("msgSig:"+msgSig+"======timeStamp:"+timeStamp+"=====nonce:"+nonce+"=====echostr:"+echostr);
        try {
            WXBizMsgCrypt wxcpt = wxcpt(APPROVAL_TOKEN, APPROVAL_ENCODING_AES_KEY);
            if (Method.GET.name().equalsIgnoreCase(request.getMethod())){
                //需要返回的明文
                String sEchoStr;
                sEchoStr = wxcpt.VerifyURL(msgSig, timeStamp, nonce, echostr);
                return sEchoStr;

            }else{

                String data = QywxCallBackUtils.getStringInputstream(request);
                log.info("post请求数据: {}", data);
                String content = wxcpt.DecryptMsg(msgSig,timeStamp,nonce,data);
                log.info("解析后的消息: {}", content);
                XStream xstream = new XStream();
                //使用注解修改对象名称
                xstream.processAnnotations(new Class[]{AppravalCallbackMessage.class, ApprovalInfo.class, ApprovalNode.class,
                        ApprovalNodeItem.class, ApprovalNotifyNode.class});
                //将字符串类型的xml转换为对象

                AppravalCallbackMessage callbackMessage = (AppravalCallbackMessage) xstream.fromXML(content);

                // TODO: 2020/7/1 回调信息返回调用方: mq?
                producer.send(RabbitQueueEnum.APPROVAL.getName(), JSONObject.toJSONString(callbackMessage));
                return "success";
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("解析微信校验数据异常: {}", e.getMessage());
            throw new RuntimeException("解析微信校验数据异常");
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
    public String contact(HttpServletRequest request) {

        String msgSig = request.getParameter("msg_signature");
        String timeStamp = request.getParameter("timestamp");
        String nonce = request.getParameter("nonce");
        String echostr = request.getParameter("echostr");
        log.info("msgSig:"+msgSig+"======timeStamp:"+timeStamp+"=====nonce:"+nonce+"=====echostr:"+echostr);
        try {
            WXBizMsgCrypt wxcpt = wxcpt(CONTACT_TOKEN, CONTACT_ENCODING_AES_KEY);
            if (Method.GET.name().equalsIgnoreCase(request.getMethod())){

                //需要返回的明文
                String sEchoStr;
                sEchoStr = wxcpt.VerifyURL(msgSig, timeStamp, nonce, echostr);
                return sEchoStr;

            }else{
                String data = QywxCallBackUtils.getStringInputstream(request);
                log.info("post请求数据: {}", data);
                String content = wxcpt.DecryptMsg(msgSig,timeStamp,nonce,data);
                log.info("解析后的消息: {}", content);

                XStream xstream = new XStream();
                xstream.processAnnotations(new Class[]{Contact.class, ExtAttr.class, Item.class, ItemText.class});
                // 解析通讯类
                Contact contact = (Contact) xstream.fromXML(content);
                // TODO: 2020/7/1 回调信息返回调用方: mq?
                producer.send(RabbitQueueEnum.CONTACT.getName(), JSONObject.toJSONString(contact));
                return "success";
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("解析微信校验数据异常: {}", e.getMessage());
            throw new RuntimeException("解析微信校验数据异常");
        }
        
    }

    private WXBizMsgCrypt wxcpt(String qywxToken, String encodingKey){
        WXBizMsgCrypt wxcpt = null;
        try {
            wxcpt = new WXBizMsgCrypt(qywxToken, encodingKey, corpid);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("解析微信校验数据异常: {}", e.getMessage());
            throw new RuntimeException("企业微信校验异常");
        }
        return wxcpt;
    }

}
