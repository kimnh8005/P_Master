package kr.co.pulmuone.v1.promotion.coupon.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "GetShippingConditionListResultVo")
public class ShippingConditionListResultVo {

	@ApiModelProperty(value = "no")
	private String no;

	@ApiModelProperty(value = "배송정책명")
	private String shippingConditionName;

	@ApiModelProperty(value = "조건배송비 구분 코드")
	private String conditionType;

	@ApiModelProperty(value = "조건배송비 명")
	private String conditionTypeName;

	@ApiModelProperty(value = "배송비")
	private String shippingPrice;

	@ApiModelProperty(value = "출고처 ID")
	private String warehouseId;

	@ApiModelProperty(value = "출고처")
	private String warehouseName;

	@ApiModelProperty(value = "묶음구분")
	private String bundleYn;

	@ApiModelProperty(value = "선결재여부 코드")
	private String panmentMethodType;

	@ApiModelProperty(value = "선결재여부 명")
	private String panmentMethodTypeName;

}
