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
public class Options implements Serializable {
    private static final long serialVersionUID = -2230980244605708848L;

    /**
     * 推送序号
     */
    @JsonProperty("sendno")
    private Integer sendNo;

    /**
     * 离线消息保留时长(秒)
     */
    @JsonProperty("time_to_live")
    private Integer timeToLive;

    /**
     * 要覆盖的消息ID
     */
    @JsonProperty("override_msg_id")
    private Long overrideMsgId;

    /**
     * APNs是否生产环境
     */
    @JsonProperty("apns_production")
    private Boolean apnsProduction;

    /**
     * 更新 iOS 通知的标识符
     */
    @JsonProperty("apns_collapse_id")
    private String apnsCollapseId;

    /**
     * 定速推送时长(分钟)
     */
    @JsonProperty("big_push_duration")
    private Integer bigPushDuration;
}