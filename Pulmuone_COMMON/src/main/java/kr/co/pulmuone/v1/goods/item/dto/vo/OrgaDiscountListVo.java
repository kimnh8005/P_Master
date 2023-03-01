package kr.co.pulmuone.v1.goods.item.dto.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class OrgaDiscountListVo {

	@ApiModelProperty(value = "순번")
	private String rowNumber; // 순번

	@ApiModelProperty(value = "품목코드 PK")
	private String ilItemCd;

	@ApiModelProperty(value = "품목바코드")
	private String itemBarcode;

	@ApiModelProperty(value = "마스터 품목명")
	private String itemNm;

	@ApiModelProperty(value = "상품할인 유형 공통코드(GOODS_DISCOUNT_TP - PRIORITY:우선할인, ERP_EVENT:올가할인, IMMEDIATE:즉시할인)")
	private String discountTp;

	@ApiModelProperty(value = "할인 시작일")
	private String discountStartDt;

	@ApiModelProperty(value = "할인 종료일")
	private String discountEndDt;

	@ApiModelProperty(value = "할인판매가")
	private int discountSalePrice;

	@ApiModelProperty(value = "할인판매가")
	private String discountSalePriceStr;

	@ApiModelProperty(value = "상품할인 유형 공통코드명")
	private String discountTpNm;
}
