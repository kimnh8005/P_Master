package kr.co.pulmuone.v1.api.babymeal.dto;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseResponseDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <PRE>
 * Forbiz Korea
 * 베이비밀 주문정보, 배송 스케쥴정보 조회 Response Dto
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :	작성일                		:	작성자      :	작성내역
 * -----------------------------------------------------------------------
 *  1.0		2021. 02. 03.		이규한	최초작성
 * =======================================================================
 * </PRE>
 */

@Builder
@Getter
@Setter
@ToString
@ApiModel(description = "베이비밀 주문정보, 배송 스케줄정보 조회 API Response Dto")
public class BabymealOrderDeliveryListResponseDto extends BaseResponseDto {

	@ApiModelProperty(value = "베이비밀 주문정보")
	private BabymealOrderInfoDto goodsInfo;

    @ApiModelProperty(value = "베이비밀 배송 스케줄 리스트")
	private	List<BabymealOrderDeliveryListDto> rows;
}