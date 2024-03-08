package cn.jzyunqi.common.third.jiguang.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author wiiyaya
 * @date 2018/8/17.
 */
@Getter
@Setter
public class Trigger implements Serializable {
    private static final long serialVersionUID = 3809483476586829590L;

    /**
     * 简单触发器
     */
    private SingleTrigger single;

    /**
     * 定期触发器
     */
    private PeriodicalTrigger periodical;
}
