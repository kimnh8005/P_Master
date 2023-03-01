package kr.co.pulmuone.v1.promotion.exhibit.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
public class SelectExhibitResponseDto {

    @ApiModelProperty(value = "기획전 PK")
    private Long evExhibitId;

    @ApiModelProperty(value = "기획명")
    private String title;

    @ApiModelProperty(value = "골라담기 균일가")
    private int selectPrice;

    @ApiModelProperty(value = "골라담기 활성화 여부")
    private Boolean isActive;

}
