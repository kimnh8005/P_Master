package kr.co.pulmuone.v1.goods.claiminfo.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "ClaimInfoRequestDto")
public class ClaimInfoRequestDto extends BaseRequestDto {

	@ApiModelProperty(value = "배송/반품/취소 안내 PK")
	private long ilClaimInfoId;

	@ApiModelProperty(value = "상품 유형")
	private String goodsType;

	@ApiModelProperty(value = "품목 유형")
	private String itemType;

	@ApiModelProperty(value = "배송 유형")
	private String deliveryType;

	@ApiModelProperty(value = "템플릿명")
	private String templateName;

	@ApiModelProperty(value = "사용여부(Y: 사용)")
	private String useYn;

	@ApiModelProperty(value = "상세정보")
	private String describe;

}
