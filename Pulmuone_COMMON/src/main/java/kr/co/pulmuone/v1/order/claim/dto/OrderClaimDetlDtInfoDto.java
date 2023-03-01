package kr.co.pulmuone.v1.order.claim.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


/**
 * <PRE>
 * Forbiz Korea
 * 주문 클레임 상세 상태일자 조회 결과 Dto
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
@ApiModel(description = "주문 클레임 상세 상태일자 조회 결과 Dto")
public class OrderClaimDetlDtInfoDto {
	@ApiModelProperty(value = "주문 클레임 상세 PK")
	private long odClaimDetlId;

	@ApiModelProperty(value = "주주문클레임 PK")
	private long odClaimId;

	@ApiModelProperty(value = "출고예정일자")
	private String shippingDt;

	@ApiModelProperty(value = "도착예정일자")
	private String deliveryDt;
}
