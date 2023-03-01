package kr.co.pulmuone.v1.goods.goods.dto;

import java.util.ArrayList;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.goods.goods.dto.vo.ExhibitGiftResultVo;
import kr.co.pulmuone.v1.goods.goods.dto.vo.GoodsAllModifyVo;
import kr.co.pulmuone.v1.goods.goods.dto.vo.GoodsDailyCycleBulkVo;
import kr.co.pulmuone.v1.goods.goods.dto.vo.GoodsPackageCalcListVo;
import kr.co.pulmuone.v1.goods.goods.dto.vo.GoodsPackageGoodsMappingVo;
import kr.co.pulmuone.v1.goods.goods.dto.vo.GoodsPriceInfoResultVo;
import kr.co.pulmuone.v1.goods.goods.dto.vo.GoodsRegistAdditionalGoodsVo;
import kr.co.pulmuone.v1.goods.goods.dto.vo.GoodsRegistCategoryVo;
import kr.co.pulmuone.v1.goods.goods.dto.vo.GoodsRegistImageVo;
import kr.co.pulmuone.v1.goods.goods.dto.vo.GoodsRegistItemWarehouseVo;
import kr.co.pulmuone.v1.goods.goods.dto.vo.GoodsRegistReserveOptionVo;
import kr.co.pulmuone.v1.goods.goods.dto.vo.GoodsRegistShippingTemplateVo;
import kr.co.pulmuone.v1.goods.goods.dto.vo.GoodsRegistVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "상품일괄수정 Response")
public class GoodsAllModifyResponseDto {

	@ApiModelProperty(value = "")
	private	List<GoodsAllModifyVo> rows = new ArrayList<GoodsAllModifyVo>();

	@ApiModelProperty(value = "추가 상품")
	private List<GoodsRegistAdditionalGoodsVo> goodsAdditionalGoodsMappingList;

	@ApiModelProperty(value = "상품 공지")
	GoodsRegistVo goodsNoticeInfo;
}
