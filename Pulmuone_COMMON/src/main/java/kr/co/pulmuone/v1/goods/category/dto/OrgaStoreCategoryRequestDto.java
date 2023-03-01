package kr.co.pulmuone.v1.goods.category.dto;

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
public class OrgaStoreCategoryRequestDto {

    @ApiModelProperty(value = "몰 구분")
    private String mallDiv;

    @ApiModelProperty(value = "카테고리 리스트")
    private List<Long> dpCtgryIdList;

    @ApiModelProperty(value = "점포 PK")
    private String urStoreId;

    @ApiModelProperty(value = "전시브랜드 리스트")
    private List<String> dpBrandIdList;

}
