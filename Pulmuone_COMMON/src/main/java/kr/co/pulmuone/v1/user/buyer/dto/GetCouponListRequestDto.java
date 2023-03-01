package kr.co.pulmuone.v1.user.buyer.dto;

import kr.co.pulmuone.v1.comm.base.dto.BaseRequestPageDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "GetCouponListRequestDto")
public class GetCouponListRequestDto extends BaseRequestPageDto {

  @ApiModelProperty(value = "사용기간시작일")
  private String useFromDate;

  @ApiModelProperty(value = "사용기간종료일")
  private String useToDate;

  @ApiModelProperty(value = "발행일시작일")
  private String issueFromDate;

  @ApiModelProperty(value = "발행일종료일")
  private String issueToDate;

  @ApiModelProperty(value = "사용여부")
  private String couponUseYn;

  @ApiModelProperty(value = "")
  private String urUserId;

  @ApiModelProperty(value = "검색구분")
  private String couponCondiType;

  @ApiModelProperty(value = "검색 값")
  private String couponCondiValue;

}
