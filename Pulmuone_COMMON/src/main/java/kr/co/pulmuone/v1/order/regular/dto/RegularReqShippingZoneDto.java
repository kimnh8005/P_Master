package kr.co.pulmuone.v1.order.regular.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.aop.service.*;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestPageDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <PRE>
 * Forbiz Korea
 * 정기배송 주문 신청 배송방법 Dto
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2021. 02. 08.	김명진		최초작성
 * =======================================================================
 * </PRE>
 */
@Getter
@Setter
@ToString
@ApiModel(description = "정기배송 주문 신청 배송방법 Dto")
public class RegularReqShippingZoneDto extends BaseRequestPageDto {

	@ApiModelProperty(value = "정기배송주문신청배송지 PK")
	private long odRegularReqShippingZoneId;

	@ApiModelProperty(value = "정기배송주문신청 PK")
	private long odRegularReqId;

	@ApiModelProperty(value = "회원 PK")
	private long urUserId;

	@ApiModelProperty(value = "배송타입")
	private String deliveryType;

	@ApiModelProperty(value = "수령인명")
	@UserMaskingUserName
	private String recvNm;

	@ApiModelProperty(value = "수령인핸드폰")
	@UserMaskingMobile
	private String recvHp;

	@ApiModelProperty(value = "수령인연락처")
	@UserMaskingTelePhone
	private String recvTel;

	@ApiModelProperty(value = "수령인이메일")
	@UserMaskingEmail
	private String recvMail;

	@ApiModelProperty(value = "수령인우편번호")
	private String recvZipCd;

	@ApiModelProperty(value = "수령인주소1")
	@UserMaskingAddress
	private String recvAddr1;

	@ApiModelProperty(value = "수령인주소2")
	@UserMaskingAddressDetail
	private String recvAddr2;

	@ApiModelProperty(value = "빌딩번호")
	private String recvBldNo;

	@ApiModelProperty(value = "배송요청사항")
	private String deliveryMsg;

	@ApiModelProperty(value = "배송요청사항 공통코드")
	private String deliveryMsgCd;

	@ApiModelProperty(value = "배송요청사항 공통코드명")
	private String deliveryMsgCdNm;

	@ApiModelProperty(value = "배송출입 기타/직접입력 메세지")
	private String doorEtc;

	@ApiModelProperty(value = "출입정보타입 공통코드")
	private String doorMsgCd;

	@ApiModelProperty(value = "출입정보타입 공통코드명")
	private String doorMsgCdNm;

	@ApiModelProperty(value = "배송출입현관비밀번호")
	private String doorMsg;

	@ApiModelProperty(value = "등록일")
	private String createDt;
}
