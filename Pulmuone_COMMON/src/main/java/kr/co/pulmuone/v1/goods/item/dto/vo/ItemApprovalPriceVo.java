package kr.co.pulmuone.v1.goods.item.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "ItemApprovalPriceVo")
public class ItemApprovalPriceVo {

	@ApiModelProperty(value = "품목가격 승인 PK")
	private String ilItemPriceApprId;

	@ApiModelProperty(value = "품목코드")
	private String ilItemCd;

	@ApiModelProperty(value = "승인상태")
	private String apprStat;

	@ApiModelProperty(value = "처리시점원가")
	private int standardPriceChg;

	@ApiModelProperty(value = "처리시점정상가")
	private int recommendedPriceChg;

	@ApiModelProperty(value = "처리시점마진율")
	private int marginRateChg;

}
