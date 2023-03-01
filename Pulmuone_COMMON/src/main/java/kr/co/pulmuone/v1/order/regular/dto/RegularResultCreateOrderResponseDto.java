package kr.co.pulmuone.v1.order.regular.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

/**
 * <PRE>
 * Forbiz Korea
 * 정기배송 주문생성 결과 응답 VO
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2021. 08. 27.	김명진		최초작성
 * =======================================================================
 * </PRE>
 */
@Getter
@Setter
@ToString
@ApiModel(description = "정기배송 결과 주문생성 상품 목록 조회 Dto")
public class RegularResultCreateOrderResponseDto {

	@ApiModelProperty(value = "입금전 취소 처리 여부")
	private boolean ibFlag;

	@ApiModelProperty(value = "주문생성완료여부")
	private boolean orderCreateFlag;

	@ApiModelProperty(value = "주문번호")
	private String odid;
}
