package cn.jzyunqi.common.third.jiguang.response;

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
public class MessagePushRsp implements Serializable {
    private static final long serialVersionUID = 4631584492879228474L;

    @JsonProperty("sendno")
    private String sendNo;

    @JsonProperty("msg_id")
    private String msgId;

}
