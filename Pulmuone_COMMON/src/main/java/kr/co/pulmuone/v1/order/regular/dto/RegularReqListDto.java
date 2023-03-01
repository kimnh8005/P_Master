package kr.co.pulmuone.v1.order.regular.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.aop.service.UserMaskingEmail;
import kr.co.pulmuone.v1.comm.aop.service.UserMaskingLoginId;
import kr.co.pulmuone.v1.comm.aop.service.UserMaskingMobile;
import kr.co.pulmuone.v1.comm.aop.service.UserMaskingUserName;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestPageDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

/**
 * <PRE>
 * Forbiz Korea
 * 정기배송 주문 신청 리스트 Dto
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2021. 02. 04.	김명진		최초작성
 * =======================================================================
 * </PRE>
 */
@Getter
@Setter
@ToString
@ApiModel(description = "정기배송 주문 신청 리스트 Dto")
public class RegularReqListDto extends BaseRequestPageDto {

	@ApiModelProperty(value = "정기배송주문신청PK")
	private long odRegularReqId;

	@ApiModelProperty(value = "정기배송주문결과PK")
	private long odRegularResultId;

	@ApiModelProperty(value = "정기배송신청일자")
	private String createDt;

	@ApiModelProperty(value = "신청번호")
	private String reqId;

	@ApiModelProperty(value = "기간연장횟수")
	private int termExtensionCnt;

	@ApiModelProperty(value = "회차")
	private int reqRound;

	@ApiModelProperty(value = "총회차")
	private int totCnt;

	@ApiModelProperty(value = "신청기간코드")
	private String goodsCycleTermTp;

	@ApiModelProperty(value = "신청기간코드명")
	private String goodsCycleTermTpNm;

	@ApiModelProperty(value = "신청기간")
	private int goodsCycleTerm;

	@ApiModelProperty(value = "배송주기코드")
	private String goodsCycleTp;

	@ApiModelProperty(value = "배송주기코드명")
	private String goodsCycleTpNm;

	@ApiModelProperty(value = "배송요일코드")
	private String weekCd;

	@ApiModelProperty(value = "배송요일코드명")
	private String weekCdNm;

	@ApiModelProperty(value = "정기배송주문상태코드")
	private String regularStatusCd;

	@ApiModelProperty(value = "정기배송주문상태코드명")
	private String regularStatusCdNm;

	@ApiModelProperty(value = "주문자명")
	@UserMaskingUserName
	private String buyerNm;

	@ApiModelProperty(value = "주문자핸드폰")
	@UserMaskingMobile
	private String buyerHp;

	@ApiModelProperty(value = "주문자이메일")
	@UserMaskingEmail
	private String buyerMail;

	@ApiModelProperty(value = "주문자ID")
	@UserMaskingLoginId
	private String loginId;

	@ApiModelProperty(value = "회원PK")
	private long urUserId;

	@ApiModelProperty(value = "수령인명")
	@UserMaskingUserName
	private String recvNm;

	@ApiModelProperty(value = "상품명")
	private String goodsNm;

	@ApiModelProperty(value = "상품수")
	private int goodsCnt;

	@ApiModelProperty(value = "판매금액")
	private int salePrice;

	@ApiModelProperty(value = "할인금액")
	private int discountPrice;

	@ApiModelProperty(value = "결제금액")
	private int paidPrice;

	@ApiModelProperty(value = "유형")
	private String agentTypeCdNm;

	@ApiModelProperty(value = "첫배송도착예정일자")
	private LocalDate deliveryDt;

	@ApiModelProperty(value = "회차정보생성여부")
	private String createRoundYn;

	@ApiModelProperty(value = "임직원여부")
	private String erpEmployeeYn;

	@ApiModelProperty(value = "회원등급명")
	private String urGroupNm;

	@ApiModelProperty(value = "회차완료여부")
	private String reqRoundYn;

	@ApiModelProperty(value = "도착예정일자")
	private LocalDate arriveDt;

	@ApiModelProperty(value = "주문PK")
	private long odOrderId;
}
