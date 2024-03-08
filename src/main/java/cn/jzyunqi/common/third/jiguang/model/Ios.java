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
public class Ios implements Serializable {
    private static final long serialVersionUID = -1844879009886804112L;

    /**
     * 通知内容，必填
     */
    private String alert;

    /**
     * 通知提示声音
     */
    private String sound;

    /**
     * 应用角标
     */
    private Integer badge;

    /**
     * 推送唤醒
     */
    @JsonProperty("content-available")
    private Boolean contentAvailable;

    /**
     * 通知扩展,iOS10
     */
    @JsonProperty("mutable-content")
    private Boolean mutableContent;

    /**
     * IOS8才支持
     */
    private String category;

    /**
     * 扩展字段
     */
    private Object extras;
}
