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
@ApiModel(description = "배송정책 리스트 Request")
public class GoodsRegistShippingTemplateDto extends BaseRequestPageDto {

	@ApiModelProperty(value = "배송정책 ID", required = true)
	private String ilGoodsShippingTemplateId;

	@ApiModelProperty(value = "상품ID", required = true)
	private String ilGoodsId;

	@ApiModelProperty(value = "선택한 배송비 템플릿 ID", required = true)
	private String itemWarehouseShippingTemplateList;

	@ApiModelProperty(value = "선택한 배송비 템플릿 명", required = true)
	private String shppingTemplateName;

	@ApiModelProperty(value = "출고처 ID", required = true)
	private String urWarehouseId;

	@ApiModelProperty(value = "등록자", required = false)
	private String createId;

	@ApiModelProperty(value = "수정자", required = false)
	private String modifyId;
}
