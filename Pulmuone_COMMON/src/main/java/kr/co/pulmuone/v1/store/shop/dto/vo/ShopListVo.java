package kr.co.pulmuone.v1.store.shop.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "매장리스트 정보 Result")
public class ShopListVo {

    @ApiModelProperty(value = "매장 PK")
    private String urStoreId;

    @ApiModelProperty(value = "매장 명")
    private String name;

    @ApiModelProperty(value = "매장 카테고리")
    private String storeCategory;

    @ApiModelProperty(value = "우편번호")
    private String zipCode;

    @ApiModelProperty(value = "주소1")
    private String address1;

    @ApiModelProperty(value = "주소2")
    private String address2;

    @ApiModelProperty(value = "전화번호")
    private String telephone;

}
