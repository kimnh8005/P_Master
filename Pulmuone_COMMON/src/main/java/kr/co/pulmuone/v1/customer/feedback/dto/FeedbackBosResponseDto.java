package kr.co.pulmuone.v1.customer.feedback.dto;

import java.util.List;

import kr.co.pulmuone.v1.customer.feedback.dto.vo.FeedbackBosListVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class FeedbackBosResponseDto {


    private long total;

    private List<FeedbackBosListVo> rows;
}
