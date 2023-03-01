package kr.co.pulmuone.v1.goods.goods.dto;

import java.time.LocalDate;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "배송정보 Dto")
public class ShippingDataResultDto
{

	@ApiModelProperty(value = "배송정보 타입")
	private String deliveryType;

	@ApiModelProperty(value = "배송정보 타입명")
	private String deliveryTypeName;

	@ApiModelProperty(value = "매장명")
	private String storeName;

	@ApiModelProperty(value = "배송정보 금액")
	private String shippingPriceText;

	@ApiModelProperty(value = "도착 예정일자")
	private LocalDate arrivalScheduledDate;

	@ApiModelProperty(value = "지역별 배송비 사용 여부(Y: 사용, N: 사용안함)")
	private String areaShippingYn;

	@ApiModelProperty(value = "제주 추가 배송비")
	private int jejuShippingPrice;

	@ApiModelProperty(value = "도서산간 추가 배송비")
	private int islandShippingPrice;

	@ApiModelProperty(value = "도착 예정시간 - 시작")
	private String arrivalScheduledStartTime;

	@ApiModelProperty(value = "도착 예정시간 - 종료")
	private String arrivalScheduledEndTime;
}
