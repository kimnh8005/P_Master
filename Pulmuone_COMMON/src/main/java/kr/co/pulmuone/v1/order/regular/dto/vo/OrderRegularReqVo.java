package kr.co.pulmuone.v1.order.regular.dto.vo;

import java.time.LocalDate;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <PRE>
 * Forbiz Korea
 * 정기배송 주문 신청
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2021. 02. 07.	김명진 		최초작성
 * =======================================================================
 * </PRE>
 */

@Getter
@Setter
@ToString
@ApiModel(description = "정기배송 주문 신청 VO")
public class OrderRegularReqVo {

	@ApiModelProperty(value = "정기배송주문신청 PK")
	private long odRegularReqId;

	@ApiModelProperty(value = "주문신청번호")
	private String reqId;

	@ApiModelProperty(value = "회원그룹ID")
	private String urGroupId;

	@ApiModelProperty(value = "회원그룹명")
	private String urGroupNm;

	@ApiModelProperty(value = "회원ID")
	private long urUserId;

	@ApiModelProperty(value = "주문자명")
	private String buyerNm;

	@ApiModelProperty(value = "주문자핸드폰")
	private String buyerHp;

	@ApiModelProperty(value = "주문자연락처")
	private String buyerTel;

	@ApiModelProperty(value = "주문자이메일")
	private String buyerMail;

	@ApiModelProperty(value = "결제수단")
	private String paymentTypeCd;

	@ApiModelProperty(value = "주문자유형")
	private String buyerTypeCd;

	@ApiModelProperty(value = "주문유형")
	private String agentTypeCd;

	@ApiModelProperty(value = "등록일")
	private LocalDate createDt;

	@ApiModelProperty(value = "총회차수")
	private int totCnt;

	@ApiModelProperty(value = "기간연장횟수")
	private int termExtensionCnt;

	@ApiModelProperty(value = "최초배송기간")
	private String firstGoodsCycleTermTp;

	@ApiModelProperty(value = "최초배송주기")
	private String firstGoodsCycleTp;

	@ApiModelProperty(value = "최초요일")
	private String firstWeekCd;

	@ApiModelProperty(value = "배송기간")
	private String goodsCycleTermTp;

	@ApiModelProperty(value = "배송주기")
	private String goodsCycleTp;

	@ApiModelProperty(value = "요일")
	private String weekCd;

	@ApiModelProperty(value = "사용자환경정보")
	private String urPcidCd;

	@ApiModelProperty(value = "첫배송도착예정일자")
	private LocalDate deliveryDt;

	@ApiModelProperty(value = "신청상태")
	private String regularStatusCd;

	@ApiModelProperty(value = "회차정보생성여부")
	private String createRoundYn;

	@ApiModelProperty(value = "판매가")
	private int salePrice;

	@ApiModelProperty(value = "판할인금액")
	private int discountPrice;

	@ApiModelProperty(value = "결제예정금액")
	private int paidPrice;

	@ApiModelProperty(value= "정기배송 기본 할인율")
	private int basicDiscountRate;

	@ApiModelProperty(value= "정기배송 추가 할인 회차")
	private int addDiscountRound;

	@ApiModelProperty(value= "정기배송 추가 할인율")
	private int addDiscountRate;
}
