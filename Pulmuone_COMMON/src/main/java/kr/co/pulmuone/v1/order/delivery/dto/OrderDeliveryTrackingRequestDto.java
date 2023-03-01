package kr.co.pulmuone.v1.order.delivery.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


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
public class OrderDeliveryTrackingRequestDto extends BaseRequestDto {

	@ApiModelProperty(value = "택배사코드")
	private String logisticsCd;

	@ApiModelProperty(value = "개별송장번호")
	private String trackingNo;

	@ApiModelProperty(value = "반품 배송추적 여부")
	private String returnsYn;
}
