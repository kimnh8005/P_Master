package kr.co.pulmuone.v1.customer.qna.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "1:1문의 현황 Result")
public class QnaInfoByUserVo {
    
    @ApiModelProperty(value = "총 문의 갯수")
    private String totalCount;

    @ApiModelProperty(value = "문의 접수 갯수")
    private String receptionCount;

    @ApiModelProperty(value = "답변 확인중 갯수")
    private String answerCheckingCount;

    @ApiModelProperty(value = "답변 완료 갯수")
    private String answerCompletedCount;
}
