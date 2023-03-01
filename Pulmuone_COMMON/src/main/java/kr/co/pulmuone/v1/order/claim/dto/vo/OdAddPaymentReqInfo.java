package kr.co.pulmuone.v1.order.claim.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


/**
 * <PRE>
 * Forbiz Korea
 * 주문 추가결제 요청 정보 VO
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2021. 04. 14.	        김명진		최초작성
 * =======================================================================
 * </PRE>
 */
@Builder
@Getter
@Setter
@ToString
@ApiModel(description = "주문 추가결제 요청 정보 VO")
public class OdAddPaymentReqInfo {

	@ApiModelProperty(value = "추가결제요청정보PK")
	private long odAddPaymentReqInfoId;

    @ApiModelProperty(value = "요청 JSON 정보")
    private String reqJsonInfo;
}
