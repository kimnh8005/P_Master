package kr.co.pulmuone.v1.order.claim.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestPageDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <PRE>
 * Forbiz Korea
 * Mall 주문 클레임 요청 정보 Request Dto
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :	작성일		:	작성자	:	작성내역
 * -----------------------------------------------------------------------
 *  1.0		2021. 01. 22.	김명진		최초작성
 * =======================================================================
 * </PRE>
 */
@Getter
@Setter
@ToString
@ApiModel(description = "Mall 주문 클레임 요청 정보 Request Dto")
public class MallOrderClaimRequestDto extends BaseRequestPageDto {

	@ApiModelProperty(value = "클레임타입", required = true)
    private String claimStatusCd;

	@ApiModelProperty(value = "주문PK", required = true)
    private long odOrderId;

	@ApiModelProperty(value = "주문상세PK")
    private long odOrderDetlId;

	@ApiModelProperty(value = "상품PK")
    private long ilGoodsId;

	@ApiModelProperty(value = "상품타입코드")
	private String goodsTpCd;
}