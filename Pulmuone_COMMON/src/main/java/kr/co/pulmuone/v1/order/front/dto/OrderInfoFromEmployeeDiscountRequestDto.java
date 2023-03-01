package kr.co.pulmuone.v1.order.front.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
public class OrderInfoFromEmployeeDiscountRequestDto {

    @ApiModelProperty(value = "임직원 코드")
    private String urErpEmployeeCd;

    @ApiModelProperty(value = "임직원 할인 정보 코드")
    private Long psEmplDiscGrpId;

    @ApiModelProperty(value = "조회 시작일자")
    private String startDate;

    @ApiModelProperty(value = "조회 종료일자")
    private String endDate;
    
}
