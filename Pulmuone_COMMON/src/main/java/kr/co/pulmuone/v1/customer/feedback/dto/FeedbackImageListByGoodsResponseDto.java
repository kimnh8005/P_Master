package kr.co.pulmuone.v1.customer.feedback.dto;

import kr.co.pulmuone.v1.customer.feedback.dto.vo.FeedbackImageListByGoodsResultVo;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@Builder
@ToString
public class FeedbackImageListByGoodsResponseDto {

    private int total;

    private List<FeedbackImageListByGoodsResultVo> dataList;

}
