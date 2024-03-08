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
public class ScheduleDto implements Serializable {
    private static final long serialVersionUID = -6113128590884680099L;

    /**
     * 定时器id
     */
    @JsonProperty("schedule_id")
    private String id;

    /**
     * 定时器名称
     */
    private String name;

    /**
     * 是否启用
     */
    private Boolean enabled;

    /**
     * 触发器
     */
    private Trigger trigger;

    /**
     * 推送消息
     */
    private MessagePushReqDto push;
}
