package kr.co.pulmuone.v1.order.create.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestDto;
import kr.co.pulmuone.v1.order.claim.dto.OrderAccountDto;
import kr.co.pulmuone.v1.order.order.dto.OrderBuyerDto;
import kr.co.pulmuone.v1.order.order.dto.OrderDetailGoodsListDto;
import kr.co.pulmuone.v1.order.order.dto.OrderDetailPayDetlListDto;
import kr.co.pulmuone.v1.order.order.dto.OrderDetailPayListDto;
import kr.co.pulmuone.v1.order.order.dto.vo.OrderDetlShippingZoneVo;
import kr.co.pulmuone.v1.user.buyer.dto.vo.CommonGetRefundBankResultVo;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

/**
 * <PRE>
 * Forbiz Korea
 * 주문복사 상세내역 Response Dto
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :	작성일                		:	작성자      :	작성내역
 * -----------------------------------------------------------------------
 *  1.0		2021. 02. 24.		이규한	최초작성
 * =======================================================================
 * </PRE>
 */

@Builder
@Getter
@ToString
@ApiModel(description = "주문복사 상세내역 Response Dto")
public class OrderCopyDetailInfoResponseDto extends BaseRequestDto {

	@ApiModelProperty(value = "주문 PK")
	private Long odOrderId;

	@ApiModelProperty(value = "주문자 유형 : 공통코드(BUYER_TYPE)")
	private String buyerTypeCd;

	@ApiModelProperty(value = "주문상세 주문자정보")
	private OrderBuyerDto orderBuyerDto;

	@ApiModelProperty(value = "주문상세 상품정보 리스트")
	private List<OrderDetailGoodsListDto> orderDetailGoodsList;

	@ApiModelProperty(value = "주문상세 배송정보 리스트")
	private List<OrderDetlShippingZoneVo> orderDetailShippingZoneList;

	@ApiModelProperty(value = "주문상세 결제상세정보 리스트")
	private List<OrderDetailPayDetlListDto> orderDetailPayDetlList;

	@ApiModelProperty(value = "주문상세 결제정보 리스트")
	private List<OrderDetailPayListDto> orderDetailPayList;

	@ApiModelProperty(value = "임직원 지원금 사용여부")
	private int orderEmployeeDiscountCnt;

	@ApiModelProperty(value = "매장 PK")
	private String urStoreId;

	@ApiModelProperty(value = "환불계좌정보")
	private CommonGetRefundBankResultVo refundBankResult;
}