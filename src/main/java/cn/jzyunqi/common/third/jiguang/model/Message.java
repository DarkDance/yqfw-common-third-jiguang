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
public class Message implements Serializable {
    private static final long serialVersionUID = 4783766153291556434L;

    /**
     * 消息内容本身
     */
    @JsonProperty("msg_content")
    private String msgContent;

    /**
     * 消息标题
     */
    private String title;

    /**
     * 消息内容类型
     */
    @JsonProperty("content_type")
    private String contentType;

    /**
     * 扩展字段
     */
    private Object extras;
}
