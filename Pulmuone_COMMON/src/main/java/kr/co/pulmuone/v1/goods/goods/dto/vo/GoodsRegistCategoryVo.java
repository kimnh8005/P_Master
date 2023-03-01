package kr.co.pulmuone.v1.goods.goods.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.aop.service.UserMaskingUserName;
import kr.co.pulmuone.v1.goods.goods.dto.GoodsRegistReservationOptionDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
@ApiModel(description = "상품등록/수정 카테고리 Vo")
public class GoodsRegistCategoryVo {

	@ApiModelProperty(value = "상품 카테고리 ID")
	String ilGoodsCtgryId;

	@ApiModelProperty(value = "상품 ID")
	int ilGoodsId;

	@ApiModelProperty(value = "전시 카테고리 > 기본 카테고리")
	String basicYn;

	@ApiModelProperty(value = "전시 카테고리 > 기본 카테고리(몰인몰)")
	String mallInMallBasicYn;

	//카테고리 리스트
	@ApiModelProperty(value = "카테고리ID")
	String ilCtgryId;

	@ApiModelProperty(value = "카테고리 명")
	String categoryName;

	@ApiModelProperty(value = "카테고리 단계")
	String depth;

	@ApiModelProperty(value = "부모 카테고리 ID")
	String parentsCategoryId;

	@ApiModelProperty(value = "성인 여부")
	String adultYn;

	@ApiModelProperty(value = "정렬순서")
	int sort;

	@ApiModelProperty(value = "")
	String isleaf;

	@ApiModelProperty(value = "부모 카테고리 명")
	String parentCtgryName;

	@ApiModelProperty(value = "전체 카테고리 명")
	String categoryFullName;

	@ApiModelProperty(value = "카테고리 종류")
	String mallDiv;
}
