package kr.co.pulmuone.v1.order.claim.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


/**
 * <PRE>
 * Forbiz Korea
 * 주문 클레임 받는 배송지 조회결과 Dto
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2021. 01. 20.   강상국         최초작성
 * =======================================================================
 * </PRE>
 */
@Getter
@Setter
@ToString
@ApiModel(description = "주문 클레임 받는 배송지 조회결과 Dto")
public class OrderClaimShippingInfoDto {

	@ApiModelProperty(value = "주문 클레임 배송지 PK")
	private long odClaimShippingZoneId;

	@ApiModelProperty(value = "주주문클레임 PK")
	private long odClaimId;

	@ApiModelProperty(value = "수령인명")
	private String recvNm;

	@ApiModelProperty(value = "수령인 핸드폰")
	private String recvHp;

	@ApiModelProperty(value = "수령인 현락처")
	private String recvTel;

	@ApiModelProperty(value = "수령인 우편번호")
	private String recvZipCd;

	@ApiModelProperty(value = "수령인 주소1")
	private String recvAddr1;

	@ApiModelProperty(value = "수령인 주소2")
	private String recvAddr2;

	@ApiModelProperty(value = "건물번호")
	private String recvBldNo;

	@ApiModelProperty(value = "배송요청사항")
	private String deliveryMsg;

	@ApiModelProperty(value = "출입정보타입 공통코드(DOOR_MSG_CD)")
	private String doorMsgCd;

	@ApiModelProperty(value = "배송출입 현관 비밀번호")
	private String doorMsg;

	@ApiModelProperty(value = "주문 클레임 상세 PK")
	private long odClaimDetlId;

	@ApiModelProperty(value = "출고처 PK : UR_WAREHOUSE.UR_WAREHOUSE_ID")
	private long urWarehouseId;

	@ApiModelProperty(value = "가맹점여부")
	private String storeYn;

}
