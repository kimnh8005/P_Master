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
@ApiModel(description = "상품 적용가능 쿠폰 Dto")
public class MallGoodsDetailDto {

	@ApiModelProperty(value = "상품 타입")
	private String goodsType;

	@ApiModelProperty(value = "상품 상세 기본정보")
	private String basicDescription;

	@ApiModelProperty(value = "상품 상세 주요정보")
	private String detailDescription;

	@ApiModelProperty(value = "상품정보제공고시")
	private List<GoodsSpecListResultVo> spec;

	@ApiModelProperty(value = "영양정보 표시여부")
	private String nutritionDispYn;

	@ApiModelProperty(value = "영양정보 리스트")
	private List<GoodsNutritionListResultVo> nutrition;

	@ApiModelProperty(value = "영양분석단위(1회분량)")
	private String nutritionQtyPerOnce;

	@ApiModelProperty(value = "영양분석단위(총분량)")
	private String nutritionQtyTotal;

	@ApiModelProperty(value = "영양성분 기타")
	private String nutritionEtc;

	@ApiModelProperty(value = "영양정보 기본정보 ")
	private String nutritionDispDefault;

	@ApiModelProperty(value = "묶음상품 상품상세 기본정보 직접등록 여부")
	private String goodsPackageBasicDescriptionYn;

	@ApiModelProperty(value = "묶음상품 상품상세 기본정보")
	private String goodsPackageBasicDescription;

	@ApiModelProperty(value = "묶음 상품 리스트")
	private List<PackageGoodsListDto> goodsPackageList;
}
