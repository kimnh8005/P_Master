package kr.co.pulmuone.v1.customer.qna.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "1:1문의 답변 Result")
public class QnaAnswerByCsQnaIdVo {

    @ApiModelProperty(value = "답변유형")
    private String answerType;

    @ApiModelProperty(value = "답변")
    private String answer;

    @ApiModelProperty(value = "답변일")
    private String answerDate;

    @ApiModelProperty(value = "답변일시")
    private String answerDateTime;
}
