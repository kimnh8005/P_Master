package kr.co.pulmuone.v1.promotion.exhibit.dto;

import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.shopping.cart.dto.AddCartInfoAdditionalGoodsRequestDto;
import kr.co.pulmuone.v1.shopping.cart.dto.SpCartPickGoodsRequestDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class SelectOrderRequestDto {

    @ApiModelProperty(value = "상품 정보")
    private List<SpCartPickGoodsRequestDto> pickGoodsList;

    @ApiModelProperty(value = "추가 구성 상품 리스트")
    private List<AddCartInfoAdditionalGoodsRequestDto> additionalGoodsList;

    @ApiModelProperty(value = "바로구매 여부")
    private String buyNowYn;

    @ApiModelProperty(value = "기획전 PK")
    private Long evExhibitId;

    @ApiModelProperty(value = "사용자 환경 정보 PK", hidden = true)
    private String urPcidCd;

    @ApiModelProperty(value = "디바이스 유형", hidden = true)
    private String deviceType;

    @ApiModelProperty(value = "유저 상태", hidden = true)
    private String userStatus;

    @ApiModelProperty(value = "유저 등급 PK", hidden = true)
    private Long urGroupId;

    @ApiModelProperty(value = "유저 PK", hidden = true)
    private Long urUserId;

    @ApiModelProperty(value = "외부광고코드PK", hidden = true)
	private String pmAdExternalCd;

    @ApiModelProperty(value = "내부광고코드-페이지코드")
	private String pmAdInternalPageCd;

    @ApiModelProperty(value = "내부광고코드-영역코드")
	private String pmAdInternalContentCd;
}
