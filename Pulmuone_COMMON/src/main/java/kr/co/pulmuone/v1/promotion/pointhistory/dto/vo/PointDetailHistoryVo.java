package kr.co.pulmuone.v1.promotion.pointhistory.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = " 적립금 상세내역 정보 List Result")
public class PointDetailHistoryVo {

	@ApiModelProperty(value = "적립금 상세사용 ID")
    private String pmPointUsedDetlId;

	@ApiModelProperty(value = "적립금 사용 ID")
    private String pmPointUsedId;

    @ApiModelProperty(value = "회원명")
    private String createDate;

    @ApiModelProperty(value = "구분")
    private String paymentType;

    @ApiModelProperty(value = "구분명")
    private String paymentTypeName;

    @ApiModelProperty(value = "상세")
    private String pointDetailTypeName;

    @ApiModelProperty(value = "내역")
    private String provisionAmount;

    @ApiModelProperty(value = "사용금액")
    private String usePoint;

    @ApiModelProperty(value = "유효기간")
    private String expirationDate;

    @ApiModelProperty(value = "비고")
    private String comment;

}
