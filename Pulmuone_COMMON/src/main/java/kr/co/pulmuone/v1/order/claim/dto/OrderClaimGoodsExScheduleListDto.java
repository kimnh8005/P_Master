package kr.co.pulmuone.v1.order.claim.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.List;


/**
 * <PRE>
 * Forbiz Korea
 * 주문 클레임 상품 재배송 도착 예정일 목록 Dto
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2021. 05. 03.            김명진         최초작성
 * =======================================================================
 * </PRE>
 */

@Getter
@Setter
@ToString
@ApiModel(description = "주문 클레임 상품 재배송 도착 예정일 목록 Dto")
public class OrderClaimGoodsExScheduleListDto {

	@ApiModelProperty(value = "주문상세 도착예정일 목록")
	private List<OrderClaimGoodsExScheduleInfoDto> arrivalDtList;
}
