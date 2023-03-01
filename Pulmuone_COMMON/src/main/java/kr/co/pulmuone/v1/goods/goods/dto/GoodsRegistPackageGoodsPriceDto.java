package kr.co.pulmuone.v1.goods.goods.dto;



import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestPageDto;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
@ApiModel(description = "묶음상품 할인가격 관련 정보 Request")
public class GoodsRegistPackageGoodsPriceDto extends BaseRequestPageDto {

	/* 묶음상품 원본 가격 시작 */
	@ApiModelProperty(value = "묶음상품 ID", required = true)
	private String ilGoodsId;

	@ApiModelProperty(value = "가격 시작일", required = true)
	private String priceStartDate;

	@ApiModelProperty(value = "가격 종료일", required = true)
	private String priceEndDate;

	@ApiModelProperty(value = "원가", required = true)
	private int standardPrice;

	@ApiModelProperty(value = "정상가", required = true)
	private int recommendedPrice;
	/* 묶음상품 원본 가격 끝 */

	/* 묶음상품 개별품목 고정가 할인가격 시작 */
	@ApiModelProperty(value = "상품할인 ID", required = true)
	private String ilGoodsDiscountId;

	@ApiModelProperty(value = "묶음상품 관리 ID", required = true)
	private String ilGoodsPackageGoodsMappingId;

	@ApiModelProperty(value = "수량", required = false)
	private int goodsQuantity;

	@ApiModelProperty(value = "판매가", required = true)
	private int salePricePerUnit;

	@ApiModelProperty(value = "판매가(개별판매가 * 수량)", required = true)
	private int salePrice;

	@ApiModelProperty(value = "임직원 할인 개별 할인율", required = true)
	private double discountRatio;
	/* 묶음상품 개별품목 고정가 할인가격 끝 */
}
