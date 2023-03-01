package kr.co.pulmuone.v1.order.status.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = " 취소요청 일괄 완료 처리 RequestDto")
public class OrderClaimInfoListDto {

    @ApiModelProperty(value = "주문PK")
    private long odOrderId;

    @ApiModelProperty(value = "주문상세PK")
    private long odOrderDetlId;

    @ApiModelProperty(value = "주문클레임PK")
    private long odClaimId;

    @ApiModelProperty(value = "주문클레임상세PK")
    private long odClaimDetlId;

    @ApiModelProperty(value = "주문번호")
    private String odid;

    @ApiModelProperty(value = "상품PK")
    private long ilGoodsId;

    @ApiModelProperty(value = "귀책구분")
    private String targetTp;

    @ApiModelProperty(value = "회수여부")
    private String returnsYn;

    @ApiModelProperty(value = "클레임수량")
    private int claimCnt;

    @ApiModelProperty(value = "주문수량")
    private int orderCnt;

    @ApiModelProperty(value = "취소수량")
    private int cancelCnt;

    @ApiModelProperty(value = "출고처PK")
    private long urWarehouseId;
}
