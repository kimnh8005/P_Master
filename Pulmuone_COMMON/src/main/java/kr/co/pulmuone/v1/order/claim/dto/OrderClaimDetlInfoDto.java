package kr.co.pulmuone.v1.order.claim.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

/**
 * <PRE>
 * Forbiz Korea
 * 주문클레임 상세 정보 DTO
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2021. 01. 20.	김명진		최초작성
 * =======================================================================
 * </PRE>
 */
@Builder
@Getter
@ToString
@ApiModel(description = "주문클레임 상세 정보 DTO")
public class OrderClaimDetlInfoDto {

    @ApiModelProperty(value = "주문 클레임 상세 PK")
    private long odClaimDetlId;

    @ApiModelProperty(value = "주문상세 PK")
    private long odOrderDetlId;

    @ApiModelProperty(value = "출고처 PK : UR_WAREHOUSE.UR_WAREHOUSE_ID")
    private long urWarehouseId;
}
