package kr.co.pulmuone.v1.policy.shoppingsetting.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "GetAuthMenuListRequestDto")
public class GetPolicyShopSettingListRequestDto extends BaseRequestDto{

	@ApiModelProperty(value = "")
	private String stRoleTypeId;

	@ApiModelProperty(value = "")
	private String stMenuGroupId;

	@ApiModelProperty(value = "상점 PK")
	private String stShopId;


}
