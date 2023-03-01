package kr.co.pulmuone.v1.order.claim.dto;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.user.buyer.dto.vo.CodeInfoVo;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


/**
 * <PRE>
 * Forbiz Korea
 * 주문 클레임 정보 조회 Response Dto
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2020. 12. 15.   강상국         최초작성
 * =======================================================================
 * </PRE>
 */

@Builder
@Getter
@Setter
@ToString
@ApiModel(description = "주문 클레임 정보 조회 결과 Dto")
public class OrderClaimViewDeliveryDto {

	@ApiModelProperty(value = "출고처PK")
	private long urWarehouseId;

	@ApiModelProperty(value = "출고처명")
	private String urWarehouseNm;

	@ApiModelProperty(value = "출고정보 ")
	private DeliveryInfoDto deliveryInfoList;

	@ApiModelProperty(value = "상품배송타입 ")
	private String goodsDeliveryType;

	@ApiModelProperty(value = "주문상태 배송유형 공통코드: ORDER_STATUS_DELI_TP")
	private String orderStatusDeliTp;
}
