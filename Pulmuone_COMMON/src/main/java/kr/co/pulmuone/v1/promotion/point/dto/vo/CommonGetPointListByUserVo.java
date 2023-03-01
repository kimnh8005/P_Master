package kr.co.pulmuone.v1.promotion.point.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "유저 적립금 정보 List Result")
public class CommonGetPointListByUserVo {

    @ApiModelProperty(value = "적립구분")
    private String paymentTypeName;

    @ApiModelProperty(value = "발생일자")
    private String createDate;

    @ApiModelProperty(value = "적립내역")
    private String pointTypeName;

    @ApiModelProperty(value = "유효기간")
    private String expirationDate;

    @ApiModelProperty(value = "금액")
    private String amount;

    @ApiModelProperty(value = "메세지")
    private String comment;

    @ApiModelProperty(value = "적립금 처리 유형")
    private String pointProcessType;

    @ApiModelProperty(value = "적립금 지급명")
    private String pointNm;
}
