package kr.co.pulmuone.v1.order.claim.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


/**
 * <PRE>
 * Forbiz Korea
 * 취소 과세, 비과세 금액 조회 결과 Dto
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
@ApiModel(description = "취소 과세, 비과세 금액 조회 결과 Dto")
public class CancelPriceInfoDto {

	@ApiModelProperty(value = "취소 과세 금액")
	private int taxPrice;

	@ApiModelProperty(value = "취소 비과세 금액")
	private int freePrice;

	@ApiModelProperty(value = "배송비")
	private int shippingPrice;
}
