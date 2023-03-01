package kr.co.pulmuone.v1.base.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "카테고리 Request")
public class CategoryRequestDto extends BaseRequestDto {

    @ApiModelProperty(value = "카테고리레벨")
    private String depth;

    @ApiModelProperty(value = "상위 카테고리 ID")
    private String categoryId;

    @ApiModelProperty(value = "몰 구분")
    private String mallDiv;
}
