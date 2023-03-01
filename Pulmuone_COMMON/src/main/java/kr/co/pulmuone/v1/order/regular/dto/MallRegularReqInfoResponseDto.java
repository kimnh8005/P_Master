package kr.co.pulmuone.v1.order.regular.dto;

import java.time.LocalDate;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseResponseDto;
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
@ApiModel(description = "정기배송 주문 신청 정보 Response Dto")
public class MallRegularReqInfoResponseDto extends BaseResponseDto {

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

	@ApiModelProperty(value = "페이징용 회차 총 갯수")
	private int totItemCnt;

	@ApiModelProperty(value = "정기배송 기본 할인율")
	private int basicDiscountRate;

	@ApiModelProperty(value = "정기배송 추가 할인 회차")
	private int addDiscountRound;

	@ApiModelProperty(value = "정기배송 추가 할인율")
	private int addDiscountRate;

	@ApiModelProperty(value = "다음배송일자")
	private LocalDate nextArriveDt;

	@ApiModelProperty(value = "기간연장가능여부")
	private String termExtensionYn;

	@ApiModelProperty(value = "회차정보목록")
	List<RegularResultReqRoundListDto> reqRoundList;
}
