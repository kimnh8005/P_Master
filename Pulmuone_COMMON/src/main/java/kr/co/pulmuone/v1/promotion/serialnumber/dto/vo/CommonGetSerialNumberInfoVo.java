package kr.co.pulmuone.v1.promotion.serialnumber.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "시리얼넘버 정보 조회 Result")
public class CommonGetSerialNumberInfoVo {

    @ApiModelProperty(value = "쿠폰 시리얼 번호")
    private Long couponSerial;

    @ApiModelProperty(value = "포인트 시리얼 번호")
    private Long pointSerial;

    @ApiModelProperty(value = "사용한 이용권")
    private int useSerial;

    @ApiModelProperty(value = "쿠폰 고정 시리얼 번호")
    private Long couponFixSerial;

    @ApiModelProperty(value = "포인트 고정 시리얼 번호")
    private Long pointFixSerial;

    @ApiModelProperty(value = "개별난수번호 PK")
    private Long pmSerialNumberId;
}
