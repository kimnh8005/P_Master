package kr.co.pulmuone.v1.promotion.serialnumber.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.enums.SerialEnums;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
@ApiModel(description = "시리얼넘버 타입 조회 ResponseDto")
public class SerialNumberTypeResponseDto {

    @ApiModelProperty(value = "개별난수번호PK")
    private Long pmSerialNumberId;

    @ApiModelProperty(value = "시리얼넘버 타입 구분")
    private SerialEnums.SerialNumberAddType serialType;
    
    @ApiModelProperty(value = "포인트 or 쿠폰 ID")
    private Long pointOrCouponId;

}
