package cn.jzyunqi.common.third.jiguang.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * @author wiiyaya
 * @date 2018/9/7.
 */
@Getter
@Setter
@ToString
public class RegistrationIdDetailRsp implements Serializable {
    private static final long serialVersionUID = -1882321278591068930L;

    private List<String> tags;

    private String alias;

    private String mobile;
}
