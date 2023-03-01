package kr.co.pulmuone.v1.user.buyer.dto.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class GetCouponListResultVo {

  @ApiModelProperty(value = "rowNumber")
  private int    rowNumber;

  @ApiModelProperty(value = "쿠폰 PK")
  private Long pmCouponId;

  @ApiModelProperty(value = "발급번호")
  private Long pmCouponIssueId;

  @ApiModelProperty(value = "쿠폰명")
  private String displayCouponName;

  @ApiModelProperty(value = "정률:퍼센트 / 정액:금액")
  private String discountValueName;

  @ApiModelProperty(value = "최소결제금액")
  private String minPaymentAmount;

  @ApiModelProperty(value = "사용기간")
  private String validityDate;

  @ApiModelProperty(value = "쿠폰사용유무")
  private String couponUseYn;

  @ApiModelProperty(value = "발행일")
  private String issueStartDate;

  @ApiModelProperty(value = "사용가능 쿠폰 여부")
  private String availableCouponYn;

  @ApiModelProperty(value = "쿠폰종류")
  private String couponTpNm;

}
