package kr.co.pulmuone.v1.goods.goods.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.aop.service.UserMaskingUserName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@ApiModel(description = "가격 계산 상품 리스트 Vo")
public class GoodsPackageCalcListVo {

	@ApiModelProperty(value="마스터품목코드")
	String ilItemCode;

	@ApiModelProperty(value="묶음상품 관리ID")
	String ilGoodsPackageGoodsMappingId;

	@ApiModelProperty(value="마스터품목바코드")
	String itemBarcode;

	@ApiModelProperty(value="상품ID")
	String ilGoodsId;

	@ApiModelProperty(value="상품유형")
	String goodsType;

	@ApiModelProperty(value="상품유형명")
	String goodsTypeName;

	@ApiModelProperty(value="상품명")
	String goodsName;

	@ApiModelProperty(value="과세구분")
	String taxYn;

	@ApiModelProperty(value="과세구분명")
	String taxYnName;

	@ApiModelProperty(value = "원가")
	private int standardPrice;

	@ApiModelProperty(value = "정상가")
	private int recommendedPrice;

	@ApiModelProperty(value = "구성수량", required = false)
	private int goodsQuantity;

	@ApiModelProperty(value = "원가 총액", required = false)
	private int standardTotalPrice;

	@ApiModelProperty(value = "정상가총액")
	private int recommendedTotalPrice;

	@ApiModelProperty(value = "상품할인 ID")
	private String ilGoodsDiscountId;

	@ApiModelProperty(value = "정률할인액")
	private int discountRatio;

	@ApiModelProperty(value = "고정할인가")
	private int discountSalePrice;

	@ApiModelProperty(value = "묶음상품(개당) 가격")
	private int salePrice;

	@ApiModelProperty(value = "묶음상품 총액")
	private int saleTotalPrice;
}
