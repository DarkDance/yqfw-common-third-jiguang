package cn.jzyunqi.common.third.jiguang.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author wiiyaya
 * @date 2018/8/17.
 */
@Getter
@Setter
public class MessagePushReqDto implements Serializable {
    private static final long serialVersionUID = -3148062519853659228L;

    /**
     * 推送平台设置
     */
    private Object platform = "all";

    /**
     * 推送设备指定
     */
    private Object audience = "all";

    /**
     * 通知内容体。是被推送到客户端的内容。与 message 一起二者必须有其一，可以二者并存
     */
    private Notification notification;

    /**
     * 消息内容体
     */
    private Message message;

    /**
     * 短信补充
     */
    @JsonProperty("sms_message")
    private SmsMessage smsMessage;

    /**
     * 推送参数
     */
    private Options options = new Options();

}
