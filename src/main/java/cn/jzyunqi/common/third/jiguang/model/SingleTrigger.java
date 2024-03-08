package cn.jzyunqi.common.third.jiguang.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author wiiyaya
 * @date 2018/8/17.
 */
@Getter
@Setter
public class SingleTrigger implements Serializable {
    private static final long serialVersionUID = 3819855153103025381L;

    /**
     * 时间 YYYY-mm-dd HH:MM:SS
     */
    private LocalDateTime time;
}
