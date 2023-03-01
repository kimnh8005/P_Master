package kr.co.pulmuone.v1.order.claim.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.pg.dto.BasicDataRequestDto;
import kr.co.pulmuone.v1.pg.dto.BasicDataResponseDto;
import kr.co.pulmuone.v1.policy.clause.dto.vo.GetLatestJoinClauseListResultVo;
import kr.co.pulmuone.v1.user.buyer.dto.vo.CodeInfoVo;
import kr.co.pulmuone.v1.user.buyer.dto.vo.CommonGetRefundBankResultVo;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.HashMap;
import java.util.List;

/**
 * <PRE>
 * Forbiz Korea
 * Mall 주문 클레임 추가결제 정보 Response Dto
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :	작성일		:	작성자	:	작성내역
 * -----------------------------------------------------------------------
 *  1.0		2021. 05. 14.	김명진		최초작성
 * =======================================================================
 * </PRE>
 */
@Getter
@Setter
@ToString
@ApiModel(description = "Mall 주문 클레임 추가결제 정보 Response Dto")
public class MallOrderClaimAddPaymentResponseDto {

	@ApiModelProperty(value = "주문PK")
	private long odOrderId;

	@ApiModelProperty(value = "주문클레임PK")
	private long odClaimId;

	@ApiModelProperty(value = "추가배송비")
	private int addPaymentShippingPrice;

	@ApiModelProperty(value = "상품명")
	private String goodsNm;

	@ApiModelProperty(value = "사용자PK")
	private long urUserId;

	@ApiModelProperty(value = "결제방법 정보 리스트")
	private List<HashMap<String,String>> paymentType;

	@ApiModelProperty(value = "카드 정보 리스트")
	private List<HashMap<String,String>> cardList;

	@ApiModelProperty(value = "할부기간")
	private List<HashMap<String,String>> installmentPeriod;

	@ApiModelProperty(value = "신용카드 혜택")
	private List<HashMap<String,String>> cartBenefit;

	@ApiModelProperty(value = "무통장 입금 결제 가능 여부")
	private String virtualAccountYn;

	@ApiModelProperty(value = "회원 사용 결제&카드 정보")
	private HashMap<String,String> userPayment;

	@ApiModelProperty(value = "약관 정보 리스트")
	private List<GetLatestJoinClauseListResultVo> clause;
}
