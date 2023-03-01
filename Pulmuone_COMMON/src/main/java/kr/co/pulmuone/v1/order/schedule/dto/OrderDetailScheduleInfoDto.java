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
 *  1.0    2021. 3. 4.       석세동         최초작성
 * =======================================================================
 * </PRE>
 */

@Getter
@Setter
@ToString
@ApiModel(description = "주문 상세 스케줄 Info Dto")
public class OrderDetailScheduleInfoDto {

	@ApiModelProperty(value = "배송일자")
	private String delvDate;

	@ApiModelProperty(value = "수령인 우편번호")
	private String recvZipCd;

    @ApiModelProperty(value = "건물번호")
    private String recvBldNo;

	@ApiModelProperty(value = "I/F 상품 코드")
	private String ilGoodsId;

	@ApiModelProperty(value = "출고처 PK")
	private long urWarehouseId;

}