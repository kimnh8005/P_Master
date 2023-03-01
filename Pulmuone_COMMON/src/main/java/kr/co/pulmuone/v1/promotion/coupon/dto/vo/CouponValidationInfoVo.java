package kr.co.pulmuone.v1.promotion.coupon.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "쿠폰 등록 검증 정보 조회 Result")
public class CouponValidationInfoVo {

    @ApiModelProperty(value = "쿠폰 발급 상태")
    private String couponMasterStat;

    @ApiModelProperty(value = "쿠폰 발급방법 유형")
    private String issueType;

    @ApiModelProperty(value = "쿠폰 발급방법 상세 유형")
    private String issueDetailType;

    @ApiModelProperty(value = "발급수량")
    private int issueQty;

    @ApiModelProperty(value = "발급수량제한(1인당 지급제한 건수)")
    private int issueQtyLimit;

    @ApiModelProperty(value = "발급기간 시작일")
    private String issueStartDate;

    @ApiModelProperty(value = "발급기간 종료일")
    private String issueEndDate;

    @ApiModelProperty(value = "쿠폰 발급 갯수")
    private int issueCnt;

    @ApiModelProperty(value = "쿠폰 유저 발급 갯수")
    private int userIssueCnt;

    @ApiModelProperty(value = "쿠폰 PK")
    private Long pmCouponId;

    @ApiModelProperty(value = "유효기간 설정타입")
    private String validityType;

    @ApiModelProperty(value = "유효기간 시작일")
    private String validityStartDate;

    @ApiModelProperty(value = "유효기간 종료일")
    private String validityEndDate;

    @ApiModelProperty(value = "유효기간 유효일")
    private int validityDay;

}
