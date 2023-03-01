package kr.co.pulmuone.v1.goods.goods.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.enums.BaseEnums;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "추가배송비 권역(도서산간, 제주)")
public class ShippingAreaVo {

	@ApiModelProperty(value = "우편번호")
	private String zipCode;

	@ApiModelProperty(value = "제주지역 여부")
	private String jejuYn;

	@ApiModelProperty(value = "도서산간 지역 여부")
	private String islandYn;

	@ApiModelProperty(value = "대체배송 여부")
	private BaseEnums.AlternateDeliveryType alternateDeliveryType;
}
