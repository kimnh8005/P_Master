package kr.co.pulmuone.v1.policy.fee.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "OmBasicFeeHistListDto")
public class OmBasicFeeHistListDto {

    @ApiModelProperty(value = "기본수수료 히스토리 PK")
    private long omBasicFeeHistId;

    @ApiModelProperty(value = "정산방식")
    private String calcType;

    @ApiModelProperty(value = "수수료")
    private int fee;

    @ApiModelProperty(value = "시작일자")
    private String startDt;

}