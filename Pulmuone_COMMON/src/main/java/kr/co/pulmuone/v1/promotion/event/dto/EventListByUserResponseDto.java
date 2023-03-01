package kr.co.pulmuone.v1.promotion.event.dto;

import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.promotion.event.dto.vo.EventListByUserVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class EventListByUserResponseDto {

    @ApiModelProperty(value = "목록 총 갯수")
    private int total;

    @ApiModelProperty(value = "이벤트 정보")
    private List<EventListByUserVo> event;

}
