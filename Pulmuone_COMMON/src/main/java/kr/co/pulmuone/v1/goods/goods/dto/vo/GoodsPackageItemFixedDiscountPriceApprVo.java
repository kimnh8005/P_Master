package kr.co.pulmuone.v1.goods.goods.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestPageDto;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ApiModel(description = "묶음상품 개별품목 할인승인 Vo")
public class GoodsPackageItemFixedDiscountPriceApprVo extends BaseRequestPageDto {
	
	@ApiModelProperty(value = "묶음상품 개별품목 할인정보 승인 ID", required = true)
	private String ilGoodsPackageItemFixedDiscountPriceApprId;
	
	@ApiModelProperty(value = "상품할인승인 ID", required = true)
	private String ilGoodsDiscountApprId;
	
	@ApiModelProperty(value = "묶음상품 관리 ID", required = true)
	private String ilGoodsPackageGoodsMappingId;

	@ApiModelProperty(value = "판매가(개별판매가 * 수량)", required = true)
	private long salePrice;
	
	@ApiModelProperty(value = "개별 판매가", required = true)
	private long unitSalePrice;
	
	@ApiModelProperty(value = "할인율(임직원 할인용)", required = true)
	private double discountRatio;
	
	@ApiModelProperty(value = "등록일", required = true)
	private String createDt;
	
	@ApiModelProperty(value = "수정일", required = true)
	private String modifyDt;
}
