package kr.co.pulmuone.v1.store.delivery.dto;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "상품PK 기반으로 배송가능한 스토어배송권역정보 Dto")
public class ShippingPossibilityStoreDeliveryByGoodsIdDto
{

	@ApiModelProperty(value = "스토어 배송권역 정보에 따른 배송가능여부")
	private boolean shippingPossibility;

	// 일일상품 배송가능 기본일자
	private String defaultDelvDate;

	// 일일상품 배송가능 일자리스트
	private	List<String> delvDate;

	// 일일상품 배송가능 일자요일 리스트
	private List<String> delvDateWeekDay;
}
