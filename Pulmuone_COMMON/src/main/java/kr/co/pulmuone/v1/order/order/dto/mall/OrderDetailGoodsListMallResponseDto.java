package kr.co.pulmuone.v1.order.order.dto.mall;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseResponseDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * <PRE>
 * Forbiz Korea
 * 주문 상세 상품 리스트 관련 응답 Response Dto
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2021. 01. 05.            이명수         최초작성
 * =======================================================================
 * </PRE>
 */
@Builder
@Setter
@ToString
@ApiModel(description = "주문 상세 상품 리스트 응답 Response Dto")
public class OrderDetailGoodsListMallResponseDto extends BaseResponseDto {

    @ApiModelProperty(value = "주문 상세 상품 리스트")
	private	List<OrderDetailGoodsListMallDto> rows;

}
