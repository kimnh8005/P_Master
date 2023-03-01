package kr.co.pulmuone.v1.customer.qna.dto;

import io.swagger.annotations.ApiModel;
import kr.co.pulmuone.v1.customer.qna.dto.vo.QnaListByUserVo;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@Builder
@ToString
@ApiModel(description = "GetQnaListByUserResponseDto")
public class QnaListByUserResponseDto {
    private int total;
    private List<QnaListByUserVo> qnaList;
}
