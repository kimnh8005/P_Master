package kr.co.pulmuone.v1.order.schedule.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 *
 * <PRE>
 * Forbiz Korea
 * 주문리스트 관련 dto
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2021. 4. 23.       석세동         최초작성
 * =======================================================================
 * </PRE>
 */

@Getter
@Setter
@ToString
@ApiModel(description = "주문 상세 Date Info Dto")
public class OrderDetailScheduleDateInfoDto {

	@ApiModelProperty(value = "주문배송지 PK")
	private long odShippingZoneId;

	@ApiModelProperty(value = "배송시작일자")
	private String startDate;

	@ApiModelProperty(value = "배송종료일자")
	private String endDate;

}