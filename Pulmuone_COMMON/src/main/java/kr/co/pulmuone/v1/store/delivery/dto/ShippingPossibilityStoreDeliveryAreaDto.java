package kr.co.pulmuone.v1.store.delivery.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "배송가능한 스토어배송권역정보 Dto")
public class ShippingPossibilityStoreDeliveryAreaDto
{

	@ApiModelProperty(value = "스토어(매장가맹점) PK")
	private String urStoreId;

	@ApiModelProperty(value = "스토어(매장가맹점) 명")
	private String storeName;

	@ApiModelProperty(value = "배송권역_고유값(ERP연동)")
	private String urDeliveryAreaId;

	@ApiModelProperty(value = "배송방식(매일,격일)")
	private String storeDeliveryIntervalType;

	@ApiModelProperty(value = "배송타입(매방배송, 매장픽업, 오피스배송, 홈배송, 홈/오피스배송)")
	private String storeDeliveryType;

	@ApiModelProperty(value = "배송가능품목유형(ALL, FD_ONLY, DM )\\n*가맹점만 사용")
	private String storeDeliverableItemType;

	@ApiModelProperty(value = "스토어 중복 여부")
	private boolean storeOverlap;
}
