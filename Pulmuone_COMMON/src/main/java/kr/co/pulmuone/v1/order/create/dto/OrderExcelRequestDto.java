package kr.co.pulmuone.v1.order.create.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

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
@Getter
@Setter
@ToString
@ApiModel(description = "주문 생성 엑셀 업로드 상품정보 조회 Dto")
public class OrderExcelRequestDto extends BaseRequestDto {

	@ApiModelProperty(value = "업로드")
	private String upload;

	@ApiModelProperty(value = "업로드 리스트")
    List<OrderExeclDto> orderExcelList;

}
