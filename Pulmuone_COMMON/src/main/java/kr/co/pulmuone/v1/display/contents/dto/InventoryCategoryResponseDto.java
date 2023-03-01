package kr.co.pulmuone.v1.display.contents.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@Builder
@ToString
public class InventoryCategoryResponseDto {

    @ApiModelProperty(value = "표준 카테고리 PK")
    private String ilCtgryId;

    @ApiModelProperty(value = "level1 정보")
    private List<ContentsDetailBannerResponseDto> level1;

}
