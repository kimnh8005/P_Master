package kr.co.pulmuone.v1.store.shop.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "매장 상세 정보 Result")
public class ShopVo {

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

    @ApiModelProperty(value = "영업시작시간")
    private String openTime;

    @ApiModelProperty(value = "영업종료시간")
    private String closeTime;

    @ApiModelProperty(value = "매장 위치 위도, 경도")
    private String locate;

    @ApiModelProperty(value = "매장 이미지")
    private String introImage;

    @ApiModelProperty(value = "매장 타입")
    private String storeOrderSet;

    @ApiModelProperty(value = "소개")
    private String introText;

}
