package cn.jzyunqi.common.third.jiguang.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * @author wiiyaya
 * @date 2018/9/7.
 */
@Getter
@Setter
public class RegistrationIdListRsp implements Serializable {
    private static final long serialVersionUID = -2962632248196263191L;

    @JsonProperty("registration_ids")
    private List<String> registrationIdList;
}
