package kr.co.pulmuone.v1.goods.goods.dto;

import java.util.ArrayList;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestPageDto;
import kr.co.pulmuone.v1.goods.goods.dto.vo.GoodsRegistCategoryVo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
@ApiModel(description = "상품 카테고리 Request")
public class GoodsRegistCategoryRequestDto extends BaseRequestPageDto {

	@ApiModelProperty(value = "상품 카테고리ID", required = true)
	private String ilGoodsCtgryId;

	@ApiModelProperty(value = "상품ID", required = true)
	private String ilGoodsId;

	@ApiModelProperty(value = "선택한 부모 카테고리 ID", required = true)
	private String parentsCategoryId;

	@ApiModelProperty(value = "선택한 카테고리 ID", required = true)
	private String ilCtgryId;

	@ApiModelProperty(value = "전체 카테고리 명", required = true)
	private String categoryFullName;

	@ApiModelProperty(value = "전시 or 몰인몰 구분값", required = true)
	private String mallDiv;

	@ApiModelProperty(value = "전시 or 몰인몰 1Depth 카테고리 ID Array", required = false)
	private ArrayList<String> masterCategoryIdArray;

	@ApiModelProperty(value = "카테고리 DEPTH", required = true)
	private String depth;

	@ApiModelProperty(value = "기본값 여부", required = true)
	private String basicYn;

	@ApiModelProperty(value = "기본값 여부(몰인몰)", required = true)
	private String mallInMallBasicYn;

	@ApiModelProperty(value = "등록자", required = false)
	private String createId;

	@ApiModelProperty(value = "수정자", required = false)
	private String modifyId;
}
