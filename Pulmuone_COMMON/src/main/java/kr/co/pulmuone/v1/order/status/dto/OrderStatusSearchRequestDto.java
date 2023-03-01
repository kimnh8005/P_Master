package kr.co.pulmuone.v1.order.status.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


/**
 * <PRE>
 * Forbiz Korea
 * 주문상태 검색 관련 Request Dto
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2020. 12. 14.            이명수         최초작성
 * =======================================================================
 * </PRE>
 */
@Getter
@Setter
@ToString
@ApiModel(description = "주문상태 검색조건 Request Dto")
public class OrderStatusSearchRequestDto extends BaseRequestDto {

	@ApiModelProperty(value = "주문상태 검색구분")
	private String searchGrp;

	@ApiModelProperty(value = "주문상태 사용여부")
	private String useYn;

	@Builder
	public OrderStatusSearchRequestDto(String searchGrp, String useYn) {
		this.searchGrp = searchGrp;
		this.useYn = useYn;
	}
}

