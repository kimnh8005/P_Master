package kr.co.pulmuone.v1.goods.goods.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.aop.service.UserMaskingUserName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "배송정책 Vo")
public class GoodsRegistShippingTemplateVo {

	@ApiModelProperty(value="배송유형별 배송정책 ID")
	String ilGoodsShippingTemplateId;

	@ApiModelProperty(value="배송정책 ORIGINAL ID")
	String origIlShippingTemplateId;

	@ApiModelProperty(value="배송정책 ORIGINAL ID")
	String itemWarehouseShippingTemplateList;

	@ApiModelProperty(value="배송정책명")
	String shppingTemplateName;

	@ApiModelProperty(value="출고처 ID")
	String urWarehouseId;

	@ApiModelProperty(value="등록자 ID")
	String createId;

	@ApiModelProperty(value="등록일")
	String createDate;

	@ApiModelProperty(value="수정자 ID")
	String modifyId;

	@ApiModelProperty(value="수정일")
	String modifyDate;
}
