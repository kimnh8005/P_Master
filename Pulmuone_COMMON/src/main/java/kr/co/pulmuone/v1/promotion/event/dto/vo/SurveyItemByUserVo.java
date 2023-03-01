package kr.co.pulmuone.v1.promotion.event.dto.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class SurveyItemByUserVo {

    @ApiModelProperty(value = "설문 항목 아이템 PK")
    private Long evEventSurveyItemId;

    @ApiModelProperty(value = "설문 보기")
    private String item;

    @ApiModelProperty(value = "직접 입력 여부")
    private String directInputYn;

    @ApiModelProperty(value = "이미지 경로")
    private String imagePath;

    @ApiModelProperty(value = "이미지 원본 명")
    private String imageOriginalName;

}
