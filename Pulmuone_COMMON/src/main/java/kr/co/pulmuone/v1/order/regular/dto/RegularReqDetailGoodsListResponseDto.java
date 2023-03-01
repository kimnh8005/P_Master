package kr.co.pulmuone.v1.order.regular.dto;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestPageDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <PRE>
 * Forbiz Korea
 * 정기배송 주문 신청 상세 리스트 Dto
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2021. 02. 07.	김명진		최초작성
 * =======================================================================
 * </PRE>
 */
@Getter
@Setter
@ToString
@ApiModel(description = "정기배송 주문 신청 상세 리스트 Dto")
public class RegularReqDetailGoodsListResponseDto extends BaseRequestPageDto {

	@ApiModelProperty(value = "총 갯수")
	private int total;

	@ApiModelProperty(value = "정기배송 주문 신청 내역 상품 목록")
	List<RegularResultReqRoundGoodsListDto> rows;
}
