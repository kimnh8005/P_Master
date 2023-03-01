package kr.co.pulmuone.v1.promotion.point.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "적립금 사용내역 등록 RequestDto")
public class CommonAddPointUsedRequestDto {

    @ApiModelProperty(value = "발급타입")
    private String paymentType;

    @ApiModelProperty(value = "유저 PK")
    private Long urUserId;

    @ApiModelProperty(value = "발급 값")
    private String issueValue;

    @ApiModelProperty(value = "적립금 PK")
    private Long pmPointId;

    @ApiModelProperty(value = "적립금 사용내역 PK")
    private Long pmPointUsedId;
}
