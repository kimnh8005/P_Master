package kr.co.pulmuone.v1.order.registration.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.order.order.dto.vo.OrderDtVo;
import kr.co.pulmuone.v1.order.order.dto.vo.OrderPaymentMasterVo;
import kr.co.pulmuone.v1.order.order.dto.vo.OrderPaymentVo;
import kr.co.pulmuone.v1.order.order.dto.vo.OrderVo;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

/**
 * <PRE>
 * Forbiz Korea
 * 주문 생성 관련 Request Dto
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
@Builder
@Getter
@ApiModel(description = "주문 생성 관련 Request Dto")
public class OrderOutmallMemoDto {

	@ApiModelProperty(value = "외부몰번호")
	private String outmallId;

	@ApiModelProperty(value = "상품PK")
	private String ilGoodsId;

	@ApiModelProperty(value = "고객상담")
	private String memo;
}
