package kr.co.pulmuone.v1.order.create.dto;

import java.util.List;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestDto;
import kr.co.pulmuone.v1.order.order.dto.vo.OrderShippingPriceVo;
import kr.co.pulmuone.v1.order.order.dto.vo.OrderShippingZoneVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <PRE>
 * Forbiz Korea
 * 주문 상세내역 복사 Request Dto
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :	작성일                		:	작성자      :	작성내역
 * -----------------------------------------------------------------------
 *  1.0		2021. 03. 03.		이규한	최초작성
 * =======================================================================
 * </PRE>
 */

@Getter
@Setter
@ToString
@ApiModel(description = "주문 상세내역 복사 Request Dto")
public class OrderCopySaveRequestDto extends BaseRequestDto {

	@ApiModelProperty(value = "주문PK")
    private Long odOrderId;

	@ApiModelProperty(value = "주문번호")
	private String odid;

	@ApiModelProperty(value = "주문 연동 방법")
    private String orderIfType;

	@ApiModelProperty(value = "결제 정보")
    private String paymentType;

	@ApiModelProperty(value = "주문 복사 배송지 목록")
	private List<OrderShippingZoneVo> orderShippingList;

	@ApiModelProperty(value = "주문 복사 배송비 목록")
	private List<OrderShippingPriceVo> orderShippingPriceList;

	@ApiModelProperty(value = "주문복사 상품정보 리스트")
	private List<OrderCopyDto> orderGoodsList;

	@ApiModelProperty(value = "결제정보")
	private OrderPaymentDto paymentInfo;

	@ApiModelProperty(value = "비인증 카드 결제 정보")
	private OrderCardPayRequestDto cardInfo;

	@ApiModelProperty(value = "공급처")
	private String sellersGroupCd;

	@ApiModelProperty(value = "대표상품명")
	private String repGoodsNm;

	@ApiModelProperty(value = "대표상품코드")
	private long repIlGoodsId;

	@ApiModelProperty(value = "환불 계좌 은행코드")
	private String bankCode;

	@ApiModelProperty(value = "환불 계좌 계좌번호")
	private String accountNumber;

	@ApiModelProperty(value = "환불 계좌 예금주")
	private String holderName;

	@ApiModelProperty(value = "배송비무료 여부")
	private String freeShippingPriceYn = "N";
}