package kr.co.pulmuone.v1.user.buyer.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.user.certification.dto.GetClauseArrayRequestDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "배송지 변경 가능 여부 체크 요청 DTO")
public class IsPossibleChangeDeliveryAddressRequestDto
{
	@ApiModelProperty(value = "주문PK", required = true)
	private Long odOrderId;

	@ApiModelProperty(value = "배송타입(일반,정기,일일,매장)", required = true)
	private String deliveryType;

	@ApiModelProperty(value = "변경 배송지 우편번호", required = true)
	private String zipCode;

	@ApiModelProperty(value = "변경 배송지 건물번호", required = true)
	private String buildingCode;

	@ApiModelProperty(value = "주문 배송 PK", required = true)
	private Long odShippingZoneId;


}
