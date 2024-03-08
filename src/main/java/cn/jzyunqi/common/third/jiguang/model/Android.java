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
public class Android implements Serializable {
    private static final long serialVersionUID = -8158944737845210348L;

    /**
     * 通知内容，必填
     */
    private String alert;

    /**
     * 通知标题
     */
    private String title;

    /**
     * 通知栏样式ID
     */
    @JsonProperty("builder_id")
    private Integer builderId;

    /**
     * 通知栏展示优先级
     */
    private Integer priority;

    /**
     * 通知栏条目过滤或排序
     */
    private String category;

    /**
     * 通知栏样式类型
     */
    private Integer style;

    /**
     * 通知提醒方式
     */
    @JsonProperty("alert_type")
    private Integer alertType;

    /**
     * 大文本通知栏样式
     */
    @JsonProperty("big_text")
    private Integer bigText;

    /**
     * 文本条目通知栏样式
     */
    private Object inbox;

    /**
     * 大图片通知栏样式
     */
    @JsonProperty("big_pic_path")
    private Integer bigPicPath;

    /**
     * 扩展字段
     */
    private Object extras;
}