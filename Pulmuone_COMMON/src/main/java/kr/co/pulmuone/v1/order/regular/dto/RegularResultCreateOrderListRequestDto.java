package kr.co.pulmuone.v1.order.regular.dto;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <PRE>
 * Forbiz Korea
 * 정기배송 결과 주문생성 상품 목록 조회 Request Dto
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2021. 02. 21.	김명진		최초작성
 * =======================================================================
 * </PRE>
 */
@Getter
@Setter
@ToString
@ApiModel(description = "정기배송 결과 주문생성 상품 목록 조회 Request Dto")
public class RegularResultCreateOrderListRequestDto {

	@ApiModelProperty(value = "상품판매상태")
	private String saleStatus;

	@ApiModelProperty(value = "상품상태코드")
	private List<String> regularStatusCdList;

	@ApiModelProperty(value = "상품상세상태코드")
	private String reqDetailStatusCd;

	@ApiModelProperty(value = "정기배송결과PK")
	private long odRegularResultId;
}
