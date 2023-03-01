package kr.co.pulmuone.v1.goods.goods.dto;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.goods.goods.dto.vo.GoodsNutritionListResultVo;
import kr.co.pulmuone.v1.goods.goods.dto.vo.GoodsSpecListResultVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "묵음상품 Dto")
public class PackageGoodsListDto
{

	@ApiModelProperty(value = "묶음 상품 PK")
	private Long ilGoodsId;

	@ApiModelProperty(value = "묶음 상품명")
	private String goodsName;

	@ApiModelProperty(value = "썸네일이미지 경로")
	private String thumbnailPath;

	@ApiModelProperty(value = "증정품 여부")
	private boolean isGift;

	@ApiModelProperty(value = "표준브랜드 PK")
	private Long urBrandId;

	@ApiModelProperty(value = "묶음상품 영양정보 표시여부")
	private String nutritionDispYn;

	@ApiModelProperty(value = "묶음상품 상품정보제공고시")
	private List<GoodsSpecListResultVo> spec;

	@ApiModelProperty(value = "묶음상품 영양정보 리스트")
	private List<GoodsNutritionListResultVo> nutrition;

	@ApiModelProperty(value = "묶음상품 영양분석단위(1회분량)")
	private String nutritionQtyPerOnce;

	@ApiModelProperty(value = "묶음상품 영양분석단위(총분량)")
	private String nutritionQtyTotal;

	@ApiModelProperty(value = "묶음상품 영양성분 기타")
	private String nutritionEtc;

	@ApiModelProperty(value = "묶음상품 영양정보 기본정보 ")
	private String nutritionDispDefault;

	@ApiModelProperty(value = "묶음상품 상품 상세 기본정보")
	private String basicDescription;

	@ApiModelProperty(value = "묶음상품 상품 상세 주요정보")
	private String detailDescription;

	@ApiModelProperty(value = "묶음상품 상품 과세 여부 (Y:과세)")
	private String taxYn;

	@ApiModelProperty(value = "묶음상품 상품 재고")
	private int stockQty;

	@ApiModelProperty(value = "묶음상품 판매상태")
	private String saleStatus;

	@ApiModelProperty(value = "묶음상품 구성 수량")
	private int goodsQty;

	@ApiModelProperty(value = "묶음상품 정상 단가")
	private int recommendedPrice;

	@ApiModelProperty(value = "묶음상품 판매 단가")
	private int salePrice;

	@ApiModelProperty(value = "묶음상품 판매가")
	private int saleTotalPrice;

	@ApiModelProperty(value = "배송 가능 도착 예정일 DTO 리스트")
	private List<ArrivalScheduledDateDto> arrivalScheduledDateDtoList;

	@ApiModelProperty(value = "상품 배송 도착 예정일 DTO")
	private ArrivalScheduledDateDto arrivalScheduledDateDto;

	@ApiModelProperty(value = "구성상품 품목코드")
	private String ilItemCd;
}
