package kr.co.pulmuone.v1.order.email.dto.vo;

import java.time.LocalDate;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.order.regular.dto.RegularResultReqRoundListDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <PRE>
 * Forbiz Korea
 * 정기배송 주문 신청 정보 Response Dto
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2021. 02. 09.	김명진		최초작성
 * =======================================================================
 * </PRE>
 */
@Getter
@Setter
@ToString
@ApiModel(description = "정기배송 주문 신청 정보 VO")
public class OrderRegularReqInfoVo{

	@ApiModelProperty(value = "정기배송주문신청 PK")
	private long odRegularReqId;

	@ApiModelProperty(value = "정기배송주문결과 PK")
	private long odRegularResultId;

	@ApiModelProperty(value = "마지막정기배송주문결과 PK")
	private long maxOdRegularResultId;

	@ApiModelProperty(value = "전체회차")
	private int totCnt;

	@ApiModelProperty(value = "배송기간코드")
	private String goodsCycleTermTp;

	@ApiModelProperty(value = "배송기간코드명")
	private String goodsCycleTermTpNm;

	@ApiModelProperty(value = "배송기간코드")
	private String goodsCycleTp;

	@ApiModelProperty(value = "배송기간코드명")
	private String goodsCycleTpNm;

	@ApiModelProperty(value = "요일코드")
	private String weekCd;

	@ApiModelProperty(value = "요일코드명")
	private String weekCdNm;

	@ApiModelProperty(value = "수령인명")
	private String recvNm;

	@ApiModelProperty(value = "수령인우편번호")
	private String recvZipCd;

	@ApiModelProperty(value = "수령인주소1")
	private String recvAddr1;

	@ApiModelProperty(value = "수령인주소2")
	private String recvAddr2;

	@ApiModelProperty(value = "수령인주소")
	private String recvAddr;

	@ApiModelProperty(value = "빌딩번호")
	private String recvBldNo;

	@ApiModelProperty(value = "수령인핸드폰")
	private String recvHp;

	@ApiModelProperty(value = "배송요청사항")
	private String deliveryMsg;

	@ApiModelProperty(value = "출입타입코드")
	private String doorMsgCd;

	@ApiModelProperty(value = "출입타입코드명")
	private String doorMsgCdNm;

	@ApiModelProperty(value = "출입현관비밀번호")
	private String doorMsg;

	@ApiModelProperty(value = "배송기간시작일자")
	private LocalDate startArriveDt;

	@ApiModelProperty(value = "배송기간종료일자")
	private LocalDate endArriveDt;

	@ApiModelProperty(value = "기간연장횟수")
	private int termExtensionCnt;

	@ApiModelProperty(value = "현재회차")
	private int reqRound;

	@ApiModelProperty(value = "다음배송일자")
	private LocalDate nextArriveDt;

	@ApiModelProperty(value = "다음배송일자_(MM)")
	private String arriveDtMonth;

	@ApiModelProperty(value = "다음배송일자_(DD)")
	private String arriveDtDay;

	@ApiModelProperty(value = "회원 PK")
	private Long urUserId;

	@ApiModelProperty(value = "주문자명")
	private String buyerName;

	@ApiModelProperty(value = "모바일")
	private String mobile;

	@ApiModelProperty(value = "이메일")
	private String mail;

	@ApiModelProperty(value = "신청일자")
	private String createDate;

	@ApiModelProperty(value = "카드명")
	private String cardName;

	@ApiModelProperty(value = "카드번호")
	private String cardMaskNumber;

	@ApiModelProperty(value = "정기배송 첫주문여부 (Y.첫주문 N.추가주문)")
	private String firstOrderYn;

	@ApiModelProperty(value = "결제예정금액")
	private int paymentPrice;

	@ApiModelProperty(value = "정기배송 기본할인율")
	private int regularBasicDiscountRate;

	/* 정기배송 상품금액 변동 안내*/
	@ApiModelProperty(value = "변경 전 결제금액")
	private int beforeSalePrice;

	@ApiModelProperty(value = "변경 후 결제금액")
	private int afterSalePrice;

}
