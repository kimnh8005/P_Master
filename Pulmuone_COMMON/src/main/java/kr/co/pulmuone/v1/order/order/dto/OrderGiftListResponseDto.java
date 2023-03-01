package kr.co.pulmuone.v1.order.order.dto;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseResponseDto;
import kr.co.pulmuone.v1.order.order.dto.vo.OrderGiftVo;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <PRE>
 * Forbiz Korea
 * 주문 상세 결제정보 > 즉시할인내역 리스트 관련 응답 Response Dto
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2021.03.19.      천혜현         최초작성
 * =======================================================================
 * </PRE>
 */
@Builder
@Getter
@Setter
@ToString
@ApiModel(description = "주문 상세 결제정보 > 증정품 내역  응답 Response Dto")
public class OrderGiftListResponseDto extends BaseResponseDto {

    @ApiModelProperty(value = "주문 상세 증정품 내역 리스트")
	private	List<OrderGiftVo> rows;

}
