package kr.co.pulmuone.v1.order.regular.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.order.order.dto.vo.OrderPaymentMasterVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * <PRE>
 * Forbiz Korea
 * 정기배송 주문 결과
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
@ApiModel(description = "정기배송 주문 결과 VO")
public class OrderRegularResultVo {

	@ApiModelProperty(value = "정기배송주문신청결과정보PK")
	private long odRegularResultId;

	@ApiModelProperty(value = "정기배송주문신청 PK")
	private long odRegularReqId;

	@ApiModelProperty(value = "주문PK")
	private long odOrderId;

	@ApiModelProperty(value = "요청회차")
	private int reqRound;

	@ApiModelProperty(value = "결제실패건수")
	private int paymentFailCnt;

	@ApiModelProperty(value = "주문생성예정일")
	private String orderCreateDt;

	@ApiModelProperty(value = "결제예정일")
	private String paymentDt;

	@ApiModelProperty(value = "도착예정일")
	private String arriveDt;

	@ApiModelProperty(value = "주문생성여부")
	private String orderCreateYn;

	@ApiModelProperty(value = "신청상세상태 공톧코드 : REGULAR_STATUS_CD")
	private String regularStatusCd;

	@ApiModelProperty(value = "회차 완료 여부")
	private String reqRoundYn;

	@ApiModelProperty(value = "입금취소처리여부")
	private boolean ibFlag;

	@ApiModelProperty(value = "입금전취소처리 대상 상품목록")
	private List<Long> ibIlGoodsIds;

	@ApiModelProperty(value = "결제마스터정보")
	private OrderPaymentMasterVo orderPaymentMasterVo;
}
