package kr.co.pulmuone.v1.promotion.serialnumber.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "시리얼넘버 등록 ResponseDto")
public class AddSerialNumberResponseDto {

    @ApiModelProperty(value = "리캡챠 실패 카운트")
    private int recaptchaFailCnt;

    @ApiModelProperty(value = "적립금 - 부분 적립 금액")
    private int pointPartialDeposit;

    @ApiModelProperty(value = "이용권 유형")
    private String serialType;

}
