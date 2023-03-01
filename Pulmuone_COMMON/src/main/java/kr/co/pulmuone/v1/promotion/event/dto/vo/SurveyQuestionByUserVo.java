package kr.co.pulmuone.v1.promotion.event.dto.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class SurveyQuestionByUserVo {

    @ApiModelProperty(value = "설문 문항 PK")
    private Long evEventSurveyQuestionId;

    @ApiModelProperty(value = "설문 문항")
    private String title;

    @ApiModelProperty(value = "설문 유형")
    private String eventSurveyType;

    @ApiModelProperty(value = "설문 문항 보기")
    private List<SurveyItemByUserVo> item;

}
