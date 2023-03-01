package kr.co.pulmuone.v1.batch.order.etc.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class TrackingNumberOrderInfoDto {
    @ApiModelProperty(value = "주문상세 PK")
    private String odOrderDetlId;

    @ApiModelProperty(value = "주문클레임 PK")
    private long odClaimId;

    @ApiModelProperty(value = "주문클레임상세 PK")
    private long odClaimDetlId;

    @ApiModelProperty(value = "주문건수")
    private int orderCnt;

    @ApiModelProperty(value = "취소완료건수")
    private int cancelCompleteCnt;
    
    @ApiModelProperty(value = "주문 상세 상태값")
    private String orderStatusCd;
}
