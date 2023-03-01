package kr.co.pulmuone.v1.order.claim.dto;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.goods.goods.dto.ArrivalScheduledDateDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <PRE>
 * Forbiz Korea
 * 출고정보 조회 결과 Dto
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2020. 12. 15.    강상국         최초작성
 * =======================================================================
 * </PRE>
 */
@Builder
@Getter
@Setter
@ToString
@ApiModel(description = "출고정보 조회 결과 Dto")
public class DeliveryInfoDto {

    @ApiModelProperty(value = "출고처 PK : UR_WAREHOUSE.UR_WAREHOUSE_ID")
    private long urWarehouseId;

	@ApiModelProperty(value = "상품코드")
	private long ilGoodsId;

	@ApiModelProperty(value = "주문상세 PK")
	private long odOrderDetlId;

	List<ArrivalScheduledDateDto> arrivalScheduleList;
}
