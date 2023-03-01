package kr.co.pulmuone.v1.goods.item.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ItemStoreStockDto {
	// 매장 재고
	@ApiModelProperty(value = "품목 코드")
	private String itemCode;

	@ApiModelProperty(value = "마스터 품목명")
	private String itemNm;

	@ApiModelProperty(value = "공급업체")
	private String companyNm;

	@ApiModelProperty(value = "전시브랜드명")
	private String dispBrandNm;

	@ApiModelProperty(value = "표준브랜드명")
	private String brandNm;

	@ApiModelProperty(value = "매장명")
	private String storeNm;

	@ApiModelProperty(value = "표준카테고리")
	private String category;

	@ApiModelProperty(value = "매장 상품 유형")
	private String itemStoreType;

	@ApiModelProperty(value = "재고")
	private Integer storeStock;

	@ApiModelProperty(value = "ERP 연동 여부")
	private String erpIfYn;

	@ApiModelProperty(value = "품목 타입")
	private String itemType;


}
