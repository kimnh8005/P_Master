package kr.co.pulmuone.v1.order.create.dto;


import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestPageDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


/**
 * <PRE>
 * Forbiz Korea
 * 신용카드 비인증 결제 Request Dto
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2020. 12. 15.            이명수         최초작성
 * =======================================================================
 * </PRE>
 */
@Getter
@Setter
@ToString
@ApiModel(description = "신용카드 비인증 결제  Dto")
public class OrderCardPayRequestDto extends BaseRequestPageDto{

	@ApiModelProperty(value = "결제금액")
	private String orderPrice;

	@ApiModelProperty(value = "카드번호")
	private String cardNo;

	@ApiModelProperty(value = "카드유효년도")
	private String cardNumYy;

	@ApiModelProperty(value = "카드유효월")
	private String cardNumMm;

	@ApiModelProperty(value = "부가정보구분")
	private String addInfoSel;

	@ApiModelProperty(value = "부가정보값")
	private String addInfoVal;

	@ApiModelProperty(value = "비밀번호")
	private String cardPass;

	@ApiModelProperty(value = "할부기간")
	private String planPeriod;

	@ApiModelProperty(value = "주문결제 마스터 PK")
	private long odPaymentMasterId;

	@ApiModelProperty(value = "주문리스트 검색용 ,로 구분")
	private String odid;

	@ApiModelProperty(value = "주문번호 리스트")
	List<String> findOdIdList;

	@ApiModelProperty(value = "주문 PK")
	private long odOrderId;

	@ApiModelProperty(value = "주문복사 매출만 연동 여부")
	private String orderCopySalIfYn;

	@ApiModelProperty(value = "PG 결제수단.PG사 은행/신용카드 코드(CARD_CODE)")
	private String pgBankCd;

	@ApiModelProperty(value = "PG 결제수단.은행/신용카드사명 공통코드명(CARD_CODE)")
	private String pgBankCdNm;

	private String flgCash;	// 현금영수증 발행여부(0: 미발행, 1: 소득공제 발행, 2: 지출증빙)

	private String cashReceiptNumber;

	@ApiModelProperty(value = "주문생성 구분")
	private String orderCreateTp;
}
