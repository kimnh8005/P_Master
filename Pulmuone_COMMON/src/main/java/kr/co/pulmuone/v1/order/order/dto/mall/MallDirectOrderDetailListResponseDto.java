package kr.co.pulmuone.v1.order.order.dto.mall;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseResponseDto;
import kr.co.pulmuone.v1.order.claim.dto.OrderClaimAttcInfoDto;
import kr.co.pulmuone.v1.order.claim.dto.vo.ClaimShippingZoneVo;
import kr.co.pulmuone.v1.policy.clause.dto.vo.GetLatestJoinClauseListResultVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

/**
 * <PRE>
 * Forbiz Korea
 * Mall 주문상세 리스트 Response Dto
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :	작성일		:	작성자	:	작성내역
 * -----------------------------------------------------------------------
 *  1.0		2021. 01. 20.	이규한		최초작성
 * =======================================================================
 * </PRE>
 */

@Getter
@Setter
@ToString
@ApiModel(description = "Mall 상담원 결제 Response Dto")
public class MallDirectOrderDetailListResponseDto extends MallOrderDetailListResponseDto {
	public MallDirectOrderDetailListResponseDto(MallOrderDetailListResponseDto dto) {
		setOdOrderId(dto.getOdOrderId());
		setOdid(dto.getOdid());
		setCreateDt(dto.getCreateDt());
		setOrder(dto.getOrder());
		setShippingAddress(dto.getShippingAddress());
		setPayInfo(dto.getPayInfo());
		setOrderDetailList(dto.getOrderDetailList());
		setGiftGoodsList(dto.getGiftGoodsList());
		setDiscountList(dto.getDiscountList());
		setGoodsDetailList(dto.getGoodsDetailList());
	}

	@ApiModelProperty(value = "결제방법 정보 리스트")
	private List<HashMap<String,String>> paymentType;

	@ApiModelProperty(value = "카드 정보 리스트")
	private List<HashMap<String,String>> cardList;

	@ApiModelProperty(value = "할부기간")
	private List<HashMap<String,String>> installmentPeriod;

	@ApiModelProperty(value = "신용카드 혜택")
	private List<HashMap<String,String>> cartBenefit;

	@ApiModelProperty(value = "약관 정보 리스트")
	private List<GetLatestJoinClauseListResultVo> clause;

	@ApiModelProperty(value = "무통장 입금 결제 가능 여부")
	private String virtualAccountYn;

	@ApiModelProperty(value = "회원 사용 결제&카드 정보")
	private HashMap<String,String> userPayment;
}