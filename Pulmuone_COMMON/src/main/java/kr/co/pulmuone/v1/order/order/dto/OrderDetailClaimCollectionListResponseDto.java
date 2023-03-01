package kr.co.pulmuone.v1.order.order.dto;

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
 * 주문 상세 클레임 회수 정보 리스트 응답 Response Dto
 * </PRE>
 */
@Builder
@Getter
@Setter
@ToString
@ApiModel(description = "주문 상세 클레임 회수 정보 리스트 응답 Response Dto")
public class OrderDetailClaimCollectionListResponseDto extends BaseResponseDto {

    @ApiModelProperty(value = "주문 상세 클레임 회수 정보 리스트")
	private	List<OrderDetailClaimCollectionListDto> rows;

}
