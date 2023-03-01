package kr.co.pulmuone.v1.goods.goods.dto;



import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestPageDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "묶음상품, 증정품 구성목록 Request")
public class GoodsRegistPackageGoodsMappingDto extends BaseRequestPageDto {

	@ApiModelProperty(value = "묶음상품 관리ID", required = true)
	private String ilGoodsPackageGoodsMappingId;

	@ApiModelProperty(value = "묶음상품 구성상품 품목 ID", required = true)
	private String ilItemCode;

	@ApiModelProperty(value = "묶음상품ID", required = true)
	private String ilGoodsId;

	@ApiModelProperty(value = "묶음 구성 상품 ID", required = true)
	private String targetGoodsId;

	@ApiModelProperty(value = "묶음상품 상품유형", required = true)
	private String goodsType;

	@ApiModelProperty(value = "기준상품", required = true)
	private String baseGoodsYn;

	@ApiModelProperty(value = "구성수량", required = true)
	private int goodsQuantity;

	@ApiModelProperty(value = "구성상품 판매가(개당)", required = true)
	private int salePricePerUnit;

	@ApiModelProperty(value = "판매가", required = true)
	private int salePrice;

	@ApiModelProperty(value = "증정품 정상가(개당)", required = true)
	private int recommendedPrice;

	@ApiModelProperty(value = "증정품 정상가 총액", required = true)
	private int recommendedTotalPrice;

	@ApiModelProperty(value = "증정품 구성수량", required = true)
	private int purchaseQuanity;

	@ApiModelProperty(value = "상품별 대표이미지 정렬 순서", required = true)
	private int imageSort;

	@ApiModelProperty(value = "등록자", required = false)
	private String createId;

	@ApiModelProperty(value = "수정자", required = false)
	private String modifyId;
}
