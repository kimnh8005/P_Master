package kr.co.pulmuone.v1.batch.order.dto.search;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <PRE>
 * Forbiz Korea
 * 용인물류 반품 매출주문 List 검색 RequestDto
 * </PRE>
 */

@Getter
@Setter
@ToString
@Builder
@ApiModel(description = "ReturnSalesOrderSearchRequestDto")
public class ReturnSalesOrderSearchRequestDto {

    @ApiModelProperty(value = "공급처 PK")
    private String urSupplierId;

    @ApiModelProperty(value = "출고처")
    private String urWarehouseId;

    @ApiModelProperty(value = "클레임주문상태")
    private String[] orderStatusCd;

    @ApiModelProperty(value = "환불상태")
    private String refundStatusCd;
    
    @ApiModelProperty(value = "출고처그룹아이디")
    private String urWarehouseGroupId;

}
