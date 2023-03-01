package kr.co.pulmuone.v1.promotion.event.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class SurveyJoinDetailRequestDto {

    @ApiModelProperty(value = "설문 이벤트 문항 PK")
    private Long evEventSurveyQuestionId;

    @ApiModelProperty(value = "설문 이벤트 보기 PK")
    private Long evEventSurveyItemId;

    @ApiModelProperty(value = "직접입력 답안")
    private String otherComment;

}
