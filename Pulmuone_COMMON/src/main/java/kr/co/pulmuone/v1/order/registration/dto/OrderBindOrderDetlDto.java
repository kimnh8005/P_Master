package kr.co.pulmuone.v1.order.registration.dto;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.order.order.dto.vo.OrderDetlDailyVo;
import kr.co.pulmuone.v1.order.order.dto.vo.OrderDetlDiscountVo;
import kr.co.pulmuone.v1.order.order.dto.vo.OrderDetlVo;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

/**
 * <PRE>
 * Forbiz Korea
 * 주문 생성 배송비 Dto
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2020. 12. 21.            이명수         최초작성
 * =======================================================================
 * </PRE>
 */
@Builder
@Getter
@ToString
@ApiModel(description = "주문 생성 배송비 Dto")
public class OrderBindOrderDetlDto {

	@ApiModelProperty(value = "상품순번")
	private int detlSeq;

	@ApiModelProperty(value = "주문 상세 VO")
	private OrderDetlVo orderDetlVo;

	@ApiModelProperty(value = "주문 상세 묶음 정보 VO")
	List<OrderBindOrderDetlPackDto> orderDetlPackList;

	@ApiModelProperty(value = "주문 상세 할인 정보 VO")
	List<OrderDetlDiscountVo> orderDetlDiscountList;

	@ApiModelProperty(value = "주문 상세 일일배송 패턴  VO")
	private OrderDetlDailyVo orderDetlDailyVo;
}
