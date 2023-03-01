package kr.co.pulmuone.v1.goods.goods.dto;

import java.util.ArrayList;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestPageDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "추가, 추천 상품 Request")
public class GoodsRegistAdditionalGoodsDto extends BaseRequestPageDto {

	@ApiModelProperty(value = "추가 상품ID", required = true)
	private String ilGoodsAdditionalGoodsMappingId;

	@ApiModelProperty(value = "상품ID", required = true)
	private String ilGoodsId;

	@ApiModelProperty(value = "타겟 추가 상품ID", required = true)
	private String targetGoodsId;

	@ApiModelProperty(value = "추가상품명", required = true)
	private String goodsName;

	@ApiModelProperty(value = "판매가", required = true)
	private String salePrice;

	@ApiModelProperty(value = "등록자", required = false)
	private String createId;

	@ApiModelProperty(value = "수정자", required = false)
	private String modifyId;
}
