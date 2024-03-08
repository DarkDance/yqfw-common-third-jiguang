package cn.jzyunqi.common.third.jiguang.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * @author wiiyaya
 * @date 2018/8/17.
 */
@Getter
@Setter
public class Audience implements Serializable {
    private static final long serialVersionUID = -2791224008136920014L;

    /**
     * 标签
     */
    private List<String> tag;

    /**
     * 标签AND
     */
    @JsonProperty("tag_and")
    private List<String> tagAnd;

    /**
     * 标签NOT
     */
    @JsonProperty("tag_not")
    private List<String> tagNot;

    /**
     * 别名
     */
    private List<String> alias;

    /**
     * 注册ID
     */
    @JsonProperty("registration_id")
    private List<String> registrationId;

    /**
     * 用户分群ID
     */
    private List<String> segment;

    /**
     * A/B Test ID
     */
    @JsonProperty("abtest")
    private List<String> abTest;
}
