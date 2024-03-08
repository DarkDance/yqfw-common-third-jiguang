package cn.jzyunqi.common.third.jiguang.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author wiiyaya
 * @date 2018/8/20.
 */
@Getter
@Setter
public class SmsMessage implements Serializable {
    private static final long serialVersionUID = 1138103937243624015L;

    @JsonProperty("delay_time")
    private Integer delayTime;

    @JsonProperty("temp_id")
    private Long tempId;

    @JsonProperty("temp_para")
    private Object tempPara;
}
