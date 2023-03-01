package kr.co.pulmuone.v1.order.order.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.aop.service.UserMaskingAccountNumber;
import kr.co.pulmuone.v1.comm.aop.service.UserMaskingUserName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "OrderCSRefundListDto")
public class OrderCSRefundTotalInfoDto {

    @ApiModelProperty(value = "총환불금액합계")
    private long totalRefundPrice;

    @ApiModelProperty(value = "총건수")
    private long total;
}
