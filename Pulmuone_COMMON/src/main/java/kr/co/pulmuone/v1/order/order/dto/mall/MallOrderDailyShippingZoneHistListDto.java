package kr.co.pulmuone.v1.order.order.dto.mall;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <PRE>
 * Forbiz Korea
 * Mall 일일배송 배송지 변경 내역 리스트 Dto
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :	작성일		:	작성자	:	작성내역
 * -----------------------------------------------------------------------
 *  1.0		2021. 01. 29.	김명진		최초작성
 * =======================================================================
 * </PRE>
 */

@Getter
@Setter
@ToString
@ApiModel(description = "Mall 일일배송 배송지 변경 내역 리스트 Dto")
public class MallOrderDailyShippingZoneHistListDto {

	@ApiModelProperty(value = "적용일자 From")
	private String applyDtFrom;

	@ApiModelProperty(value = "적용일자 To")
	private String applyDtTo;

	@ApiModelProperty(value = "주소1")
	private String recvAddr1;

	@ApiModelProperty(value = "주소2")
	private String recvAddr2;

	@ApiModelProperty(value = "주소 받는사람명")
	private String recvNm;

	@ApiModelProperty(value = "주소 우편번호")
	private String recvZipCd;

	@ApiModelProperty(value = "주소 건물번호")
	private String recvBldNo;

	@ApiModelProperty(value = "변경이력")
	List<MallOrderDailyShippingZoneHistListDto> histList;

	@ApiModelProperty(value = "현재 주소 여부")
	private String nowYn;

	@ApiModelProperty(value = "주문배송지 PK")
	private long odShippingZoneId;

}