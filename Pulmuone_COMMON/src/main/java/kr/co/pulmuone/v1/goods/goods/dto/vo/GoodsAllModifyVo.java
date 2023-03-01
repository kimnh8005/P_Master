package kr.co.pulmuone.v1.goods.goods.dto.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class GoodsAllModifyVo {

	@ApiModelProperty(value = "상품 ID")
    private String ilItemCd;

	@ApiModelProperty(value = "상품바코드")
    private String itemBarcode;

	@ApiModelProperty(value = "상품 ID")
    private String ilGoodsId;

	@ApiModelProperty(value = "프로모션명")
    private String promotionNm;

	@ApiModelProperty(value = "공급처ID")
    private String urSupplierId;

	@ApiModelProperty(value = "공급처명")
    private String supplierName;

	@ApiModelProperty(value = "상품명")
    private String goodsNm;

	@ApiModelProperty(value = "판매상태 공통코드(SALE_STATUS)")
    private String saleStatue;

	@ApiModelProperty(value = "판매상태 공통코드명")
	private String saleStatusName;

	@ApiModelProperty(value = "상품 가격ID")
    private String goodsPriceId;

	@ApiModelProperty(value = "표준카테고리ID")
    private String categoryStandardId;

	@ApiModelProperty(value = "전시카테고리 ID")
    private String categoryId;

	@ApiModelProperty(value = "전시여부")
	private String dispYn;

	@ApiModelProperty(value = "WEB PC 전시여부(Y:전시)")
    private String displayWebPcYn;

	@ApiModelProperty(value = "WEB MOBILE 전시여부(Y:전시)")
    private String displayWebMobileYn;

	@ApiModelProperty(value = "APP 전시여부(Y:전시)")
    private String displayAppYn;

	@ApiModelProperty(value = "쿠폰사용여부")
    private String couponUseYn;

	@ApiModelProperty(value = "추가상품 갯수")
    private String goodsAddCnt;

	@ApiModelProperty(value = "추가상품 ID")
    private String ilGoodsAdditionalGoodsMappingId;

	@ApiModelProperty(value = "추가상품 정보")
    private String goodsAddInfo;

	@ApiModelProperty(value = "표준카테고리명")
    private String categoryStandardDepthName;

	@ApiModelProperty(value = "전시카테고리명")
    private String categoryDepthName;

	@ApiModelProperty(value = "상품가격 ID")
    private String itemPriceId;

	@ApiModelProperty(value = "상품 할인 ID")
    private String goodsDiscountId;

	@ApiModelProperty(value = "원가")
    private int standardPrice;

	@ApiModelProperty(value = "할인율")
	private int discountPriceRatio;

	@ApiModelProperty(value = "정상가")
    private int recommendedPrice;

	@ApiModelProperty(value = "판매가")
    private int salePrice;

	@ApiModelProperty(value = "상품 할인 코드")
    private String discountTypeCode;

	@ApiModelProperty(value = "상품 할인 코드명")
    private String discountTypeName;

	@ApiModelProperty(value = "출고처명")
	private String warehouseNm;

	@ApiModelProperty(value = "출고처ID")
	private String urWarehouseId;

	@ApiModelProperty(value = "상품유형")
	private String goodsTp;

	@ApiModelProperty(value = "판매허용범위")
	private String displayTarget;
}
