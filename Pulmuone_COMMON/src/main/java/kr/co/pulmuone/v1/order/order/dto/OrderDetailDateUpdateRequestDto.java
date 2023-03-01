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
@ApiModel(description = "주문 상세 상태 일자 업데이트 RequestDto")
public class OrderDetailDateUpdateRequestDto extends BaseRequestDto {

	@ApiModelProperty(value = "주문상세 PK")
	private Long odOrderDetlId;

	@ApiModelProperty(value = "주문 I/F 등록자")
	private Long orderIfId;

	@ApiModelProperty(value = "주문 I/F 일자")
	private String orderIfDt;

	@ApiModelProperty(value = "출고예정일 등록자")
	private Long shippingId;

	@ApiModelProperty(value = "출고예정일 일자")
	private String shippingDt;

	@ApiModelProperty(value = "도착예정일 등록자")
	private Long deliveryId;

	@ApiModelProperty(value = "도착예정일 일자")
	private String deliveryDt;

	@ApiModelProperty(value = "동일 출고처 변경여부")
	private String allChangeYn;

	@ApiModelProperty(value = "상품 타입")
	private String goodsTpCd;

	@ApiModelProperty(value = "일일배송 상품 타입")
	private String goodsDailyTp;

	@ApiModelProperty(value = "출고처 PK")
	private long urWarehouseId;

	@ApiModelProperty(value = "새벽배송여부 (Y:새벽배송 N:일반배송)")
	private String orderIfDawnYn;

	@ApiModelProperty(value = "주문 배송지 PK")
	private long odShippingZoneId;

	@ApiModelProperty(value = "프로모션타입(균일가 골라담기,녹즙내맘대로)")
	private String promotionTp;
}