package kr.co.pulmuone.v1.policy.fee.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@Getter
@Setter
@ToString
@ApiModel(description = "OmBasicFeeDto")
public class OmBasicFeeDto {

    @ApiModelProperty(value = "기본수수료 pk")
    private long omBasicFeeId;

    @ApiModelProperty(value = "판매처 그룹코드")
    private String sellersGroupCd;

    @ApiModelProperty(value = "판매처 PK")
    private long omSellersId;

    @ApiModelProperty(value = "공급처 PK")
    private long urSupplierId;

    @ApiModelProperty(value = "정산방식")
    private String calcType;

    @ApiModelProperty(value = "수수료")
    private int fee;

    @ApiModelProperty(value = "시작일자")
    private String startDt;

    @ApiModelProperty(value = "공급처 코드")
    private String supplierCd;

}
