package cn.jzyunqi.common.third.jiguang.response;

import cn.jzyunqi.common.third.jiguang.model.ScheduleDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * @author wiiyaya
 * @date 2018/8/21.
 */
@Getter
@Setter
public class SchedulePageRsp implements Serializable {
    private static final long serialVersionUID = 5395426544485040235L;

    @JsonProperty("total_count")
    private Integer totalCount;

    @JsonProperty("total_pages")
    private Integer totalPages;

    private Integer page;

    private List<ScheduleDto> schedules;
}
