package kr.co.pulmuone.v1.customer.feedback.dto;

import kr.co.pulmuone.v1.customer.feedback.dto.vo.FeedbackListByGoodsResultVo;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@Builder
@ToString
public class FeedbackListByGoodsResponseDto {

    private int total;

    private List<FeedbackListByGoodsResultVo> dataList;

}
