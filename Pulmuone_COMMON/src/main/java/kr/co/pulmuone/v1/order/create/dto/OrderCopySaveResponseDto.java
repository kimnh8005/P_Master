package kr.co.pulmuone.v1.order.create.dto;

import java.util.List;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestDto;
import kr.co.pulmuone.v1.comm.enums.OrderEnums;
import kr.co.pulmuone.v1.order.order.dto.vo.OrderShippingPriceVo;
import kr.co.pulmuone.v1.order.order.dto.vo.OrderShippingZoneVo;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <PRE>
 * Forbiz Korea
 * 주문 복사 저장  Response Dto
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :	작성일                		:	작성자      :	작성내역
 * -----------------------------------------------------------------------
 *  1.0		2021. 03. 03.		강상국	최초작성
 * =======================================================================
 * </PRE>
 */
@Builder
@Getter
@Setter
@ToString
@ApiModel(description = "주문 복사 저장 Response Dto")
public class OrderCopySaveResponseDto extends BaseRequestDto {

    @ApiModelProperty(value = "주문 생성 결과 상태")
    private OrderEnums.OrderRegistrationResult result;

	@ApiModelProperty(value = "주문PK")
    private Long odOrderId;

	@ApiModelProperty(value = "주문번호")
	private String odid;

	@ApiModelProperty(value = "주문결제마스터PK")
	private long odPaymentMasterId;

	@ApiModelProperty(value = "주문복사 매출만 연동 여부")
	private String orderCopySalIfYn;

	@ApiModelProperty(value = "주문복사에러 메세지")
	private String errMsg;

}