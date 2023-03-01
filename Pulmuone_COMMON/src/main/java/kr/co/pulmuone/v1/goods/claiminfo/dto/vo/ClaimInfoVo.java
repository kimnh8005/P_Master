package kr.co.pulmuone.v1.goods.claiminfo.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "ClaimInfoVo")
public class ClaimInfoVo {

	@ApiModelProperty(value = "배송/반품/취소 안내 PK")
	private long ilClaimInfoId;

	@ApiModelProperty(value = "품목 유형")
	private String goodsType;

	@ApiModelProperty(value = "품목 유형명")
	private String goodsTypeName;

	@ApiModelProperty(value = "상품 유형")
	private String itemType;

	@ApiModelProperty(value = "상품 유형명")
	private String itemTypeName;

	@ApiModelProperty(value = "템플릿명")
	private String templateName;

	@ApiModelProperty(value = "사용여부(Y: 사용)")
	private String useYn;

	@ApiModelProperty(value = "사용여부명칭")
	private String useYnName;

	@ApiModelProperty(value = "상세정보")
	private String describe;


}
