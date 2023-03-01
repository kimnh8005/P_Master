package kr.co.pulmuone.v1.order.create.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.store.delivery.dto.ShippingAddressPossibleDeliveryInfoDto;
import kr.co.pulmuone.v1.user.buyer.dto.vo.CommonGetRefundBankResultVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <PRE>
 * Forbiz Korea
 *
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :	작성일                		:	작성자      :	작성내역
 * -----------------------------------------------------------------------
 *  1.0		2021. 04. 09.		이규한	최초작성
 * =======================================================================
 * </PRE>
 */

@Getter
@Setter
@ToString
@ApiModel(description = "주문 생성 주문자 정보조회 Response Dto")
public class OrderCreateBuyerInfoResponseDto {

	@ApiModelProperty(value = "배송지 코드")
	private String shippingAddressType;

	@ApiModelProperty(value = "배송지 코드명")
	private String shippingAddressTypeName;

	@ApiModelProperty(value = "배송지 수령인명")
	private String receiverName;

	@ApiModelProperty(value = "배송지 우편번호")
	private String receiverZipCode;

	@ApiModelProperty(value = "배송지 주소")
	private String receiverAddress1;

	@ApiModelProperty(value = "배송지 상세주소")
	private String receiverAddress2;

	@ApiModelProperty(value = "건물관리번호")
	private String buildingCode;

	@ApiModelProperty(value = "배송지 모바일")
	private String receiverMobile;

	@ApiModelProperty(value = "출입정보타입")
	private String accessInformationType;

	@ApiModelProperty(value = "출입정보 비밀번호(암호화)")
	private String accessInformationPassword;

	@ApiModelProperty(value = "배송 요청 사항")
	private String shippingComment;

	@ApiModelProperty(value = "배송 가능 정보")
	private ShippingAddressPossibleDeliveryInfoDto delivery;

	@ApiModelProperty(value = "배송지 PK")
	private Long shippingAddressId;

	@ApiModelProperty(value = "보유 적립금")
	private int availablePoint;

	@ApiModelProperty(value = "환불계좌정보")
	private CommonGetRefundBankResultVo refundBankResult;
}