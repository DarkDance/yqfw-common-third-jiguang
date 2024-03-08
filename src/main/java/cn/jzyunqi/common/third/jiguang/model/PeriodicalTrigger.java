package cn.jzyunqi.common.third.jiguang.model;

import cn.jzyunqi.common.third.jiguang.enums.TimeUnit;
import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

/**
 * @author wiiyaya
 * @date 2018/8/17.
 */
@Getter
@Setter
public class PeriodicalTrigger implements Serializable {
    private static final long serialVersionUID = -2692745738724623552L;

    /**
     * 开始时间 YYYY-mm-dd HH:MM:SS
     */
    private LocalDateTime start;

    /**
     * 结束时间 YYYY-mm-dd HH:MM:SS
     */
    private LocalDateTime end;

    /**
     * 定期执行时间 HH:MM:SS
     */
    private LocalTime time;

    /**
     * 最小时间单位
     */
    @JsonProperty("time_unit")
    @JsonEnumDefaultValue
    private TimeUnit timeUnit;

    /**
     * 执行周期 1 - 100
     */
    private Integer frequency;

    /**
     * 当time_unit为week时，point为星期"MON","TUE","WED","THU","FRI","SAT","SUN"
     * 当time_unit为month时，point为合法日期01 - 31
     */
    private List<String> point;
}
