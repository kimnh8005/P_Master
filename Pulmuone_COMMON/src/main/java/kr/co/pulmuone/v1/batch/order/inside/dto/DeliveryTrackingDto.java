package kr.co.pulmuone.v1.batch.order.inside.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


/**
 * <PRE>
 * Forbiz Korea
 * 주문상세 상품에서 배송중이면서 택배사가 CJ, 롯데 택배사 조회 및 주문상세 상태 업데이트 요청 Dto
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2021. 02. 18.   강상국         최초작성
 * =======================================================================
 * </PRE>
 */

@Getter
@Setter
@ToString
@ApiModel(description = "주문상세 상품에서 배송중이면서 택배사가 CJ, 롯데 택배사 조회 및 주문상세 상태 업데이트 요청 Dto")
public class DeliveryTrackingDto {

	@ApiModelProperty(value = "정상주문상태 OD_STATUS_INFO.STATUS_CD")
	private String orderStatusCd;

	@ApiModelProperty(value = "택배회사코드1")
	private long psShippingCompId1;

	@ApiModelProperty(value = "택배회사코드2")
	private long psShippingCompId2;
}
