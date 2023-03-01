package kr.co.pulmuone.v1.order.status.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
@ApiModel(description = " 취소요청 일괄 완료 처리 RequestDto")
public class OrderClaimStatusRequestDto {

    @ApiModelProperty(value = "클레임정보 목록")
    private List<OrderClaimInfoListDto> odClaimInfoList;
}
