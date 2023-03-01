package kr.co.pulmuone.v1.promotion.event.dto;

import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.customer.feedback.dto.vo.FeedbackTargetListByUserResultVo;
import kr.co.pulmuone.v1.promotion.event.dto.vo.EventListFromMyPageVo;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@Builder
@ToString
public class EventListFromMyPageResponseDto {

    @ApiModelProperty(value = "목록 총 갯수")
    private int total;

    @ApiModelProperty(value = "이벤트 정보")
    private List<EventListFromMyPageVo> list;

}
