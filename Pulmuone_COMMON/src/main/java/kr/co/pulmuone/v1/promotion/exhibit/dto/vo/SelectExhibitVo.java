package kr.co.pulmuone.v1.promotion.exhibit.dto.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class SelectExhibitVo {

    @ApiModelProperty(value = "기획전 PK")
    private Long evExhibitId;

    @ApiModelProperty(value = "제목")
    private String title;

    @ApiModelProperty(value = "사용여부")
    private String useYn;

    @ApiModelProperty(value = "삭제여부")
    private String delYn;

    @ApiModelProperty(value = "상시진행여부")
    private String alwaysYn;

    @ApiModelProperty(value = "시작일자")
    private String startDate;

    @ApiModelProperty(value = "종료일자")
    private String endDate;

    @ApiModelProperty(value = "결재상태")
    private String exhibitStatus;

    @ApiModelProperty(value = "골라담기 균일가")
    private int selectPrice;

}
