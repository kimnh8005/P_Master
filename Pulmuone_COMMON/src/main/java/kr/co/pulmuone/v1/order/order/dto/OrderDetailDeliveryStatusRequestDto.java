package kr.co.pulmuone.v1.order.order.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = " 주문상세 배송상태 업데이트 RequestDto")
public class OrderDetailDeliveryStatusRequestDto extends BaseRequestDto {

	@ApiModelProperty(value = "주문상세 PK")
	private Long odOrderDetlId;

	@ApiModelProperty(value = "택배사 PK")
	private Long psShippingCompId;

	@ApiModelProperty(value = "개별송장번호")
	private String trackingNo;

	@ApiModelProperty(value = "정상주문상태")
	private String orderStatusCd;
}