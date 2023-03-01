package kr.co.pulmuone.v1.order.claim.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


/**
 * <PRE>
 * Forbiz Korea
 * 주문 클레임 클레임정보 사유 조회 결과 Dto
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2021. 01. 21.   강상국         최초작성
 * =======================================================================
 * </PRE>
 */

@Getter
@Setter
@ToString
@ApiModel(description = "주문마스터 조회 결과 Dto")
public class OrderMasterInfoDto {

	@ApiModelProperty(value = "주문번호 16자리 년,월,일 + 난수Ex 2020년 01월 01일 >> 2020010101234567")
	private String odid;

	@ApiModelProperty(value = "등록일=주문일자")
	private String createDt;

	@ApiModelProperty(value = "회원 ID : UR_USER.UR_USER_ID")
	private long urUserId;

	@ApiModelProperty(value = "임직원사번 : UR_EMPLOYEE.UR_EMPLOYEE_CD")
	private String urEmployeeCd;

	@ApiModelProperty(value = "비회원 CI")
	private String guestCi;

	@ApiModelProperty(value = "주문자 명")
	private String buyerNm;

	@ApiModelProperty(value = "주문자 핸드폰")
	private String buyerHp;

	@ApiModelProperty(value = "주문자 이메일")
	private String buyerMail;

    @ApiModelProperty(value = "정기배송 주문 여부")
    private String regularYn;
}
