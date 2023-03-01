package kr.co.pulmuone.v1.promotion.exhibit.dto.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ExhibitListByUserVo {

    @ApiModelProperty(value = "기획전 PK")
    private String evExhibitId;

    @ApiModelProperty(value = "기획전 유형")
    private String exhibitType;

    @ApiModelProperty(value = "몰인몰 유형")
    private String mallDiv;

    @ApiModelProperty(value = "배너 이미지 경로")
    private String bannerImagePath;

    @ApiModelProperty(value = "배너 이미지 원본 명")
    private String bannerImageOriginalName;

    @ApiModelProperty(value = "이벤트 제목")
    private String title;

    @ApiModelProperty(value = "상시 유무")
    private String alwaysYn;

    @ApiModelProperty(value = "시작일자")
    private String startDate;

    @ApiModelProperty(value = "종료일자")
    private String endDate;

    @ApiModelProperty(value = "종료 여부")
    private String endYn;

}