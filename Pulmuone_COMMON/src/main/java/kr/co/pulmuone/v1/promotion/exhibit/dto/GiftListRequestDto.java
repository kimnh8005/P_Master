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
public class GiftListRequestDto {

    @ApiModelProperty(value = "상품 PK")
    private Long ilGoodsId;

    @ApiModelProperty(value = "표준 브랜드 PK")
    private Long urBrandId;

    @ApiModelProperty(value = "전시 브랜드 PK")
    private Long dpBrandId;

    @ApiModelProperty(value = "증정 유형")
    private String giftType;

    @ApiModelProperty(value = "디바이스 유형", hidden = true)
    private String deviceType;

    @ApiModelProperty(value = "유저 상태", hidden = true)
    private String userStatus;

    @ApiModelProperty(value = "유저 등급 PK", hidden = true)
    private Long urGroupId;

}
