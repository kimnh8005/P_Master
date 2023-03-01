package kr.co.pulmuone.v1.order.create.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestPageDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;


/**
 * <PRE>
 * Forbiz Korea
 * 주문클레임 신용카드 비인증 결제 Request Dto
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2021. 04. 23.            김명진         최초작성
 * =======================================================================
 * </PRE>
 */
@Getter
@Setter
@ToString
@ApiModel(description = "주문클레임 신용카드 비인증 결제  Dto")
public class OrderClaimCardPayRequestDto {

	@ApiModelProperty(value = "주문번호")
	private String odid;

	@ApiModelProperty(value = "주문상세번호 목록")
	private List<Long> odOrderDetlIds;

	@ApiModelProperty(value = "결제금액")
	private int orderPrice;

	@ApiModelProperty(value = "카드번호")
	private String cardNo;

	@ApiModelProperty(value = "카드유효월")
	private String cardNumYy;

	@ApiModelProperty(value = "카드유효년도")
	private String cardNumMm;

	@ApiModelProperty(value = "부가정보구분")
	private String addInfoSel;

	@ApiModelProperty(value = "부가정보값")
	private String addInfoVal;

	@ApiModelProperty(value = "비밀번호")
	private String cardPass;

	@ApiModelProperty(value = "할부기간")
	private String planPeriod;

	@ApiModelProperty(value = "로그인ID")
	private String loginId;

	@ApiModelProperty(value = "구매자명")
	private String buyerNm;

	@ApiModelProperty(value = "구매자메일")
	private String buyerMail;

	@ApiModelProperty(value = "구매자연락처")
	private String buyerHp;

	@ApiModelProperty(value = "상품명")
	private String goodsNm;

	@ApiModelProperty(value = "추가결제요청정보PK")
	private long odAddPaymentReqInfoId;
}
